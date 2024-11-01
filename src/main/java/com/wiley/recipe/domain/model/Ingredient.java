package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Ingredient {
    private Long id;
    private String name;
    private double amount;
    private String unit;
    private List<Nutrient> nutrients;
}
