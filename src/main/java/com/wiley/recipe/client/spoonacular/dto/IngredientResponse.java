package com.wiley.recipe.client.spoonacular.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IngredientResponse {
    private Long id;
    private String name;
    private double amount;
    private String unit;
    private List<NutrientResponse> nutrients;

}
