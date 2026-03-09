package com.lunchbox.lunch_box.modules.restaurant.repository;

import com.lunchbox.lunch_box.modules.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryId(Long categoryId);

    List<MenuItem> findByRestaurantId(Long restaurantId);
}
