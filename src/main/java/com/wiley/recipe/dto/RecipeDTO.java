package com.wiley.recipe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDTO {
    private Long id;
    private String title;
    private String image;
//    private String readyInMinutes;
//    private String servings;
//    private String spoonacularSourceUrl;

}
