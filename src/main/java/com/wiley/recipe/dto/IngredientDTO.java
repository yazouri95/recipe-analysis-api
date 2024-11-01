package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IngredientDTO {
    private Long id;
    private String name;
    private double amount;
    private String unit;
    private List<NutrientDTO> nutrients;

}
