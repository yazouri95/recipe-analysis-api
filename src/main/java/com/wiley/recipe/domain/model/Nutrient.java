package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Nutrient {
    private String name;
    private double amount;
    private String unit;
    private double percentOfDailyNeeds;
}
