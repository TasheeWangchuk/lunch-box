package com.lunchbox.lunch_box.modules.restaurant.repository;

import com.lunchbox.lunch_box.modules.restaurant.entity.RestaurantUser;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {
    List<RestaurantUser> findByRestaurantId(Long restaurantId);

    List<RestaurantUser> findByRestaurantIdAndRole(Long restaurantId, AppRole role);

    Optional<RestaurantUser> findByRestaurantIdAndUserId(Long restaurantId, Long userId);

    List<RestaurantUser> findByUserId(Long userId);
}
