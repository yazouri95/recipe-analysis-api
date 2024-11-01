package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeInformationDTO {
    private Long id;
    private String title;
    private String image;
    private String imageType;
    private int servings;
    private int readyInMinutes;
    private int cookingMinutes;
    private int preparationMinutes;
    private String summary;
    private String sourceName;
    private String sourceUrl;
    private double healthScore;
    private boolean glutenFree;
    private List<IngredientDTO> ingredients;
    private String instructions;
    private double totalCalories;
}
