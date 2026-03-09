package com.lunchbox.lunch_box.modules.restaurant.entity;

import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private AppRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;
}
