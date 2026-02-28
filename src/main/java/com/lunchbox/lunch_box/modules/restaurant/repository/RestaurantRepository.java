package com.lunchbox.lunch_box.modules.restaurant.repository;

import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
