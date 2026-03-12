package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.UpdateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.RestaurantResponse;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setDescription(request.getDescription());
        restaurant.setContactNumber(request.getContactNumber());
        restaurant.setCuisineTypes(request.getCuisineTypes());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setImageUrl(request.getImageUrl());
        restaurant.setLogoUrl(request.getLogoUrl());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());
        restaurant.setActive(true);

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

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setDescription(request.getDescription());
        restaurant.setContactNumber(request.getContactNumber());
        restaurant.setCuisineTypes(request.getCuisineTypes());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setImageUrl(request.getImageUrl());
        restaurant.setLogoUrl(request.getLogoUrl());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());

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

        restaurantRepository.delete(restaurant);
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .description(restaurant.getDescription())
                .contactNumber(restaurant.getContactNumber())
                .cuisineTypes(restaurant.getCuisineTypes())
                .openingHours(restaurant.getOpeningHours())
                .imageUrl(restaurant.getImageUrl())
                .logoUrl(restaurant.getLogoUrl())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .active(restaurant.getActive())
                .createdAt(restaurant.getCreatedAt())
                .build();
    }
}
