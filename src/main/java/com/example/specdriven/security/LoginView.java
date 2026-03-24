package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login — Stash.log")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final Div credentialsHint = new Div();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        Image logo = new Image("images/logo.svg", "Stash.log");
        logo.setWidth("220px");

        login.setAction("login");
        login.setForgotPasswordButtonVisible(true);
        login.addForgotPasswordListener(e -> credentialsHint.setVisible(true));

        credentialsHint.addClassName("credentials-hint");
        credentialsHint.setVisible(false);

        Paragraph title = new Paragraph("Demo Credentials");
        title.getStyle().set("font-weight", "var(--aura-font-weight-semibold)");
        title.getStyle().set("margin", "0 0 var(--vaadin-gap-xs) 0");

        Div adminCred = new Div(new Span("Manager: "), createCode("admin"), new Span(" / "), createCode("admin"));
        Div staffCred = new Div(new Span("Staff: "), createCode("staff"), new Span(" / "), createCode("staff"));

        credentialsHint.add(title, adminCred, staffCred);

        add(logo, login, credentialsHint);
    }

    private Span createCode(String text) {
        Span code = new Span(text);
        code.getStyle().set("font-family", "monospace");
        code.getStyle().set("background", "var(--vaadin-background-container)");
        code.getStyle().set("padding", "2px 6px");
        code.getStyle().set("border-radius", "var(--vaadin-radius-s)");
        return code;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
