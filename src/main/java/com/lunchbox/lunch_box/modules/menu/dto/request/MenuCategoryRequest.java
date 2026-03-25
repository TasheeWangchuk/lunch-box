package com.lunchbox.lunch_box.modules.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private Long restaurantId;
}
