package com.example.specdriven.admin;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin", layout = AdminLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Admin")
public class AdminView extends VerticalLayout {

    public AdminView() {
        add(new H1("Cinema Administration"));
        add(new Anchor("/admin/movies", "Manage Movies"));
        add(new Anchor("/admin/shows", "Manage Shows"));
    }
}
