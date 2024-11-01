package com.wiley.recipe.client.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NutritionResponse {
    @JsonAlias(value = "nutrients")
    List<NutrientResponse> nutrients;
    @JsonAlias(value = "ingredients")
    List<IngredientResponse> ingredients;
}
