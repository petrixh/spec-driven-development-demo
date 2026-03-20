package com.example.specdriven.web;

import java.nio.file.Path;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String postersLocation = Path.of("posters").toAbsolutePath().toUri().toString();

        // BR-03: Cache-Control: no-cache + ETag — browser caches but revalidates;
        // server returns 304 Not Modified if the file hasn't changed.
        registry.addResourceHandler("/api/posters/**")
                .addResourceLocations(postersLocation)
                .setCacheControl(CacheControl.noCache());
    }

    /** Generates ETag (MD5 of body) and returns 304 if If-None-Match matches. */
    @Bean
    FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ShallowEtagHeaderFilter());
        bean.addUrlPatterns("/api/posters/*");
        bean.setName("etagFilter");
        return bean;
    }
}
