package com.lunchbox.lunch_box.modules.user.repository;

import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
