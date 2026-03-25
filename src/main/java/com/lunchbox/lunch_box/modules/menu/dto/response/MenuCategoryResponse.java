package com.lunchbox.lunch_box.modules.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private Long restaurantId;
}
