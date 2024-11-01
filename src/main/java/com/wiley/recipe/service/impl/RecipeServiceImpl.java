package com.wiley.recipe.service.impl;

import com.wiley.recipe.domain.exception.RecipeNotFoundException;
import com.wiley.recipe.domain.model.*;
import com.wiley.recipe.repository.RecipeRepository;
import com.wiley.recipe.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private static final String CALORIES = "Calories";


    public RecipeServiceImpl(final RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Page<Recipe> seacrhRecipes(String query, int pageNumber, int size) {
        return recipeRepository.search(query, pageNumber, size);
    }

    @Override
    public RecipeInformation findRecipeById(Long id) {
        RecipeInformation recipeInformation = recipeRepository.findRecipeById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        double totalCalories = calculateTotalCalories(recipeInformation);
        recipeInformation.setTotalCalories(totalCalories);
        return recipeInformation;
    }

    @Override
    public RecipeCaloriesInfo findRecipeCaloriesInfo(Long recipeId, List<Long> excludedIngredientIds) {
        Nutrition nutrition = recipeRepository.findNutritionInformation(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        double totalCalories = getTotalCalories(nutrition);
        List<Ingredient> recipeIngredients = nutrition.getIngredients();

        if (isAllIngredientsExcluded(excludedIngredientIds, recipeIngredients)) {
            return RecipeCaloriesInfo
                    .builder()
                    .totalCalories(0.0)
                    .warning(Warning.builder().message("All specified ingredients are missing from the recipe.").build())
                    .build();
        }

        List<Long> missingIngredientsFromRecipe = excludedIngredientIds
                .stream()
                .filter(ingredient -> recipeIngredients
                        .stream()
                        .noneMatch(i -> Objects.equals(i.getId(), ingredient)))
                .toList();


        Warning warning = null;
        if (!missingIngredientsFromRecipe.isEmpty()) {
            warning = Warning.builder().missingIngredientsFromRecipe(missingIngredientsFromRecipe).build();
        }

        double excludedCalories = calculateExcludedCalories(recipeIngredients, excludedIngredientIds);

        return RecipeCaloriesInfo
                .builder()
                .totalCalories(totalCalories - excludedCalories)
                .warning(warning)
                .build();
    }

    private double calculateTotalCalories(final RecipeInformation recipeInformation) {
        List<Ingredient> ingredients = recipeInformation.getNutrition().getIngredients();
        double totalCalories = Optional
                .ofNullable(ingredients)
                .orElse(new ArrayList<>())
                .stream()
                .flatMap(ingredient -> ingredient.getNutrients().stream())
                .filter(nutrient -> CALORIES.equals(nutrient.getName()))
                .mapToDouble(Nutrient::getAmount)
                .sum();

        return Math.round(totalCalories * 100.0) / 100.0;
    }

    private double getTotalCalories(Nutrition nutrition) {
        return nutrition
                .getNutrients()
                .stream()
                .filter(n -> CALORIES.equals(n.getName()))
                .findFirst()
                .map(Nutrient::getAmount)
                .orElse(0.0);
    }

    private boolean isAllIngredientsExcluded(List<Long> excludedIngredientsList, List<Ingredient> recipeIngredients) {
        return new HashSet<>(excludedIngredientsList).containsAll(
                recipeIngredients.stream().map(Ingredient::getId).toList());
    }

    private double calculateExcludedCalories(List<Ingredient> ingredients,
                                             List<Long> excludedIngredientsList) {
        return ingredients
                .stream()
                .filter(ingredient -> excludedIngredientsList.contains(ingredient.getId()))
                .flatMap(ingredient -> ingredient.getNutrients().stream())
                .filter(nutrient -> CALORIES.equals(nutrient.getName()))
                .mapToDouble(Nutrient::getAmount)
                .sum();
    }

}
