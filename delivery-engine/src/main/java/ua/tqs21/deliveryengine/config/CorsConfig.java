package ua.tqs21.deliveryengine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // Paths that allow cross domain access
        .allowedOrigins("*")    // Sources that allow cross domain access
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")    // Allow request method
        .maxAge(168000)    // Pre inspection interval
        .allowedHeaders("*")  // Allow head setting
        .allowCredentials(false);
    }
}
