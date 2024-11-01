package com.wiley.recipe.domain.exception;

public class SpoonacualrUnauthorizedException extends DomainException {

    public SpoonacualrUnauthorizedException() {
        super("Unauthorized access to Spoonacular API");
    }
}
