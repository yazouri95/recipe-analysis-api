package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NutrientDTO {
    private String name;
    private double amount;
    private String unit;
    private double percentOfDailyNeeds;
}
