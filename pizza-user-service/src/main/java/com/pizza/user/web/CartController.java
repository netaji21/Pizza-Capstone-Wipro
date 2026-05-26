package com.pizza.user.web;

import com.pizza.user.dto.CartDto;
import com.pizza.user.dto.CartItemDto;
import com.pizza.user.dto.MenuItemDto;
import com.pizza.user.dto.OrderResponseDto;
import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.service.MenuBrowseService;
import com.pizza.user.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/customers/cart")
public class CartController {

    public static final String CART_SESSION_KEY = "cart";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuBrowseService menuBrowseService;
    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    public CartController(MenuBrowseService menuBrowseService,
                          OrderService orderService,
                          CustomerRepository customerRepository) {
        this.menuBrowseService = menuBrowseService;
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("itemId") Long itemId,
                            @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
                            HttpSession session) {
        try {
            Long customerId = (Long) session.getAttribute("loggedInCustomerId");
            if (customerId == null) return "redirect:/customers/login";

            if (quantity == null || quantity < 1) {
                return "redirect:/customers/menu?error=Quantity must be at least 1";
            }

            MenuItemDto menuItem = menuBrowseService.getMenuItemById(itemId);
            if (menuItem == null) return "redirect:/customers/menu?error=Menu item not found";
            if (!menuItem.isAvailable()) return "redirect:/customers/menu?error=Item is not available";

            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart == null) {
                cart = new CartDto();
                session.setAttribute(CART_SESSION_KEY, cart);
            }

            CartItemDto cartItem = new CartItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), Math.max(1, quantity));
            cart.addItem(cartItem);

            return "redirect:/customers/menu?msg=Added to cart";
        } catch (Exception ex) {
            log.error("Error in addToCart", ex);
            return "redirect:/customers/menu?error=Could not add to cart";
        }
    }

    // Increase quantity endpoint
    @PostMapping("/increase")
    public String increaseItem(@RequestParam("itemId") Long itemId, HttpSession session) {
        try {
            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart != null) {
                cart.increaseItem(itemId);
            }
            return "redirect:/customers/cart";
        } catch (Exception ex) {
            log.error("Error in increaseItem", ex);
            return "redirect:/customers/cart?error=Update failed";
        }
    }

    // Decrease quantity endpoint
    @PostMapping("/decrease")
    public String decreaseItem(@RequestParam("itemId") Long itemId, HttpSession session) {
        try {
            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart != null) {
                cart.decreaseItem(itemId);
            }
            return "redirect:/customers/cart";
        } catch (Exception ex) {
            log.error("Error in decreaseItem", ex);
            return "redirect:/customers/cart?error=Update failed";
        }
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model,
                           @RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "msg", required = false) String msg) {
        try {
            Long customerId = (Long) session.getAttribute("loggedInCustomerId");
            if (customerId == null) return "redirect:/customers/login";

            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart == null) cart = new CartDto();

            model.addAttribute("cart", cart);
            model.addAttribute("cartTotal", cart.getTotal());

            if (error != null) model.addAttribute("error", error);
            if (msg != null) model.addAttribute("msg", msg);

            return "user-cart";
        } catch (Exception ex) {
            log.error("Error in viewCart", ex);
            return "redirect:/customers/home?error=view_cart_failed";
        }
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("itemId") Long itemId,
                                 HttpSession session) {
        try {
            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart != null) {
                cart.removeItem(itemId);
            }
            return "redirect:/customers/cart?msg=Item removed";
        } catch (Exception ex) {
            log.error("Error in removeFromCart", ex);
            return "redirect:/customers/cart?error=remove_failed";
        }
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("deliveryMode") String deliveryMode,
                           @RequestParam("paymentMode") String paymentMode,
                           HttpSession session,
                           Model model) {
        try {
            Long customerId = (Long) session.getAttribute("loggedInCustomerId");
            if (customerId == null) return "redirect:/customers/login";

            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart == null || cart.isEmpty()) {
                model.addAttribute("error", "Your cart is empty.");
                model.addAttribute("cart", new CartDto());
                model.addAttribute("cartTotal", "0.00");
                return "user-cart";
            }

            for (CartItemDto it : cart.getItems()) {
                if (it.getQuantity() < 1) {
                    model.addAttribute("error", "Cart contains invalid quantity for '" + it.getName() + "'.");
                    model.addAttribute("cart", cart);
                    model.addAttribute("cartTotal", cart.getTotal());
                    return "user-cart";
                }
                MenuItemDto menu = menuBrowseService.getMenuItemById(it.getMenuItemId());
                if (menu == null || !menu.isAvailable()) {
                    model.addAttribute("error", "Item '" + it.getName() + "' is out of stock. Please remove it.");
                    model.addAttribute("cart", cart);
                    model.addAttribute("cartTotal", cart.getTotal());
                    model.addAttribute("outOfStockItemId", it.getMenuItemId());
                    return "user-cart";
                }
            }

            OrderResponseDto response = orderService.placeOrderFromCart(customerId, cart, deliveryMode, paymentMode);
            Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
            String email = customerOpt.map(CustomerEntity::getEmail).orElse(null);

            model.addAttribute("order", response);
            model.addAttribute("customerEmail", email);
            session.removeAttribute(CART_SESSION_KEY);

            return "user-order-confirmation";
        } catch (Exception ex) {
            log.error("Error in checkout", ex);
            model.addAttribute("error", "Could not process checkout. Please try again.");
            CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
            if (cart == null) cart = new CartDto();
            model.addAttribute("cart", cart);
            model.addAttribute("cartTotal", cart.getTotal());
            return "user-cart";
        }
    }
}