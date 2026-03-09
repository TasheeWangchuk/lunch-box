package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;
import com.lunchbox.lunch_box.common.exception.ConflictException;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.common.exception.UnauthorizedException;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.entity.RestaurantUser;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantRepository;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantUserRepository;
import com.lunchbox.lunch_box.modules.user.entity.Role;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
import com.lunchbox.lunch_box.modules.user.repository.RoleRepository;
import com.lunchbox.lunch_box.modules.user.repository.UserRepository;
import com.lunchbox.lunch_box.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantUserRepository restaurantUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email is already registered");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Username is already taken");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to add staff to this restaurant");
        }

        User staff = new User();
        staff.setUsername(request.getUsername());
        staff.setEmail(request.getEmail());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setPhone(request.getPhone());
        Role staffRole = roleRepository.findByName(AppRole.STAFF)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.STAFF)));
        staff.getRoles().add(staffRole);
        staff.setProvider(AuthProvider.LOCAL);
        staff.setActive(true);
        staff.setEmailVerified(false);
        User saved = userRepository.save(staff);

        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setRestaurant(restaurant);
        restaurantUser.setUser(saved);
        restaurantUser.setRole(AppRole.STAFF);
        restaurantUser.setActive(true);
        restaurantUserRepository.save(restaurantUser);

        return mapToResponse(saved, restaurant);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        boolean isStaff = staff.getRoles().stream().anyMatch(r -> r.getName() == AppRole.STAFF);
        if (!isStaff) {
            throw new ResourceNotFoundException("User is not a staff member");
        }

        RestaurantUser mapping = restaurantUserRepository.findByUserId(staff.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Staff mapping not found"));

        return mapToResponse(staff, mapping.getRestaurant());
    }

    @Override
    public List<StaffResponse> getStaffByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to view staff for this restaurant");
        }

        return restaurantUserRepository.findByRestaurantIdAndRole(restaurantId, AppRole.STAFF).stream()
                .map(ru -> mapToResponse(ru.getUser(), ru.getRestaurant()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaff(Long id) {
        User staff = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        boolean isStaff = staff.getRoles().stream().anyMatch(r -> r.getName() == AppRole.STAFF);
        if (!isStaff) {
            throw new ResourceNotFoundException("User is not a staff member");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        RestaurantUser mapping = restaurantUserRepository.findByUserId(staff.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Staff mapping not found"));

        Restaurant restaurant = mapping.getRestaurant();
        if (restaurant == null || restaurant.getOwner() == null
                || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to delete staff from this restaurant");
        }

        restaurantUserRepository.delete(mapping);
        userRepository.deleteById(id);
    }

    private StaffResponse mapToResponse(User user, Restaurant restaurant) {
        return StaffResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .active(user.getActive())
                .restaurantId(restaurant != null ? restaurant.getId() : null)
                .restaurantName(restaurant != null ? restaurant.getName() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
