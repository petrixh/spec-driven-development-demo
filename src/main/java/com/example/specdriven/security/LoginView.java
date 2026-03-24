package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | re:solve")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginOverlay loginOverlay = new LoginOverlay();
    private final Div credentialsPanel = new Div();

    public LoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        loginOverlay.setAction("login");
        loginOverlay.setTitle("re:solve");
        loginOverlay.setDescription("Help desk, simplified.");
        loginOverlay.setOpened(true);

        loginOverlay.addForgotPasswordListener(event -> {
            credentialsPanel.setVisible(!credentialsPanel.isVisible());
        });

        credentialsPanel.setVisible(false);
        credentialsPanel.getStyle()
                .set("background", "var(--vaadin-primary-color-50, #EFF6FF)")
                .set("padding", "var(--vaadin-space-m, 1rem)")
                .set("border-radius", "var(--vaadin-radius-m, 8px)")
                .set("max-width", "400px")
                .set("margin", "var(--vaadin-space-m, 1rem) auto");

        H3 credentialsTitle = new H3("Demo Credentials");
        credentialsTitle.getStyle().set("margin-top", "0");

        Paragraph customer = new Paragraph("Customer: customer@test.com / customer@test.com");
        Paragraph agent = new Paragraph("Agent: agent@test.com / agent@test.com");
        Paragraph manager = new Paragraph("Manager: manager@test.com / manager@test.com");

        credentialsPanel.add(credentialsTitle, customer, agent, manager);

        add(loginOverlay, credentialsPanel);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginOverlay.setError(true);
        }
    }
}
