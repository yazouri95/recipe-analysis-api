package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeCaloriesInfoDTO {
    private double totalCalories;
    private WarningDTO warning;
}
