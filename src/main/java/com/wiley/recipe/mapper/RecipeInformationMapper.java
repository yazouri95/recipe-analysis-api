package com.wiley.recipe.mapper;

import com.wiley.recipe.client.spoonacular.dto.RecipeInformationResponse;
import com.wiley.recipe.domain.model.RecipeInformation;
import com.wiley.recipe.dto.RecipeInformationDTO;

public class RecipeInformationMapper {

    private RecipeInformationMapper() {
    }

    public static RecipeInformationDTO toRecipeInformationDTO(RecipeInformation recipeInformation) {
        return RecipeInformationDTO.builder()
                .id(recipeInformation.getId())
                .title(recipeInformation.getTitle())
                .image(recipeInformation.getImage())
                .imageType(recipeInformation.getImageType())
                .servings(recipeInformation.getServings())
                .readyInMinutes(recipeInformation.getReadyInMinutes())
                .summary(recipeInformation.getSummary())
                .sourceName(recipeInformation.getSourceName())
                .sourceUrl(recipeInformation.getSourceUrl())
                .healthScore(recipeInformation.getHealthScore())
                .glutenFree(recipeInformation.isGlutenFree())
                .ingredients(IngredientMapper.toIngredientDTOs(recipeInformation.getIngredients()))
                .instructions(recipeInformation.getInstructions())
                .totalCalories(recipeInformation.getTotalCalories())
                .build();
    }


    public static RecipeInformation toRecipeInformationModel(RecipeInformationResponse recipeInformationResponse) {
        if (recipeInformationResponse == null) {
            return null;
        }
        return RecipeInformation.builder()
                .id(recipeInformationResponse.getId())
                .title(recipeInformationResponse.getTitle())
                .image(recipeInformationResponse.getImage())
                .imageType(recipeInformationResponse.getImageType())
                .servings(recipeInformationResponse.getServings())
                .readyInMinutes(recipeInformationResponse.getReadyInMinutes())
                .summary(recipeInformationResponse.getSummary())
                .sourceName(recipeInformationResponse.getSourceName())
                .sourceUrl(recipeInformationResponse.getSourceUrl())
                .healthScore(recipeInformationResponse.getHealthScore())
                .glutenFree(recipeInformationResponse.isGlutenFree())
                .ingredients(IngredientMapper.toIngredientModelList(recipeInformationResponse.getExtendedIngredients()))
                .instructions(recipeInformationResponse.getInstructions())
                .nutrition(NutritionMapper.toNutritionModel(recipeInformationResponse.getNutrition()))
                .build();
    }
}
