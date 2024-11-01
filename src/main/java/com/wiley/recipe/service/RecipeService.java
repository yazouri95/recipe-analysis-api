package com.wiley.recipe.service;

import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeCaloriesInfo;
import com.wiley.recipe.domain.model.RecipeInformation;

import java.util.List;


public interface RecipeService {
    Page<Recipe> seacrhRecipes(String query, int pageNumber, int size);

    RecipeInformation findRecipeById(Long id);

    RecipeCaloriesInfo findRecipeCaloriesInfo(Long recipeId, List<Long> excludedIngredientIds);

}
