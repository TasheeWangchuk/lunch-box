package com.lunchbox.lunch_box.modules.user.repository;

import com.lunchbox.lunch_box.modules.user.entity.AuthProvider;
import com.lunchbox.lunch_box.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);
}
