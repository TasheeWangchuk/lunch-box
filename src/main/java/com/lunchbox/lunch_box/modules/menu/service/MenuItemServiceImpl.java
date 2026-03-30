package com.lunchbox.lunch_box.modules.menu.service;

import com.lunchbox.lunch_box.modules.menu.dto.request.MenuItemRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuItemResponse;
import com.lunchbox.lunch_box.modules.menu.entity.MenuCategory;
import com.lunchbox.lunch_box.modules.menu.entity.MenuItem;
import com.lunchbox.lunch_box.modules.menu.entity.MenuItemImage;
import com.lunchbox.lunch_box.modules.menu.mapper.MenuMapper;
import com.lunchbox.lunch_box.modules.menu.repository.MenuCategoryRepository;
import com.lunchbox.lunch_box.modules.menu.repository.MenuItemRepository;
import com.lunchbox.lunch_box.modules.restaurant.entity.Restaurant;
import com.lunchbox.lunch_box.modules.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository itemRepository;
    private final MenuCategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;

    @Override
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        item.setIsVeg(request.getIsVeg());
        item.setSpicyLevel(request.getSpicyLevel());
        item.setPrepTimeMinutes(request.getPrepTimeMinutes());
        item.setRestaurant(restaurant);
        item.setCategory(category);

        if (request.getImageUrls() != null) {
            List<MenuItemImage> images = request.getImageUrls().stream()
                    .map(url -> {
                        MenuItemImage image = new MenuItemImage();
                        image.setImageUrl(url);
                        image.setMenuItem(item);
                        return image;
                    }).collect(Collectors.toList());
            item.setImages(images);
        }

        return menuMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setAvailable(request.getAvailable());
        item.setIsVeg(request.getIsVeg());
        item.setSpicyLevel(request.getSpicyLevel());
        item.setPrepTimeMinutes(request.getPrepTimeMinutes());

        if (request.getCategoryId() != null && !request.getCategoryId().equals(item.getCategory().getId())) {
            MenuCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            item.setCategory(category);
        }

        if (request.getImageUrls() != null) {
            item.getImages().clear();
            List<MenuItemImage> images = request.getImageUrls().stream()
                    .map(url -> {
                        MenuItemImage image = new MenuItemImage();
                        image.setImageUrl(url);
                        image.setMenuItem(item);
                        return image;
                    }).collect(Collectors.toList());
            item.getImages().addAll(images);
        }

        return menuMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItem(Long id) {
        return itemRepository.findById(id)
                .map(menuMapper::toItemResponse)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(menuMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        return itemRepository.findByRestaurantId(restaurantId).stream()
                .map(menuMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMenuItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found");
        }
        itemRepository.deleteById(id);
    }
}
