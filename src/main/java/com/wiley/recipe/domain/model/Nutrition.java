package com.wiley.recipe.domain.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Nutrition {
    List<Nutrient> nutrients;
    List<Ingredient> ingredients;
}
