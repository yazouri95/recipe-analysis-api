package com.wiley.recipe.service;

import com.wiley.recipe.domain.exception.RecipeNotFoundException;
import com.wiley.recipe.domain.model.*;
import com.wiley.recipe.repository.RecipeRepository;
import com.wiley.recipe.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void seacrhRecipes_ShouldReturnPageOfRecipes_WhenQueryIsValid() {
        String query = "pasta";
        int pageSize = 10;
        int page = 0;
        Recipe recipe = Recipe.builder()
                .id(716429L)
                .title("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs")
                .image("https://img.spoonacular.com/recipes/716429-312x231.jpg")
                .imageType("jpg")
                .build();
        Page<Recipe> expectedPage = new Page<>(Collections.singletonList(recipe), 1, page, pageSize);
        when(recipeRepository.search(query, page, pageSize)).thenReturn(expectedPage);

        Page<Recipe> result = recipeService.seacrhRecipes(query, page, pageSize);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(recipeRepository).search(query, page, pageSize);
    }

    @Test
    void findRecipeById_ShouldReturnRecipeInformation_WhenRecipeExists() {
        Long recipeId = 1L;

        double totalCalories = 280;
        double totalProtein = 38;
        Nutrient nutrientTotalCalories = Nutrient.builder().name("Calories").amount(totalCalories).build();
        Nutrient nutrientTotalProtein = Nutrient.builder().name("Protein").amount(totalProtein).build();


        Ingredient pastaIngredient = createIngredient(1L, "pasta", 120, 28);
        Ingredient cheeseIngredient = createIngredient(2L, "cheese", 160, 10);


        Nutrition nutrition = Nutrition
                .builder()
                .nutrients(Arrays.asList(nutrientTotalCalories, nutrientTotalProtein))
                .ingredients(Arrays.asList(pastaIngredient, cheeseIngredient))
                .build();


        RecipeInformation expectedRecipeInformation = RecipeInformation.builder()
                .id(recipeId)
                .title("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs")
                .image("https://img.spoonacular.com/recipes/716429-312x231.jpg")
                .imageType("jpg")
                .nutrition(nutrition)
                .build();

        when(recipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(expectedRecipeInformation));

        RecipeInformation result = recipeService.findRecipeById(recipeId);

        assertEquals(expectedRecipeInformation, result);
        verify(recipeRepository).findRecipeById(recipeId);
    }

    @Test
    void findRecipeById_ShouldThrowRecipeNotFoundException_WhenRecipeDoesNotExist() {
        Long recipeId = 1L;
        when(recipeRepository.findRecipeById(recipeId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.findRecipeById(recipeId));
        verify(recipeRepository).findRecipeById(recipeId);
    }

    @Test
    void findRecipeCaloriesInfo_ShouldReturnCaloriesInfo_WhenIngredientsIncluded() {
        Long recipeId = 1L;
        double totalCalories = 280;
        double totalProtein = 38;

        Nutrient nutrientTotalCalories = Nutrient.builder().name("Calories").amount(totalCalories).build();
        Nutrient nutrientTotalProtein = Nutrient.builder().name("Protein").amount(totalProtein).build();

        Ingredient pastaIngredient = createIngredient(1L, "pasta", 120, 28);
        Ingredient cheeseIngredient = createIngredient(2L, "cheese", 160, 10);


        Nutrition expectedNutrition = Nutrition
                .builder()
                .nutrients(Arrays.asList(nutrientTotalCalories, nutrientTotalProtein))
                .ingredients(Arrays.asList(pastaIngredient, cheeseIngredient))
                .build();

        List<Long> excludedIngredients = List.of(cheeseIngredient.getId());
        when(recipeRepository.findNutritionInformation(recipeId)).thenReturn(Optional.of(expectedNutrition));

        RecipeCaloriesInfo result = recipeService.findRecipeCaloriesInfo(recipeId, excludedIngredients);

        assertEquals(120, result.getTotalCalories());
        assertNull(result.getWarning());
        verify(recipeRepository).findNutritionInformation(recipeId);
    }

    @Test
    void findRecipeCaloriesInfo_ShouldThrowRecipeNotFoundException_WhenRecipeDoesNotExist() {
        Long recipeId = 1L;
        when(recipeRepository.findNutritionInformation(recipeId)).thenReturn(Optional.empty());

        List<Long> excludedIngredients = List.of(123L);
        assertThrows(RecipeNotFoundException.class, () -> recipeService.findRecipeCaloriesInfo(recipeId, excludedIngredients));
        verify(recipeRepository).findNutritionInformation(recipeId);
    }

    @Test
    void findRecipeCaloriesInfo_ShouldReturnCaloriesInfoWithWarnings_WhenSomeIngredientsExcludedNotInRecipe() {
        Long recipeId = 1L;
        double totalCalories = 310;
        double totalProtein = 48;
        Nutrient nutrientTotalCalories = Nutrient.builder().name("Calories").amount(totalCalories).build();
        Nutrient nutrientTotalProtein = Nutrient.builder().name("Protein").amount(totalProtein).build();

        Ingredient pastaIngredient = createIngredient(1L, "pasta", 120, 28);
        Ingredient cheeseIngredient = createIngredient(2L, "cheese", 160, 10);
        Ingredient mushroomIngredient = createIngredient(3L, "mushroom", 30, 10);


        Nutrition mockNutrition = Nutrition
                .builder()
                .nutrients(Arrays.asList(nutrientTotalCalories, nutrientTotalProtein))
                .ingredients(Arrays.asList(pastaIngredient, cheeseIngredient, mushroomIngredient))
                .build();


        long onionIngredientId = 1234;
        List<Long> excludedIngredients = List.of(onionIngredientId, mushroomIngredient.getId()); // mushroom is in the recipe, onion is not
        when(recipeRepository.findNutritionInformation(recipeId)).thenReturn(Optional.of(mockNutrition));

        RecipeCaloriesInfo result = recipeService.findRecipeCaloriesInfo(recipeId, excludedIngredients);

        assertEquals(280, result.getTotalCalories());
        assertNotNull(result.getWarning());
        assertEquals(List.of(onionIngredientId), result.getWarning().getMissingIngredientsFromRecipe());
        verify(recipeRepository).findNutritionInformation(recipeId);
    }

    @Test
    void findRecipeCaloriesInfo_ShouldReturnEmptyCaloriesInfo_WhenAllIngredientsExcluded() {
        Long recipeId = 1L;
        double totalCalories = 310;
        double totalProtein = 48;
        Nutrient nutrientTotalCalories = Nutrient.builder().name("Calories").amount(totalCalories).build();
        Nutrient nutrientTotalProtein = Nutrient.builder().name("Protein").amount(totalProtein).build();

        Ingredient pastaIngredient = createIngredient(1L, "pasta", 120, 28);
        Ingredient cheeseIngredient = createIngredient(2L, "cheese", 160, 10);
        Ingredient mushroomIngredient = createIngredient(3L, "mushroom", 30, 10);


        Nutrition mockNutrition = Nutrition
                .builder()
                .nutrients(Arrays.asList(nutrientTotalCalories, nutrientTotalProtein))
                .ingredients(Arrays.asList(pastaIngredient, cheeseIngredient, mushroomIngredient))
                .build();

        List<Long> excludedIngredients = List.of(pastaIngredient.getId(), cheeseIngredient.getId(), mushroomIngredient.getId());
        when(recipeRepository.findNutritionInformation(recipeId)).thenReturn(Optional.of(mockNutrition));

        RecipeCaloriesInfo result = recipeService.findRecipeCaloriesInfo(recipeId, excludedIngredients);

        assertEquals(0.0, result.getTotalCalories());
        assertNotNull(result.getWarning());
        verify(recipeRepository).findNutritionInformation(recipeId);
    }

    public static Ingredient createIngredient(Long id, String name, double calories, double protein) {
        return Ingredient
                .builder()
                .id(id)
                .name(name).
                nutrients(Arrays.asList(Nutrient.builder().name("Calories").amount(calories).build(),
                        Nutrient.builder().name("Protein").amount(protein).build())).build();
    }
}
