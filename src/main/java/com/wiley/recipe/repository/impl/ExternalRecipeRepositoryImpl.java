package com.wiley.recipe.repository.impl;

import com.wiley.recipe.client.spoonacular.SpoonacularClient;
import com.wiley.recipe.domain.exception.SpoonacualrUnauthorizedException;
import com.wiley.recipe.domain.exception.SpoonacularClientException;
import com.wiley.recipe.domain.model.Nutrition;
import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeInformation;
import com.wiley.recipe.client.spoonacular.dto.NutritionResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeInformationResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeSearchResponse;
import com.wiley.recipe.mapper.NutritionMapper;
import com.wiley.recipe.mapper.RecipeInformationMapper;
import com.wiley.recipe.mapper.RecipeMapper;
import com.wiley.recipe.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ExternalRecipeRepositoryImpl implements RecipeRepository {
    private final SpoonacularClient spoonacularClient;

    public ExternalRecipeRepositoryImpl(SpoonacularClient spoonacularClient) {
        this.spoonacularClient = spoonacularClient;
    }

    @Override
    public Page<Recipe> search(String query, int pageNumber, int size) {
        try {
            final RecipeSearchResponse recipeSearchResponse = spoonacularClient.searchRecipes(query, pageNumber, size);
            List<Recipe> recipes = RecipeMapper.toRecipeModelList(recipeSearchResponse.getResults());
            return new Page<>(recipes, recipeSearchResponse.getTotalResults(), pageNumber, size);
        } catch (WebClientResponseException ex) {
            return handleWebClientExceptionForPage(ex);
        }
    }

    @Override
    public Optional<RecipeInformation> findRecipeById(Long id) {
        try {
            RecipeInformationResponse recipeInformationResponse = spoonacularClient.findRecipeInformationById(id);
            RecipeInformation recipeInformation = RecipeInformationMapper.toRecipeInformationModel(recipeInformationResponse);
            return Optional.of(recipeInformation);
        } catch (WebClientResponseException ex) {
            return handleWebClientExceptionForOptional(ex);
        }

    }

    @Override
    public Optional<Nutrition> findNutritionInformation(Long recipeId) {
        try {
            NutritionResponse nutritionResponse = spoonacularClient.findRecipeNutritionInformation(recipeId);
            Nutrition nutrition = NutritionMapper.toNutritionModel(nutritionResponse);
            return Optional.ofNullable(nutrition);
        } catch (WebClientResponseException ex) {
            return handleWebClientExceptionForOptional(ex);
        }
    }

    private Page<Recipe> handleWebClientExceptionForPage(WebClientResponseException ex) {
        handleWebClientExceptionForOptional(ex);
        return new Page<>(new ArrayList<>(), 0, 0, 10);
    }

    private <T> Optional<T> handleWebClientExceptionForOptional(WebClientResponseException ex) {
        if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            return Optional.empty();
        } else if (ex.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
            throw new SpoonacualrUnauthorizedException();
        }
        throw new SpoonacularClientException("Client error occurred");
    }
}
