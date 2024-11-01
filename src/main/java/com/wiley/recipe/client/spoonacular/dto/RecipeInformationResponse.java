package com.wiley.recipe.client.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class RecipeInformationResponse {
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
    private double pricePerServing;
    private boolean cheap;
    private boolean glutenFree;
    private List<IngredientResponse> extendedIngredients;
    private String instructions;
    @JsonAlias(value = "nutrition")
    private NutritionResponse nutrition;
}
