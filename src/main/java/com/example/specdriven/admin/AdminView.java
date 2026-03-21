package com.example.specdriven.admin;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin", layout = AdminLayout.class)
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {

    public AdminView() {
        H2 header = new H2("Admin Dashboard");
        Paragraph intro = new Paragraph("Select a section to manage:");

        RouterLink productsLink = new RouterLink("Product Management", ProductAdminView.class);

        add(header, intro, productsLink);
    }
}
