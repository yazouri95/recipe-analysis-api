package com.wiley.recipe.repository;

import com.wiley.recipe.domain.model.Nutrition;
import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeInformation;

import java.util.Optional;

public interface RecipeRepository {
    Page<Recipe> search(String query, int pageSize, int page);

    Optional<RecipeInformation> findRecipeById(Long id);

    Optional<Nutrition> findNutritionInformation(Long recipeId);
}
