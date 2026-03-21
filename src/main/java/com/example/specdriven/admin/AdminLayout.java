package com.example.specdriven.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class AdminLayout extends VerticalLayout implements RouterLayout {

    public AdminLayout() {
        setSizeFull();
        setPadding(true);
        setSpacing(false);

        // Header bar with nav and logout
        H1 title = new H1("BuyBuy Admin");
        title.getStyle().set("font-size", "var(--aura-font-size-l)");
        title.getStyle().set("margin", "0");

        RouterLink productsLink = new RouterLink("Products", ProductAdminView.class);
        productsLink.addClassName("admin-nav-link");

        Span navSpacer = new Span();
        navSpacer.getStyle().set("flex-grow", "1");

        Button logoutButton = new Button("Logout", e ->
                UI.getCurrent().getPage().setLocation("/logout"));
        logoutButton.getStyle().set("color", "var(--aura-red)");

        HorizontalLayout header = new HorizontalLayout(title, productsLink, navSpacer, logoutButton);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("gap", "var(--vaadin-gap-l)");
        header.getStyle().set("padding-bottom", "var(--vaadin-padding-m)");
        header.getStyle().set("border-bottom", "1px solid var(--vaadin-border-color-secondary)");
        header.getStyle().set("margin-bottom", "var(--vaadin-gap-m)");

        add(header);
    }
}
