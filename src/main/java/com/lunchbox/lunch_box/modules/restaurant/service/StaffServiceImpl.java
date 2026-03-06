package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;
import com.lunchbox.lunch_box.common.exception.ConflictException;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.common.exception.UnauthorizedException;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantRepository;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
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

        // Big Tech Style: Audit/Permission Check
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
        staff.setRole(AppRole.STAFF);
        staff.setProvider(AuthProvider.LOCAL);
        staff.setActive(true);
        staff.setRestaurant(restaurant);
        staff.setEmailVerified(false);

        User saved = userRepository.save(staff);
        return mapToResponse(saved);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        if (staff.getRole() != AppRole.STAFF) {
            throw new ResourceNotFoundException("User is not a staff member");
        }

        return mapToResponse(staff);
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

        return userRepository.findByRestaurantIdAndRole(restaurantId, AppRole.STAFF).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaff(Long id) {
        User staff = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        if (staff.getRole() != AppRole.STAFF) {
            throw new ResourceNotFoundException("User is not a staff member");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Restaurant restaurant = staff.getRestaurant();
        if (restaurant == null || restaurant.getOwner() == null
                || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to delete staff from this restaurant");
        }

        userRepository.deleteById(id);
    }

    private StaffResponse mapToResponse(User user) {
        return StaffResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .active(user.getActive())
                .restaurantId(user.getRestaurant() != null ? user.getRestaurant().getId() : null)
                .restaurantName(user.getRestaurant() != null ? user.getRestaurant().getName() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
