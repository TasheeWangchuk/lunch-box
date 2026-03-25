package com.lunchbox.lunch_box.modules.menu.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.menu.dto.request.MenuCategoryRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuCategoryResponse;
import com.lunchbox.lunch_box.modules.menu.service.MenuCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class MenuCategoryController {

    private final MenuCategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> createCategory(@Valid @RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Menu category created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu category updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> getCategory(@PathVariable Long id) {
        MenuCategoryResponse response = categoryService.getCategory(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu category retrieved successfully", response));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponse>>> getCategoriesByRestaurant(@PathVariable Long restaurantId) {
        List<MenuCategoryResponse> response = categoryService.getCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu categories for restaurant retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Menu category deleted successfully", null));
    }
}
