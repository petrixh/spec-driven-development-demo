package com.example.specdriven;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.communication.IndexHtmlResponse;

@Component
public class SocialMetaTagsInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addIndexHtmlRequestListener(this::applySocialMetaTags);
    }

    private void applySocialMetaTags(IndexHtmlResponse response) {
        Document document = response.getDocument();
        Element head = document.head();
        String scheme = response.getVaadinRequest().isSecure() ? "https" : "http";
        String host = response.getVaadinRequest().getHeader("host");
        String contextPath = response.getVaadinRequest().getContextPath();
        String path = response.getVaadinRequest().getPathInfo() == null ? "/" : response.getVaadinRequest().getPathInfo();
        String origin = scheme + "://" + host + contextPath;
        String requestUrl = origin + path;
        String imageUrl = origin + Application.OG_IMAGE_PATH;

        addPropertyMetaTag(head, "og:title", Application.SITE_NAME);
        addPropertyMetaTag(head, "og:description", Application.SITE_DESCRIPTION);
        addPropertyMetaTag(head, "og:image", imageUrl);
        addPropertyMetaTag(head, "og:url", requestUrl);
        addPropertyMetaTag(head, "og:type", "website");

        addNameMetaTag(head, "twitter:image", imageUrl);
    }

    private void addPropertyMetaTag(Element head, String property, String content) {
        head.select("meta[property='" + property + "']").remove();
        head.appendElement("meta")
                .attr("property", property)
                .attr("content", content);
    }

    private void addNameMetaTag(Element head, String name, String content) {
        head.select("meta[name='" + name + "']").remove();
        head.appendElement("meta")
                .attr("name", name)
                .attr("content", content);
    }
}
