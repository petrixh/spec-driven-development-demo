package com.example.specdriven;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        // Twitter Card meta tags (use name attribute)
        settings.addMetaTag("twitter:card", "summary_large_image");
        settings.addMetaTag("twitter:title", "CineMax");
        settings.addMetaTag("twitter:description",
                "Browse movies, pick your seats, and buy tickets at CineMax cinema");
        settings.addMetaTag("twitter:image", "/images/og-image.png");

        // Open Graph meta tags (use property attribute, injected as inline HTML)
        settings.addInlineWithContents(
                "<meta property=\"og:title\" content=\"CineMax\">\n"
                + "<meta property=\"og:description\" content=\"Browse movies, pick your seats, and buy tickets at CineMax cinema\">\n"
                + "<meta property=\"og:image\" content=\"/images/og-image.png\">\n"
                + "<meta property=\"og:url\" content=\"/\">\n"
                + "<meta property=\"og:type\" content=\"website\">\n",
                Inline.Wrapping.NONE);
    }
}
