package com.wiley.recipe.mapper;

import com.wiley.recipe.domain.model.RecipeCaloriesInfo;
import com.wiley.recipe.domain.model.Warning;
import com.wiley.recipe.dto.RecipeCaloriesInfoDTO;
import com.wiley.recipe.dto.WarningDTO;

public class RecipeCaloriesInfoMapper {

    private RecipeCaloriesInfoMapper() {
    }

    public static RecipeCaloriesInfoDTO toRecipeCaloriesInfoDTO(RecipeCaloriesInfo recipeCaloriesInfo) {
        WarningDTO warningDTO = null;
        Warning warningModel = recipeCaloriesInfo.getWarning();
        if (warningModel != null) {
            warningDTO = WarningDTO.builder()
                    .message(warningModel.getMessage())
                    .missingIngredientsFromRecipe(warningModel.getMissingIngredientsFromRecipe())
                    .build();
        }
        return RecipeCaloriesInfoDTO.builder()
                .totalCalories(recipeCaloriesInfo.getTotalCalories())
                .warning(warningDTO)
                .build();
    }
}
