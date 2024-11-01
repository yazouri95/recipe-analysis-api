package com.wiley.recipe.domain.exception;

public class RecipeNotFoundException extends DomainException {

    public RecipeNotFoundException(Long id) {
        super(String.format("Recipe with ID %d not found.", id));
    }
}
