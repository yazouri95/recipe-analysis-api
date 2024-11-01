package com.wiley.recipe.client.spoonacular;

import com.wiley.recipe.client.spoonacular.dto.NutritionResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeInformationResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeSearchResponse;

public interface SpoonacularClient {
    RecipeSearchResponse searchRecipes(String query, int pageNumber, int size);

    RecipeInformationResponse findRecipeInformationById(Long id);

    NutritionResponse findRecipeNutritionInformation(Long id);

}
