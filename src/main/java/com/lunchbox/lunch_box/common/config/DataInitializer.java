package com.lunchbox.lunch_box.common.config;

import com.lunchbox.lunch_box.modules.user.entity.Role;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRoles();
    }

    private void seedRoles() {
        log.info("Checking roles in database...");
        Arrays.stream(AppRole.values()).forEach(roleName -> {
            if (roleRepository.findByName(roleName).isEmpty()) {
                log.info("Seeding role: {}", roleName);
                roleRepository.save(new Role(roleName));
            }
        });
        log.info("Roles initialization complete.");
    }
}
