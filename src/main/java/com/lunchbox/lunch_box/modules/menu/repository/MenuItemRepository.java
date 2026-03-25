package com.lunchbox.lunch_box.modules.menu.repository;

import com.lunchbox.lunch_box.modules.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryId(Long categoryId);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    Optional<MenuItem> findByNameAndCategoryId(String name, Long categoryId);
}
