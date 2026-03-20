package com.example.specdriven.admin.ui;

import jakarta.annotation.security.RolesAllowed;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.router.RouterLink;

@RolesAllowed("ADMIN")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        H2 title = new H2("CineMax Admin");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "var(--lumo-header-text-color)");

        RouterLink dashboard = new RouterLink("Dashboard", AdminDashboardView.class);
        RouterLink movies = new RouterLink("Movies", MovieAdminView.class);
        dashboard.getStyle().set("font-weight", "600");
        dashboard.getStyle().set("color", "var(--lumo-body-text-color)");
        movies.getStyle().set("font-weight", "600");
        movies.getStyle().set("color", "var(--lumo-body-text-color)");

        Span loggedIn = new Span("Logged in as admin");
        loggedIn.getStyle().set("color", "var(--lumo-secondary-text-color)");

        HorizontalLayout links = new HorizontalLayout(dashboard, movies, loggedIn);
        links.setAlignItems(Alignment.CENTER);
        links.setJustifyContentMode(JustifyContentMode.BETWEEN);
        links.setWidthFull();
        links.getStyle().set("color", "var(--lumo-body-text-color)");

        Header header = new Header(title);
        header.getStyle().set("padding", "var(--vaadin-space-m) 0");
        header.getStyle().set("background", "var(--lumo-base-color)");

        Nav nav = new Nav(links);
        nav.getStyle().set("padding-bottom", "var(--vaadin-space-m)");
        nav.getStyle().set("background", "var(--lumo-base-color)");

        VerticalLayout container = new VerticalLayout(header, nav);
        container.setPadding(false);
        container.setSpacing(false);
        container.setWidthFull();
        container.getStyle().set("background", "var(--lumo-base-color)");
        container.getStyle().set("color", "var(--lumo-body-text-color)");

        addToNavbar(container);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs(
                """
                requestAnimationFrame(() => {
                  document.documentElement.style.background = 'var(--lumo-base-color)';
                  document.body.style.background = 'var(--lumo-base-color)';
                  document.body.style.color = 'var(--lumo-body-text-color)';
                  document.body.style.minHeight = '100vh';
                  this.style.background = 'var(--lumo-base-color)';
                  this.style.color = 'var(--lumo-body-text-color)';
                  const drawer = this.shadowRoot?.querySelector('[part="drawer"]');
                  const navbar = this.shadowRoot?.querySelector('[part="navbar"]');
                  const content = this.shadowRoot?.querySelector('[content]');
                  [drawer, navbar, content].forEach((part) => {
                    if (part) {
                      part.style.background = 'var(--lumo-base-color)';
                      part.style.color = 'var(--lumo-body-text-color)';
                      part.style.minHeight = '100vh';
                    }
                  });
                });
                """);
    }
}
