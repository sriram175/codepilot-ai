package com.sriram.ai.codepilot_ai.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Value("${app.frontend.url}")
            private String frontendUrl;
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
