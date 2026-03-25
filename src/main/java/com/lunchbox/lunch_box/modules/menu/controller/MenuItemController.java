package com.lunchbox.lunch_box.modules.menu.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.menu.dto.request.MenuItemRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuItemResponse;
import com.lunchbox.lunch_box.modules.menu.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse response = menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Menu item created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse response = menuItemService.updateMenuItem(id, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu item updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItem(@PathVariable Long id) {
        MenuItemResponse response = menuItemService.getMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu item retrieved successfully", response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        List<MenuItemResponse> response = menuItemService.getMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu items for category retrieved successfully", response));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemResponse> response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu items for restaurant retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu item deleted successfully", null));
    }
}
