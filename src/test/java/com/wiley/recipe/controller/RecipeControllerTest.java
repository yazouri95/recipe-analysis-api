package com.wiley.recipe.controller;

import com.wiley.recipe.constants.PathConstants;
import com.wiley.recipe.domain.exception.RecipeNotFoundException;
import com.wiley.recipe.domain.exception.SpoonacularClientException;
import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeCaloriesInfo;
import com.wiley.recipe.domain.model.RecipeInformation;
import com.wiley.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    private static final String SEARCH_RECIPES_ENDPOINT = PathConstants.API_RECIPES + PathConstants.SEARCH_RECIPES;

    @Test
    void whenValidSearchRequest_thenReturnRecipes() throws Exception {
        Page<Recipe> mockPage = new Page<>(Collections.singletonList(Recipe.builder().build()), 1, 0, 10);
        when(recipeService.seacrhRecipes("pasta", 0, 10)).thenReturn(mockPage);

        mockMvc.perform(get(SEARCH_RECIPES_ENDPOINT)
                        .param("query", "pasta")
                        .param("pageNumber", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(recipeService).seacrhRecipes("pasta", 0, 10);
    }

    @Test
    void whenSearchRequestMissingQueryParam_thenBadRequest() throws Exception {
        mockMvc.perform(get(SEARCH_RECIPES_ENDPOINT)
                        .param("pageSize", "10")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenValidGetRecipeInformation_thenReturnRecipeInfo() throws Exception {
        long recipeId = 1;
        RecipeInformation mockRecipeInformation = RecipeInformation.builder().build();
        when(recipeService.findRecipeById(recipeId)).thenReturn(mockRecipeInformation);

        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "info");

        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(recipeService).findRecipeById(recipeId);
    }

    @Test
    void whenGetRecipeInformationNotFound_thenNotFound() throws Exception {
        long recipeId = 99;

        when(recipeService.findRecipeById(recipeId)).thenThrow(new RecipeNotFoundException(recipeId));

        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "info");

        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRecipeInformationUnauthorizedAccessToSpoonacular_thenInternalServerError() throws Exception {
        long recipeId = 99;

        when(recipeService.findRecipeById(recipeId)).thenThrow(new SpoonacularClientException("Unauthorized access to Spoonacular API"));

        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "info");

        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenValidGetRecipeCaloriesInfo_thenReturnCaloriesInfo() throws Exception {
        long recipeId = 1;
        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "calories");
        RecipeCaloriesInfo mockCaloriesInfo = RecipeCaloriesInfo.builder().build();
        when(recipeService.findRecipeCaloriesInfo(recipeId, List.of(123L))).thenReturn(mockCaloriesInfo);

        mockMvc.perform(get(endpoint)
                        .param("excludedIngredientIds", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(recipeService).findRecipeCaloriesInfo(recipeId, List.of(123L));
    }

    @Test
    void whenGetRecipeCaloriesInfoNotFound_thenNotFound() throws Exception {
        long recipeId = 99;
        when(recipeService.findRecipeCaloriesInfo(recipeId, List.of(123L))).thenThrow(new RecipeNotFoundException(recipeId));
        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "calories");

        mockMvc.perform(get(endpoint)
                        .param("excludedIngredientIds", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRecipeCaloriesInfoUnauthorizedAccessToSpoonacular_thenInternalServerError() throws Exception {
        long recipeId = 99;

        when(recipeService.findRecipeCaloriesInfo(recipeId, List.of(123L))).thenThrow(new SpoonacularClientException("Unauthorized access to Spoonacular API"));
        String endpoint = String.format("%s/%d/%s", PathConstants.API_RECIPES, recipeId, "calories");

        mockMvc.perform(get(endpoint)
                        .param("excludedIngredientIds", "123L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
