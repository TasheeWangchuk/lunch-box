package com.lunchbox.lunch_box.modules.menu.repository;

import com.lunchbox.lunch_box.modules.menu.entity.MenuItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, Long> {
    List<MenuItemImage> findByMenuItemId(Long menuItemId);
}
