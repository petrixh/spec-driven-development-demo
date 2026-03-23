package com.example.specdriven.expense;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("my-expenses")
@PageTitle("My Expenses — GreenLedger")
@PermitAll
public class MyExpensesView extends VerticalLayout {

    public MyExpensesView() {
        add(new H2("My Expenses"));
    }
}
