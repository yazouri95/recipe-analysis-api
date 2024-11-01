package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Warning {
    private String message;
    private List<Long> missingIngredientsFromRecipe;
}
