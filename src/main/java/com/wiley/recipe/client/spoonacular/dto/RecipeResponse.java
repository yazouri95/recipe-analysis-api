package com.wiley.recipe.client.spoonacular.dto;

import lombok.Data;

@Data
public class RecipeResponse {
    private Long id;
    private String title;
    private String image;
    private String imageType;
}
