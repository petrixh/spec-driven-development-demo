package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login — TickTask")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private Div credentialsHint;

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");
        login.addForgotPasswordListener(e -> {
            if (credentialsHint == null) {
                credentialsHint = new Div();
                credentialsHint.getStyle()
                        .set("background", "rgba(255, 255, 255, 0.1)")
                        .set("border-radius", "var(--vaadin-radius-m)")
                        .set("padding", "var(--vaadin-padding-m)")
                        .set("margin-top", "var(--vaadin-gap-m)")
                        .set("text-align", "center")
                        .set("max-width", "320px");

                Span title = new Span("Demo Credentials");
                title.getStyle()
                        .set("font-weight", "var(--aura-font-weight-semibold)")
                        .set("display", "block")
                        .set("margin-bottom", "var(--vaadin-gap-xs)")
                        .set("color", "#F57C00");

                Span admin = new Span("Admin: admin / admin");
                admin.getStyle().set("display", "block").set("color", "#FFFFFF");

                Span user = new Span("User: user / user");
                user.getStyle().set("display", "block").set("color", "#FFFFFF");

                credentialsHint.add(title, admin, user);
                add(credentialsHint);
            }
        });

        add(new H1("TickTask"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters()
                .getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
