package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.UpdateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {
    RestaurantResponse createRestaurant(CreateRestaurantRequest request);

    RestaurantResponse getRestaurantById(Long id);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request);

    void deleteRestaurant(Long id);
}
