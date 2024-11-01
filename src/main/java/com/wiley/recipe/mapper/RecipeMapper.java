package com.wiley.recipe.mapper;

import com.wiley.recipe.client.spoonacular.dto.RecipeResponse;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.dto.RecipeDTO;

import java.util.List;

public class RecipeMapper {

    private RecipeMapper() {
    }

    public static RecipeDTO toRecipeDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .image(recipe.getImage())
                .build();
    }

    public static List<RecipeDTO> toRecipeDTOs(List<Recipe> recipeList) {
        return recipeList
                .stream()
                .map(RecipeMapper::toRecipeDTO)
                .toList();
    }

    public static Recipe toRecipeModel(RecipeResponse recipeResponse) {
        return Recipe.builder()
                .id(recipeResponse.getId())
                .title(recipeResponse.getTitle())
                .image(recipeResponse.getImage())
                .imageType(recipeResponse.getImageType())
                .build();
    }

    public static List<Recipe> toRecipeModelList(List<RecipeResponse> recipeResponseList) {
        return recipeResponseList
                .stream()
                .map(RecipeMapper::toRecipeModel)
                .toList();
    }
}
