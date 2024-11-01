package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WarningDTO {
    private String message;
    private List<Long> missingIngredientsFromRecipe;

}
