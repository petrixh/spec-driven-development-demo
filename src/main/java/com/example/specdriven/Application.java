package com.example.specdriven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;

@SpringBootApplication
public class Application implements AppShellConfigurator {

    public static final String SITE_NAME = "CineMax";
    public static final String SITE_DESCRIPTION = "Browse movies, pick your seats, and buy tickets at CineMax cinema";
    public static final String OG_IMAGE_PATH = "/cinemax-og.png";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.setPageTitle(SITE_NAME);
        settings.setViewport("width=device-width, initial-scale=1");
        settings.addMetaTag("description", SITE_DESCRIPTION);
        settings.addMetaTag("twitter:card", "summary_large_image");
        settings.addMetaTag("twitter:title", SITE_NAME);
        settings.addMetaTag("twitter:description", SITE_DESCRIPTION);
    }
}
