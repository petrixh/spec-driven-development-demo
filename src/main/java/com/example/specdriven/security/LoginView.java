package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login — Triage")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private final Div credentialsHint = new Div();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div brand = new Div();
        brand.addClassName("login-brand");

        Span logoText = new Span();
        logoText.addClassName("login-logo-text");
        Span tri = new Span("Tri");
        Span age = new Span("age");
        age.addClassName("login-logo-accent");
        logoText.add(tri, age);

        Span subtitle = new Span("Patient Management");
        subtitle.addClassName("login-subtitle");

        Div subtitleDiv = new Div(subtitle);
        brand.add(logoText, subtitleDiv);

        loginForm.setAction("login");

        credentialsHint.addClassName("credentials-hint");
        credentialsHint.setVisible(false);
        credentialsHint.setText("Demo login — Username: admin, Password: admin");

        loginForm.addForgotPasswordListener(e -> credentialsHint.setVisible(true));

        add(brand, loginForm, credentialsHint);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
