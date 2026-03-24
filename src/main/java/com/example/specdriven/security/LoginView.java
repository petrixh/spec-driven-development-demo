package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login — Forge")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private final Div credentialsHint = new Div();

    public LoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(true);
        loginForm.addForgotPasswordListener(e -> {
            credentialsHint.setVisible(true);
        });

        credentialsHint.setVisible(false);
        credentialsHint.getStyle()
                .set("background", "var(--vaadin-primary-color-10pct)")
                .set("padding", "var(--vaadin-padding-m)")
                .set("border-radius", "var(--vaadin-radius-m)")
                .set("max-width", "320px")
                .set("text-align", "center");
        credentialsHint.add(
                new Paragraph("Demo credentials:"),
                new Paragraph("admin / admin (full access)"),
                new Paragraph("user / user (view only)")
        );

        add(loginForm, credentialsHint);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
