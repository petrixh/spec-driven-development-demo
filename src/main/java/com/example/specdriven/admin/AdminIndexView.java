package com.example.specdriven.admin;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import jakarta.annotation.security.RolesAllowed;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminIndexView extends VerticalLayout {

    public AdminIndexView() {
        setSpacing(true);
        setPadding(true);
        getStyle().set("color", "#1a1a1a").set("background", "#f5f5f5");

        H1 title = new H1("CineMax Admin");
        Paragraph description = new Paragraph("Manage the cinema catalogue and show schedule.");

        H2 links = new H2("Admin Areas");

        RouterLink moviesLink = new RouterLink("🎬 Manage Movies", MovieAdminView.class);
        RouterLink showsLink = new RouterLink("🗓 Manage Shows", ShowAdminView.class);

        Anchor logoutLink = new Anchor("/logout", "Log out");

        add(title, description, links, moviesLink, showsLink, logoutLink);
    }
}
