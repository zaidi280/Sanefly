package com.backend.sanfely.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI saneflyOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sanefly API")
                .description("Backend API for Sanefly - Tunisian traiteur marketplace")
                .version("v1"));
    }
}