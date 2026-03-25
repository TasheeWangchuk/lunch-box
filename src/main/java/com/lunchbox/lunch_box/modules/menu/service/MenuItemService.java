package com.lunchbox.lunch_box.modules.menu.service;

import com.lunchbox.lunch_box.modules.menu.dto.request.MenuItemRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    MenuItemResponse createMenuItem(MenuItemRequest request);
    MenuItemResponse updateMenuItem(Long id, MenuItemRequest request);
    MenuItemResponse getMenuItem(Long id);
    List<MenuItemResponse> getMenuItemsByCategory(Long categoryId);
    List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId);
    void deleteMenuItem(Long id);
}
