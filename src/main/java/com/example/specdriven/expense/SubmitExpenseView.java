package com.example.specdriven.expense;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("submit-expense")
@PageTitle("Submit Expense — GreenLedger")
@PermitAll
public class SubmitExpenseView extends VerticalLayout {

    public SubmitExpenseView() {
        add(new H2("Submit Expense"));
    }
}
