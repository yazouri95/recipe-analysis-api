package com.wiley.recipe.client.spoonacular.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NutrientResponse {
    private String name;
    private double amount;
    private String unit;
    private double percentOfDailyNeeds;
}