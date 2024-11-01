package com.wiley.recipe.client;

import com.wiley.recipe.client.spoonacular.SpoonacularClientImpl;
import com.wiley.recipe.client.spoonacular.dto.*;
import com.wiley.recipe.config.SpoonacularClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpoonacularClientImplTest {

    private static final String QUERY = "pasta";
    private static final int PAGE_SIZE = 10;
    private static final int PAGE = 1;

    @InjectMocks
    private SpoonacularClientImpl spoonacularClient;

    @Mock
    private SpoonacularClientProperties properties;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        // Init the service mock
        MockitoAnnotations.openMocks(this);

        // Set up the WebClient mock
        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
    }

    @Test
    void whenSearchRecipes_thenReturnsRecipeSearchResponse() {
        // Given
        RecipeSearchResponse mockRecipeSearchResponse = new RecipeSearchResponse();
        RecipeResponse mockRecipeResponse = new RecipeResponse();
        mockRecipeResponse.setId(1234L);
        mockRecipeResponse.setTitle("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs");
        mockRecipeResponse.setImage("https://img.spoonacular.com/recipes/716429-312x231.jpg");
        mockRecipeResponse.setImageType("jpg");

        mockRecipeSearchResponse.setResults(singletonList(mockRecipeResponse));
        mockRecipeSearchResponse.setTotalResults(1);
        mockRecipeSearchResponse.setOffset(0);
        mockRecipeSearchResponse.setNumber(1);


        //When
        when(responseSpecMock.bodyToMono(RecipeSearchResponse.class)).thenReturn(Mono.just(mockRecipeSearchResponse));

        // Then
        RecipeSearchResponse recipeSearchResponse = spoonacularClient.searchRecipes(QUERY, PAGE_SIZE, PAGE);

        verify(webClient, times(1)).get();

        assertThat(recipeSearchResponse).isNotNull();
        assertThat(recipeSearchResponse.getResults()).hasSize(1);
        assertThat(recipeSearchResponse.getResults().get(0)).isEqualTo(mockRecipeResponse);
        assertThat(recipeSearchResponse.getResults().get(0)).isEqualTo(mockRecipeResponse);

        RecipeResponse recipeResponse = recipeSearchResponse.getResults().get(0);
        assertThat(recipeResponse.getId()).isEqualTo(mockRecipeResponse.getId());
        assertThat(recipeResponse.getTitle()).isEqualTo(mockRecipeResponse.getTitle());
        assertThat(recipeResponse.getImage()).isEqualTo(mockRecipeResponse.getImage());
        assertThat(recipeResponse.getImageType()).isEqualTo(mockRecipeResponse.getImageType());
    }

    @Test
    void whenSearchRecipes_thenReturnsEmptyResponse() {
        // Given
        RecipeSearchResponse mockRecipeSearchResponse = new RecipeSearchResponse();
        mockRecipeSearchResponse.setResults(emptyList());
        mockRecipeSearchResponse.setTotalResults(0);
        mockRecipeSearchResponse.setOffset(0);
        mockRecipeSearchResponse.setNumber(10);

        // When
        when(responseSpecMock.bodyToMono(RecipeSearchResponse.class)).thenReturn(Mono.just(mockRecipeSearchResponse));
        RecipeSearchResponse recipeSearchResponse = spoonacularClient.searchRecipes(QUERY, PAGE_SIZE, PAGE);

        // Then
        verify(webClient, times(1)).get();

        assertThat(recipeSearchResponse).isNotNull();
        assertThat(recipeSearchResponse.getResults()).isEmpty();
        assertThat(recipeSearchResponse.getTotalResults()).isZero();
    }

    @Test
    void whenSearchRecipes_thenReturnsUnauthorizedException() {
        // When
        when(webClient.get().uri(any(Function.class)))
                .thenThrow(new WebClientResponseException(
                        "Unauthorized", HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null, null, null));

        // Then
        verify(webClient, times(1)).get();

        WebClientResponseException ex = assertThrows(WebClientResponseException.class, () ->
                spoonacularClient.searchRecipes(QUERY, PAGE_SIZE, PAGE));

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenFindRecipeInformationById_thenReturnsRecipeInformationResponse() {
        // Given
        Long id = 1234L;
        RecipeInformationResponse mockRecipeInformationResponse = new RecipeInformationResponse();
        mockRecipeInformationResponse.setId(id);
        mockRecipeInformationResponse.setTitle("Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs");
        mockRecipeInformationResponse.setImage("https://img.spoonacular.com/recipes/716429-556x370.jpg");
        mockRecipeInformationResponse.setImageType("jpg");


        // When
        when(responseSpecMock.bodyToMono(RecipeInformationResponse.class)).thenReturn(Mono.just(mockRecipeInformationResponse));

        // Then
        RecipeInformationResponse recipeInformationResponse = spoonacularClient.findRecipeInformationById(id);
        verify(webClient, times(1)).get();
        assertThat(recipeInformationResponse).isEqualTo(mockRecipeInformationResponse);
    }

    @Test
    void whenFindRecipeInformationById_thenReturnsNotFoundException() {
        // When
        when(webClient.get().uri(any(Function.class)))
                .thenThrow(new WebClientResponseException(
                        "not found", HttpStatus.NOT_FOUND.value(), "not found", null, null, null));

        // Then
        WebClientResponseException ex = assertThrows(WebClientResponseException.class, () ->
                spoonacularClient.findRecipeInformationById(1L));

        verify(webClient, times(2)).get();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenFindRecipeNutritionInformation_thenReturnsRecipeInformationResponse() {
        // Given
        Long id = 1234L;
        NutritionResponse mockResponse = getMockNutritionResponse();
        when(responseSpecMock.bodyToMono(NutritionResponse.class)).thenReturn(
                Mono.just(mockResponse));

        // When
        NutritionResponse
                response =
                spoonacularClient.findRecipeNutritionInformation(id);

        // Then
        verify(webClient, times(1)).get();
        assertThat(response).isEqualTo(mockResponse);

    }

    @Test
    void whenFindRecipeNutritionInformation_thenReturnsNotFoundException() {
        // When
        when(webClient.get().uri(any(Function.class)))
                .thenThrow(new WebClientResponseException(
                        "not found", HttpStatus.NOT_FOUND.value(), "not found", null, null, null));

        // Then
        WebClientResponseException ex = assertThrows(WebClientResponseException.class, () ->
                spoonacularClient.findRecipeNutritionInformation(1L));

        verify(webClient, times(2)).get();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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
