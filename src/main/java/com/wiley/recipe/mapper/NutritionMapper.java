package com.wiley.recipe.mapper;

import com.wiley.recipe.client.spoonacular.dto.NutritionResponse;
import com.wiley.recipe.domain.model.Nutrition;

public class NutritionMapper {

    private NutritionMapper() {
    }

    public static Nutrition toNutritionModel(NutritionResponse nutritionResponse) {
        if (nutritionResponse == null) {
            return null;
        }
        return Nutrition
                .builder()
                .ingredients(IngredientMapper.toIngredientModelList(nutritionResponse.getIngredients()))
                .nutrients(NutrientMapper.toNutrientModelList(nutritionResponse.getNutrients()))
                .build();
    }
}
