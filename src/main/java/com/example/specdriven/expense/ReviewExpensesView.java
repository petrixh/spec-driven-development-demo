package com.example.specdriven.expense;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("review-expenses")
@PageTitle("Review Expenses — GreenLedger")
@RolesAllowed("MANAGER")
public class ReviewExpensesView extends VerticalLayout {

    public ReviewExpensesView() {
        add(new H2("Review Expenses"));
    }
}
