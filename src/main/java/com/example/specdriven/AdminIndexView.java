package com.example.specdriven;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminIndexView extends VerticalLayout {

    public AdminIndexView() {
        addClassName("admin-view");
        add(new H2("Admin"));
        add(new Anchor("admin/movies", "Manage Movies"));
        add(new Anchor("admin/shows", "Manage Shows"));
    }
}
