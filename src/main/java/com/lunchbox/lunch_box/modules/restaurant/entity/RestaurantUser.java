package com.lunchbox.lunch_box.modules.restaurant.entity;

import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;
import com.lunchbox.lunch_box.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurant_users")
public class RestaurantUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The role this user holds within this specific restaurant.
     * Use RestaurantRole (not AppRole) — restaurant-scoped assignment.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private RestaurantRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;
}
