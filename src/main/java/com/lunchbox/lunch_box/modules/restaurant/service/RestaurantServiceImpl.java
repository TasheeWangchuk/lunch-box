package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.UpdateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.RestaurantResponse;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.common.exception.UnauthorizedException;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantRepository;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.repository.UserRepository;
import com.lunchbox.lunch_box.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User owner = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner user not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setContactNumber(request.getContactNumber());
        restaurant.setActive(true);
        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToResponse(saved);
    }

    @Override
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return mapToResponse(restaurant);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to update this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setContactNumber(request.getContactNumber());
        if (request.getActive() != null) {
            restaurant.setActive(request.getActive());
        }

        Restaurant updated = restaurantRepository.save(restaurant);
        return mapToResponse(updated);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this restaurant");
        }

        restaurantRepository.delete(restaurant);
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .contactNumber(restaurant.getContactNumber())
                .active(restaurant.getActive())
                .createdAt(restaurant.getCreatedAt())
                .ownerId(restaurant.getOwner() != null ? restaurant.getOwner().getId() : null)
                .ownerName(restaurant.getOwner() != null ? restaurant.getOwner().getUsername() : null)
                .build();
    }
}
