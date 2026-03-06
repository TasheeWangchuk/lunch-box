package com.lunchbox.lunch_box.modules.restaurant.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.UpdateRestaurantRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.RestaurantResponse;
import com.lunchbox.lunch_box.modules.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);
        return new ResponseEntity<>(
                ApiResponse.success(HttpStatus.CREATED.value(), "Restaurant created successfully", restaurant),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantById(@PathVariable Long id) {
        RestaurantResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "Restaurant retrieved successfully", restaurant));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "All restaurants retrieved successfully", restaurants));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request) {
        RestaurantResponse restaurant = restaurantService.updateRestaurant(id, request);
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "Restaurant updated successfully", restaurant));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Restaurant deleted successfully", null));
    }
}
