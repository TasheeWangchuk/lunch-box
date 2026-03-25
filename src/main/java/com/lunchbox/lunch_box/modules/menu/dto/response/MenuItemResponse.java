package com.lunchbox.lunch_box.modules.menu.dto.response;

import com.lunchbox.lunch_box.modules.menu.enums.SpicyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long restaurantId;
    private BigDecimal price;
    private List<String> imageUrls;
    private Boolean available;
    private Boolean isVeg;
    private SpicyLevel spicyLevel;
    private Integer prepTimeMinutes;
}
