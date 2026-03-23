package com.example.specdriven.stock;

import com.example.specdriven.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "receive", layout = MainLayout.class)
@PageTitle("Receive Stock — Stash.log")
@PermitAll
public class ReceiveStockView extends VerticalLayout {

    public ReceiveStockView() {
        add(new H1("Receive Stock"));
        add(new Paragraph("Coming soon."));
    }
}
