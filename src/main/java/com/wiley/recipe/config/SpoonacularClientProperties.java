package com.wiley.recipe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spoonacular")
@Data
public class SpoonacularClientProperties {

    private API api = new API();
    private RecipesProperties recipes = new RecipesProperties();

    @Data
    public static class API {
        private String key;
        private String baseUrl;
    }

    @Data
    public static class RecipesProperties {
        private String searchPath;
        private String informationPath;
        private String nutritionPath;
    }
}
