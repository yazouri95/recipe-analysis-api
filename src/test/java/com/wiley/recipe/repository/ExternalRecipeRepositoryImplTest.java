package com.wiley.recipe.repository;

import com.wiley.recipe.client.spoonacular.SpoonacularClient;
import com.wiley.recipe.client.spoonacular.dto.*;
import com.wiley.recipe.domain.exception.SpoonacualrUnauthorizedException;
import com.wiley.recipe.domain.exception.SpoonacularClientException;
import com.wiley.recipe.domain.model.Nutrition;
import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeInformation;
import com.wiley.recipe.mapper.NutritionMapper;
import com.wiley.recipe.mapper.RecipeInformationMapper;
import com.wiley.recipe.mapper.RecipeMapper;
import com.wiley.recipe.repository.impl.ExternalRecipeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalRecipeRepositoryImplTest {

    @Mock
    private SpoonacularClient spoonacularClient;

    @InjectMocks
    private ExternalRecipeRepositoryImpl externalRecipeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenSearch() {
        RecipeSearchResponse mockResponse = new RecipeSearchResponse();
        RecipeResponse mockRecipeResponse = new RecipeResponse();
        mockRecipeResponse.setId(1234L);
        mockRecipeResponse.setTitle("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs");
        mockRecipeResponse.setImage("https://img.spoonacular.com/recipes/716429-312x231.jpg");
        mockRecipeResponse.setImageType("jpg");

        mockResponse.setResults(List.of(mockRecipeResponse));
        mockResponse.setTotalResults(1);
        mockResponse.setOffset(0);
        mockResponse.setNumber(1);

        when(spoonacularClient.searchRecipes("pasta", 0, 10)).thenReturn(mockResponse);

        Page<Recipe> result = externalRecipeRepository.search("pasta", 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getResults().size());

        Recipe expectedRecipe = RecipeMapper.toRecipeModel(mockRecipeResponse);

        assertEquals(expectedRecipe, result.getResults().get(0));
    }

    @Test
    void whenSearch_ShouldReturnEmptyPage_WhenHttpStatusNotFound() {
        when(spoonacularClient.searchRecipes("pasta", 0, 10)).thenThrow(new WebClientResponseException(
                "Not found", HttpStatus.NOT_FOUND.value(), "not found", null, null, null));

        Page<Recipe> result = externalRecipeRepository.search("pasta", 0, 10);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getResults().size());
    }

    @Test
    void whenSearch_ShouldThrowException_WhenUnauthorized() {
        when(spoonacularClient.searchRecipes("pasta", 0, 10)).thenThrow(new WebClientResponseException(
                "UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", null, null, null));

        SpoonacualrUnauthorizedException ex = assertThrows(SpoonacualrUnauthorizedException.class, () -> externalRecipeRepository.search("pasta", 0, 10));
        assertThat(ex.getClass()).isEqualTo(SpoonacualrUnauthorizedException.class);
    }

    @Test
    void whenSearch_ShouldThrowException_WhenClientError() {
        when(spoonacularClient.searchRecipes("pasta", 0, 10)).thenThrow(new WebClientResponseException(
                "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal server error", null, null, null));

        SpoonacularClientException ex = assertThrows(SpoonacularClientException.class, () -> externalRecipeRepository.search("pasta", 0, 10));
        assertThat(ex.getClass()).isEqualTo(SpoonacularClientException.class);
    }


    @Test
    void whenFindRecipeById_thenReturnRecipeInformation() {
        Long id = 1234L;
        RecipeInformationResponse mockRecipeInformationResponse = new RecipeInformationResponse();
        mockRecipeInformationResponse.setId(id);
        mockRecipeInformationResponse.setTitle("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs");
        mockRecipeInformationResponse.setImage("https://img.spoonacular.com/recipes/716429-556x370.jpg");
        mockRecipeInformationResponse.setImageType("jpg");

        when(spoonacularClient.findRecipeInformationById(id)).thenReturn(mockRecipeInformationResponse);

        Optional<RecipeInformation> result = externalRecipeRepository.findRecipeById(id);

        RecipeInformation expectedRecipeInformation = RecipeInformationMapper.toRecipeInformationModel(mockRecipeInformationResponse);
        assertTrue(result.isPresent());
        assertEquals(expectedRecipeInformation, result.get());
    }

    @Test
    void whenFindRecipeById_ShouldReturnEmptyResult_WhenRecipeDoesNotExist() {
        Long id = 1234L;

        when(spoonacularClient.findRecipeInformationById(id)).thenThrow(new WebClientResponseException(
                "not found", HttpStatus.NOT_FOUND.value(), "not found", null, null, null));

        Optional<RecipeInformation> result = externalRecipeRepository.findRecipeById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenFindNutritionInformation_thenReturnNutrition() {
        NutritionResponse mockNutritionResponse = getMockNutritionResponse();
        when(spoonacularClient.findRecipeNutritionInformation(1L)).thenReturn(mockNutritionResponse);

        Optional<Nutrition> result = externalRecipeRepository.findNutritionInformation(1L);

        Nutrition expectedNutrition = NutritionMapper.toNutritionModel(mockNutritionResponse);
        assertTrue(result.isPresent());
        assertEquals(expectedNutrition, result.get());
    }

    @Test
    void whenFindNutritionInformation_ShouldReturnEmptyResult_WhenRecipeDoesNotExist() {
        Long id = 1234L;

        when(spoonacularClient.findRecipeNutritionInformation(id)).thenThrow(new WebClientResponseException(
                "not found", HttpStatus.NOT_FOUND.value(), "not found", null, null, null));

        Optional<Nutrition> result = externalRecipeRepository.findNutritionInformation(id);

        assertTrue(result.isEmpty());
    }

    private NutritionResponse getMockNutritionResponse() {

        double totalCalories = 130.0;
        double pastaCalories = 60.0;
        double cheeseCalories = 90.0;

        IngredientResponse pasta = createIngredient("pasta", pastaCalories, 20.0);
        IngredientResponse cheese = createIngredient("cheese", cheeseCalories, 30.0);

        return NutritionResponse.builder().ingredients(Arrays.asList(pasta, cheese)).nutrients(
                Arrays.asList(NutrientResponse.builder().name("Calories").amount(totalCalories).build(),
                        NutrientResponse.builder().name("Protein").amount(30.0).build())).build();
    }

    public IngredientResponse createIngredient(String name, double calories, double protein) {
        return IngredientResponse.builder().name(name).nutrients(
                Arrays.asList(NutrientResponse.builder().name("Calories").amount(calories).build(),
                        NutrientResponse.builder().name("Protein").amount(protein).build())).build();
    }
}

