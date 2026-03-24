package com.example.specdriven.security;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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
@PageTitle("Login — Triage")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private Div hintContainer;

    public LoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getStyle().set("background-color", "#F7F9FB");

        H1 title = new H1();
        Span tri = new Span("Tri");
        tri.getStyle().set("color", "#2C3E50");
        Span age = new Span("age");
        age.getStyle().set("color", "#4A90B8");
        title.add(tri, age);
        title.getStyle()
                .set("font-family", "system-ui, -apple-system, 'Segoe UI', sans-serif")
                .set("font-weight", "600");

        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(true);

        hintContainer = new Div();
        hintContainer.setVisible(false);
        hintContainer.getStyle()
                .set("background-color", "#EBF3F8")
                .set("border", "1px solid #D6E0E8")
                .set("border-radius", "var(--vaadin-radius-m)")
                .set("padding", "var(--vaadin-padding-m)")
                .set("max-width", "360px")
                .set("text-align", "center");
        Paragraph hintText = new Paragraph("Demo credentials: username admin, password admin");
        hintText.getStyle().set("color", "#2C3E50").set("margin", "0");
        hintContainer.add(hintText);

        loginForm.addForgotPasswordListener(e -> hintContainer.setVisible(true));

        add(title, loginForm, hintContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
