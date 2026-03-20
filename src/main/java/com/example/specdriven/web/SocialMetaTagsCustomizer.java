package com.example.specdriven.web;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.communication.IndexHtmlResponse;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * Injects Open Graph and Twitter Card meta tags into every server-rendered
 * HTML page so that social-media crawlers can build rich link previews.
 */
@Component
public class SocialMetaTagsCustomizer implements VaadinServiceInitListener {

    private static final String SITE_URL = "https://cinemax.example.com";
    private static final String OG_IMAGE_URL = SITE_URL + "/og-image.png";

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addIndexHtmlRequestListener(this::addSocialMetaTags);
    }

    private void addSocialMetaTags(IndexHtmlResponse response) {
        Element head = response.getDocument().head();

        // Open Graph tags
        addMeta(head, "property", "og:type", "website");
        addMeta(head, "property", "og:url", SITE_URL);
        addMeta(head, "property", "og:title", "CineMax");
        addMeta(head, "property", "og:description",
                "Browse movies, pick your seats, and buy tickets at CineMax cinema");
        addMeta(head, "property", "og:image", OG_IMAGE_URL);
        addMeta(head, "property", "og:image:width", "1200");
        addMeta(head, "property", "og:image:height", "630");

        // Twitter Card tags
        addMeta(head, "name", "twitter:card", "summary_large_image");
        addMeta(head, "name", "twitter:title", "CineMax");
        addMeta(head, "name", "twitter:description",
                "Browse movies, pick your seats, and buy tickets at CineMax cinema");
        addMeta(head, "name", "twitter:image", OG_IMAGE_URL);
    }

    private static void addMeta(Element head, String attrKey, String attrValue, String content) {
        head.appendElement("meta")
                .attr(attrKey, attrValue)
                .attr("content", content);
    }
}
