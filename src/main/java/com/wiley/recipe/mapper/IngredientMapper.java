package com.wiley.recipe.mapper;

import com.wiley.recipe.client.spoonacular.dto.IngredientResponse;
import com.wiley.recipe.domain.model.Ingredient;
import com.wiley.recipe.dto.IngredientDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientMapper {

    private IngredientMapper() {
    }

    public static IngredientDTO toIngredientDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .amount(ingredient.getAmount())
                .unit(ingredient.getUnit())
                .nutrients(NutrientMapper.toNutrientDTOs(ingredient.getNutrients()))
                .build();
    }

    public static List<IngredientDTO> toIngredientDTOs(List<Ingredient> ingredients) {
        return Optional.ofNullable(ingredients)
                .orElse(new ArrayList<>())
                .stream()
                .map(IngredientMapper::toIngredientDTO)
                .toList();
    }

    public static Ingredient toIngredientModel(IngredientResponse ingredientResponse) {
        return Ingredient.builder()
                .id(ingredientResponse.getId())
                .name(ingredientResponse.getName())
                .amount(ingredientResponse.getAmount())
                .unit(ingredientResponse.getUnit())
                .nutrients(NutrientMapper.toNutrientModelList(ingredientResponse.getNutrients()))
                .build();
    }

    public static List<Ingredient> toIngredientModelList(List<IngredientResponse> ingredientResponses) {
        return Optional
                .ofNullable(ingredientResponses)
                .orElse(new ArrayList<>())
                .stream()
                .map(IngredientMapper::toIngredientModel)
                .toList();
    }

}
