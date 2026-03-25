package com.lunchbox.lunch_box.modules.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MenuItemRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    private List<String> imageUrls;
    private Boolean available;
}
