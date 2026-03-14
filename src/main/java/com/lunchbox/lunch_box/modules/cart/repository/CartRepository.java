package com.lunchbox.lunch_box.modules.cart.repository;

import com.lunchbox.lunch_box.modules.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
