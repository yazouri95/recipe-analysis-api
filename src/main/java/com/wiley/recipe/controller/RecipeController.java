package com.wiley.recipe.controller;

import com.wiley.recipe.domain.model.Page;
import com.wiley.recipe.domain.model.Recipe;
import com.wiley.recipe.domain.model.RecipeCaloriesInfo;
import com.wiley.recipe.domain.model.RecipeInformation;
import com.wiley.recipe.service.RecipeService;
import com.wiley.recipe.dto.PageDTO;
import com.wiley.recipe.dto.RecipeCaloriesInfoDTO;
import com.wiley.recipe.dto.RecipeDTO;
import com.wiley.recipe.dto.RecipeInformationDTO;
import com.wiley.recipe.mapper.RecipeCaloriesInfoMapper;
import com.wiley.recipe.mapper.RecipeInformationMapper;
import com.wiley.recipe.mapper.RecipeMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wiley.recipe.constants.PathConstants.*;

@RestController
@RequestMapping(API_RECIPES)
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(SEARCH_RECIPES)
    public ResponseEntity<PageDTO<RecipeDTO>> searchRecipes(@RequestParam @NotBlank @Size(min = 3, max = 50) String query,
                                                            @RequestParam @Min(0) int pageNumber,
                                                            @RequestParam @Min(5) int size) {
        Page<Recipe> recipes = recipeService.seacrhRecipes(query, pageNumber, size);
        List<RecipeDTO> recipeDTOs = RecipeMapper.toRecipeDTOs(recipes.getResults());

        return ResponseEntity.ok(new PageDTO<>(recipeDTOs, recipes.getTotalElements(), pageNumber, size));
    }

    @GetMapping(GET_RECIPE_INFO)
    public ResponseEntity<RecipeInformationDTO> getRecipeInformation(@PathVariable Long id) {
        RecipeInformation recipeInformation = recipeService.findRecipeById(id);
        RecipeInformationDTO recipeInformationDTO = RecipeInformationMapper.toRecipeInformationDTO(recipeInformation);
        return ResponseEntity.ok(recipeInformationDTO);
    }

    @GetMapping(GET_RECIPE_CALORIES_INFO)
    public ResponseEntity<RecipeCaloriesInfoDTO> getRecipeCaloriesInfo(@PathVariable Long id, @RequestParam(required = false) List<Long> excludedIngredientIds) {
        final RecipeCaloriesInfo recipeCaloriesInfo = recipeService.findRecipeCaloriesInfo(id, Optional.ofNullable(excludedIngredientIds).orElse(new ArrayList<>()));
        final RecipeCaloriesInfoDTO recipeCaloriesInfoDTO = RecipeCaloriesInfoMapper.toRecipeCaloriesInfoDTO(recipeCaloriesInfo);
        return ResponseEntity.ok(recipeCaloriesInfoDTO);
    }
}
