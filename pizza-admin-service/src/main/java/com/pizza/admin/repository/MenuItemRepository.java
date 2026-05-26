package com.pizza.admin.repository;

import com.pizza.admin.entity.MenuItemEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    // Advanced search query handling keyword, category, and veg status
    @Query("SELECT m FROM MenuItemEntity m WHERE " +
           "(:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:category IS NULL OR m.category.name = :category) AND " +
           "(:veg IS NULL OR m.veg = :veg)")
    List<MenuItemEntity> searchMenuItems(@Param("keyword") String keyword, 
                                         @Param("category") String category, 
                                         @Param("veg") Boolean veg, 
                                         Sort sort);
}