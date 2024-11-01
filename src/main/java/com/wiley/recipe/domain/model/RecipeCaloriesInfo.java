package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeCaloriesInfo {
    private double totalCalories;
    private Warning warning;
}
