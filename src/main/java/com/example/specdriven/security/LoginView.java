package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login — GreenLedger")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H1 title = new H1("GreenLedger");
        title.getStyle().set("color", "#2E7D32");

        Paragraph tagline = new Paragraph("Expenses, simplified.");
        tagline.getStyle().set("color", "var(--vaadin-text-color-secondary)");

        loginForm.setAction("login");

        Div credentialsHint = new Div();
        credentialsHint.setVisible(false);
        credentialsHint.getStyle()
                .set("background", "var(--vaadin-background-container)")
                .set("padding", "var(--vaadin-padding-m)")
                .set("border-radius", "var(--vaadin-radius-m)")
                .set("margin-top", "var(--vaadin-gap-s)");

        Paragraph hintTitle = new Paragraph("Demo credentials:");
        hintTitle.getStyle().set("font-weight", "var(--aura-font-weight-semibold)")
                .set("margin", "0 0 var(--vaadin-gap-xs) 0");
        Paragraph employeeCred = new Paragraph("Employee: employee / employee");
        employeeCred.getStyle().set("margin", "0");
        Paragraph managerCred = new Paragraph("Manager: manager / manager");
        managerCred.getStyle().set("margin", "0");

        credentialsHint.add(hintTitle, employeeCred, managerCred);

        loginForm.addForgotPasswordListener(event -> credentialsHint.setVisible(true));

        add(title, tagline, loginForm, credentialsHint);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
