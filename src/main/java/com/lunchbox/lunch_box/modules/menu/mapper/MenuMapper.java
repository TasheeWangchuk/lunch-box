package com.lunchbox.lunch_box.modules.menu.mapper;

import com.lunchbox.lunch_box.modules.menu.dto.response.MenuCategoryResponse;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuItemResponse;
import com.lunchbox.lunch_box.modules.menu.entity.MenuCategory;
import com.lunchbox.lunch_box.modules.menu.entity.MenuItem;
import com.lunchbox.lunch_box.modules.menu.entity.MenuItemImage;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public MenuCategoryResponse toCategoryResponse(MenuCategory category) {
        if (category == null) {
            return null;
        }

        return MenuCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .displayOrder(category.getDisplayOrder())
                .active(category.getActive())
                .restaurantId(category.getRestaurant().getId())
                .build();
    }

    public MenuItemResponse toItemResponse(MenuItem item) {
        if (item == null) {
            return null;
        }

        return MenuItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .categoryId(item.getCategory().getId())
                .categoryName(item.getCategory().getName())
                .restaurantId(item.getRestaurant().getId())
                .price(item.getPrice())
                .available(item.getAvailable())
                .isVeg(item.getIsVeg())
                .spicyLevel(item.getSpicyLevel())
                .prepTimeMinutes(item.getPrepTimeMinutes())
                .imageUrls(item.getImages().stream()
                        .map(MenuItemImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
