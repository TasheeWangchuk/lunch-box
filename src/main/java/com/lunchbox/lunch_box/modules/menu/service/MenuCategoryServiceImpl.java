package com.lunchbox.lunch_box.modules.menu.service;

import com.lunchbox.lunch_box.modules.menu.dto.request.MenuCategoryRequest;
import com.lunchbox.lunch_box.modules.menu.dto.response.MenuCategoryResponse;
import com.lunchbox.lunch_box.modules.menu.entity.MenuCategory;
import com.lunchbox.lunch_box.modules.menu.mapper.MenuMapper;
import com.lunchbox.lunch_box.modules.menu.repository.MenuCategoryRepository;
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
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;

    @Override
    public MenuCategoryResponse createCategory(MenuCategoryRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        MenuCategory category = new MenuCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        category.setActive(request.getActive() != null ? request.getActive() : true);
        category.setRestaurant(restaurant);

        return menuMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public MenuCategoryResponse updateCategory(Long id, MenuCategoryRequest request) {
        MenuCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        return menuMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuCategoryResponse getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(menuMapper::toCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> getCategoriesByRestaurant(Long restaurantId) {
        return categoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId).stream()
                .map(menuMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
