package com.example.specdriven.admin.ui;

import jakarta.annotation.security.RolesAllowed;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {

    public AdminDashboardView() {
        setSizeFull();
        setPadding(false);

        H1 heading = new H1("Admin Dashboard");
        Paragraph copy = new Paragraph("Manage the cinema catalogue and show schedule.");
        RouterLink movieLink = new RouterLink("Manage Movies", MovieAdminView.class);

        add(heading, copy, movieLink);
    }
}
