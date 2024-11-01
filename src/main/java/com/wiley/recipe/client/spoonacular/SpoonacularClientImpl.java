package com.wiley.recipe.client.spoonacular;

import com.wiley.recipe.client.spoonacular.dto.NutritionResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeInformationResponse;
import com.wiley.recipe.client.spoonacular.dto.RecipeSearchResponse;
import com.wiley.recipe.config.SpoonacularClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class SpoonacularClientImpl implements SpoonacularClient {
    private final SpoonacularClientProperties spoonacularClientProperties;
    private final WebClient spoonacularWebClient;

    private static final String QUERY_PARAM = "query";
    private static final String NUMBER_PARAM = "number";
    private static final String OFFSET_PARAM = "offset";
    private static final String ADD_RECIPE_INFORMATION = "addRecipeInformation";
    private static final String INCLUDE_NUTRITION_PARAM = "includeNutrition";

    public SpoonacularClientImpl(SpoonacularClientProperties spoonacularClientProperties, WebClient spoonacularWebClient) {
        this.spoonacularClientProperties = spoonacularClientProperties;
        this.spoonacularWebClient = spoonacularWebClient;
    }

    @Override
    public RecipeSearchResponse searchRecipes(String query, int pageNumber, int size) {
        int offset = size * (pageNumber == 0 ? pageNumber : pageNumber - 1);
        return spoonacularWebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(spoonacularClientProperties.getRecipes().getSearchPath())
                                .queryParam(QUERY_PARAM, query)
                                .queryParam(NUMBER_PARAM, size)
                                .queryParam(ADD_RECIPE_INFORMATION, true)
                                .queryParam(OFFSET_PARAM, offset)
                                .build())
                .retrieve()
                .bodyToMono(RecipeSearchResponse.class)
                .doOnError(error -> log.error("Error while calling get search recipes: {}", error.getMessage()))
                .blockOptional()
                .orElse(null);
    }

    @Override
    public RecipeInformationResponse findRecipeInformationById(Long id) {
        return spoonacularWebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(spoonacularClientProperties.getRecipes().getInformationPath())
                                .queryParam(INCLUDE_NUTRITION_PARAM, true)
                                .build(id))
                .retrieve()
                .bodyToMono(RecipeInformationResponse.class)
                .doOnError(error -> log.error("Error while calling get recipe by id: {}", id, error))
                .blockOptional()
                .orElse(null);
    }

    @Override
    public NutritionResponse findRecipeNutritionInformation(Long id) {
        return spoonacularWebClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(spoonacularClientProperties.getRecipes().getNutritionPath())
                                .build(id))
                .retrieve()
                .bodyToMono(NutritionResponse.class)
                .doOnError(error -> log.error("Error while calling get nutrition info for recipe id: {}", id, error))
                .blockOptional()
                .orElse(null);
    }
}
