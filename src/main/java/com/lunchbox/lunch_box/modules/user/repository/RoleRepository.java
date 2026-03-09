package com.lunchbox.lunch_box.modules.user.repository;

import com.lunchbox.lunch_box.modules.user.entity.Role;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(AppRole name);
}
