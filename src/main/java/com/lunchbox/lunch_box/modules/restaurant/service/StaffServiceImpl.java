package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;
import com.lunchbox.lunch_box.common.exception.ConflictException;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.common.exception.UnauthorizedException;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.entity.RestaurantUser;
import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;
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

        User staff = new User();
        staff.setUsername(request.getUsername());
        staff.setEmail(request.getEmail());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setPhone(request.getPhone());
        // Staff users hold CUSTOMER as their global platform role.
        // Their restaurant-level role (STAFF) is tracked in RestaurantUser.
        Role customerRole = roleRepository.findByName(AppRole.CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.CUSTOMER)));
        staff.getRoles().add(customerRole);
        staff.setProvider(AuthProvider.LOCAL);
        staff.setActive(true);
        staff.setEmailVerified(false);
        User saved = userRepository.save(staff);

        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setRestaurant(restaurant);
        restaurantUser.setUser(saved);
        // RestaurantRole tracks the user's role within this specific restaurant
        restaurantUser.setRole(RestaurantRole.STAFF);
        restaurantUser.setActive(true);
        restaurantUserRepository.save(restaurantUser);

        return mapToResponse(saved, restaurantUser);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        // Verify the user actually has a restaurant assignment (i.e. is really staff)
        RestaurantUser mapping = restaurantUserRepository.findByUserId(id).stream()
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException("Staff not found or user has no restaurant assignment"));

        User staff = mapping.getUser();

        // Option 2: Admin can view any profile; a staff member can only view their own
        UserDetailsImpl me = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        boolean isAdmin = me.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !me.getId().equals(id)) {
            throw new UnauthorizedException("You can only view your own profile");
        }

        return mapToResponse(staff, mapping);
    }

    @Override
    public List<StaffResponse> getStaffByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }

        return restaurantUserRepository.findByRestaurantId(restaurantId).stream()
                .map(ru -> mapToResponse(ru.getUser(), ru))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaff(Long id) {
        // Verify via RestaurantUser — no longer relies on AppRole.STAFF
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        boolean isRestaurantMember = restaurantUserRepository.findByUserId(id).stream()
                .anyMatch(ru -> ru.getRole() == RestaurantRole.STAFF || ru.getRole() == RestaurantRole.MANAGER);
        if (!isRestaurantMember) {
            throw new ResourceNotFoundException("User has no restaurant assignment");
        }

        RestaurantUser mapping = restaurantUserRepository.findByUserId(staff.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Staff mapping not found"));

        Restaurant restaurant = mapping.getRestaurant();
        if (restaurant == null) {
            throw new ResourceNotFoundException("Restaurant mapping is invalid");
        }

        restaurantUserRepository.delete(mapping);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StaffResponse updateStaffRole(Long staffId, RestaurantRole newRole) {
        RestaurantUser mapping = restaurantUserRepository.findByUserId(staffId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Staff mapping not found"));

        mapping.setRole(newRole);
        restaurantUserRepository.save(mapping);

        return mapToResponse(mapping.getUser(), mapping);
    }

    @Override
    public List<StaffResponse> getAllStaff() {
        return restaurantUserRepository.findAll().stream()
                .map(ru -> mapToResponse(ru.getUser(), ru))
                .collect(Collectors.toList());
    }

    private StaffResponse mapToResponse(User user, Restaurant restaurant) {
        RestaurantUser mapping = restaurantUserRepository.findByUserId(user.getId()).stream()
                .findFirst()
                .orElse(null);
        return mapToResponse(user, mapping);
    }

    private StaffResponse mapToResponse(User user, RestaurantUser mapping) {
        Restaurant restaurant = mapping != null ? mapping.getRestaurant() : null;
        return StaffResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(mapping != null ? mapping.getActive() : user.getActive())
                .role(mapping != null ? mapping.getRole() : null)
                .restaurantId(restaurant != null ? restaurant.getId() : null)
                .restaurantName(restaurant != null ? restaurant.getName() : null)
                .joinedAt(mapping != null ? mapping.getJoinedAt() : user.getCreatedAt())
                .build();
    }
}
