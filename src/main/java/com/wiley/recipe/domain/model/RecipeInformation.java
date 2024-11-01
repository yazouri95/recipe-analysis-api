package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RecipeInformation {
    private Long id;
    private String title;
    private String image;
    private String imageType;
    private int servings;
    private int readyInMinutes;
    private String summary;
    private String sourceName;
    private String sourceUrl;
    private double healthScore;
    private boolean glutenFree;
    private List<Ingredient> ingredients;
    private String instructions;
    private Nutrition nutrition;
    private double totalCalories;

}
