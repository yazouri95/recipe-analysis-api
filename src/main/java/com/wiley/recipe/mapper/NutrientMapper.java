package com.wiley.recipe.mapper;

import com.wiley.recipe.client.spoonacular.dto.NutrientResponse;
import com.wiley.recipe.domain.model.Nutrient;
import com.wiley.recipe.dto.NutrientDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NutrientMapper {

    private NutrientMapper() {

    }

    public static NutrientDTO toNutrientDTO(Nutrient nutrient) {
        return NutrientDTO.builder()
                .name(nutrient.getName())
                .amount(nutrient.getAmount())
                .unit(nutrient.getUnit())
                .percentOfDailyNeeds(nutrient.getPercentOfDailyNeeds())
                .build();
    }

    public static List<NutrientDTO> toNutrientDTOs(List<Nutrient> nutrients) {
        return Optional.ofNullable(nutrients)
                .orElse(new ArrayList<>())
                .stream()
                .map(NutrientMapper::toNutrientDTO)
                .toList();
    }

    public static Nutrient toNutrientModel(NutrientResponse nutrientResponse) {
        return Nutrient.builder()
                .name(nutrientResponse.getName())
                .amount(nutrientResponse.getAmount())
                .unit(nutrientResponse.getUnit())
                .percentOfDailyNeeds(nutrientResponse.getPercentOfDailyNeeds())
                .build();
    }

    public static List<Nutrient> toNutrientModelList(List<NutrientResponse> nutrientResponses) {
        return Optional
                .ofNullable(nutrientResponses)
                .orElse(new ArrayList<>())
                .stream()
                .map(NutrientMapper::toNutrientModel)
                .toList();
    }


}
