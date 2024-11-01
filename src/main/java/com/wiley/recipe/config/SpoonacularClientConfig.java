package com.wiley.recipe.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
public class SpoonacularClientConfig {

    private static final String API_KEY_HEADER = "x-api-key";
    private SpoonacularClientProperties spoonacularClientProperties;

    @Bean
    public WebClient SpoonacularWebClient() {
        return WebClient.builder()
                .baseUrl(spoonacularClientProperties.getApi().getBaseUrl())
                .defaultHeader(API_KEY_HEADER, spoonacularClientProperties.getApi().getKey())
                .build();
    }
}
