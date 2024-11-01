package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recipe {
    private Long id;
    private String title;
    private String image;
    private String imageType;
}
