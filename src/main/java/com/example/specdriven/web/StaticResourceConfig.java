package com.example.specdriven.web;

import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String postersLocation = Path.of("posters").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/posters/**")
                .addResourceLocations(postersLocation);
    }
}
