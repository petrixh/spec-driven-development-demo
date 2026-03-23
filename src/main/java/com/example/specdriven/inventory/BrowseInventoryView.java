package com.example.specdriven.inventory;

import com.example.specdriven.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "inventory", layout = MainLayout.class)
@PageTitle("Inventory — Stash.log")
@PermitAll
public class BrowseInventoryView extends VerticalLayout {

    public BrowseInventoryView() {
        add(new H1("Inventory"));
        add(new Paragraph("Coming soon."));
    }
}
