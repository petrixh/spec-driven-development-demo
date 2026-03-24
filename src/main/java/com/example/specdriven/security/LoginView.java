package com.example.specdriven.security;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
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

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");
        login.addForgotPasswordListener(e -> {
            Notification notification = new Notification();
            Div content = new Div();
            content.getStyle().set("padding", "var(--vaadin-padding-s)");

            Span title = new Span("Demo Credentials");
            title.getStyle().set("font-weight", "var(--aura-font-weight-semibold)")
                    .set("display", "block")
                    .set("margin-bottom", "var(--vaadin-gap-xs)");

            Span admin = new Span("Admin: admin / admin");
            admin.getStyle().set("display", "block");

            Span user = new Span("User: user / user");
            user.getStyle().set("display", "block");

            content.add(title, admin, user);
            notification.add(content);
            notification.setDuration(8000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
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
