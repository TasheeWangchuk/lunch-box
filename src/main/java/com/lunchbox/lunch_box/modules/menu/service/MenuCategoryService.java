package com.lunchbox.lunch_box.modules.menu.service;

import com.lunchbox.lunch_box.modules.menu.dto.request.MenuCategoryRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuCategoryResponse;

import java.util.List;

public interface MenuCategoryService {
    MenuCategoryResponse createCategory(MenuCategoryRequest request);
    MenuCategoryResponse updateCategory(Long id, MenuCategoryRequest request);
    MenuCategoryResponse getCategory(Long id);
    List<MenuCategoryResponse> getCategoriesByRestaurant(Long restaurantId);
    void deleteCategory(Long id);
}
