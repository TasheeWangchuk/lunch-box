package com.lunchbox.lunch_box.modules.restaurant.repository;

import com.lunchbox.lunch_box.modules.restaurant.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findByRestaurantIdOrderByDisplayOrderAsc(Long restaurantId);
}
