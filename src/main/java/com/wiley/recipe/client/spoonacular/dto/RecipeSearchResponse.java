package com.wiley.recipe.client.spoonacular.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchResponse {
    private List<RecipeResponse> results;
    private int totalResults;
    private int offset;
    private int number;
}


