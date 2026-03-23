package com.example.specdriven.expense;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.security.core.userdetails.UserDetails;

@Route("my-expenses")
@PageTitle("My Expenses — GreenLedger")
@PermitAll
public class MyExpensesView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final String username;

    private final Select<ExpenseStatus> statusFilter = new Select<>();
    private final DatePicker fromDate = new DatePicker("From");
    private final DatePicker toDate = new DatePicker("To");
    private final Grid<Expense> grid = new Grid<>(Expense.class, false);
    private final Span totalLabel = new Span();

    public MyExpensesView(ExpenseService expenseService, AuthenticationContext authContext) {
        this.expenseService = expenseService;
        this.username = authContext.getAuthenticatedUser(UserDetails.class)
                .map(UserDetails::getUsername)
                .orElse("unknown");

        setPadding(true);

        H2 title = new H2("My Expenses");
        title.addClassName("view-title");

        configureFilters();
        configureGrid();

        HorizontalLayout filterBar = new HorizontalLayout(statusFilter, fromDate, toDate, totalLabel);
        filterBar.setAlignItems(Alignment.BASELINE);
        filterBar.setWidthFull();
        filterBar.addClassName("filter-bar");

        totalLabel.addClassName("total-label");

        add(title, filterBar, grid);
        refreshGrid();
    }

    private void configureFilters() {
        statusFilter.setLabel("Status");
        statusFilter.setItems(ExpenseStatus.values());
        statusFilter.setItemLabelGenerator(s -> {
            if (s == null) return "All";
            return switch (s) {
                case PENDING -> "Pending";
                case APPROVED -> "Approved";
                case REJECTED -> "Rejected";
            };
        });
        statusFilter.setPlaceholder("All");
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.setEmptySelectionCaption("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        fromDate.addValueChangeListener(e -> refreshGrid());
        toDate.addValueChangeListener(e -> refreshGrid());
    }

    private void configureGrid() {
        grid.addColumn(Expense::getDate).setHeader("Date").setSortable(true).setAutoWidth(true);
        grid.addColumn(e -> formatCategory(e.getCategory())).setHeader("Category").setAutoWidth(true);
        grid.addColumn(Expense::getDescription).setHeader("Description").setFlexGrow(1);
        grid.addColumn(e -> formatCurrency(e.getAmount())).setHeader("Amount").setAutoWidth(true);
        grid.addComponentColumn(e -> createStatusBadge(e.getStatus())).setHeader("Status").setAutoWidth(true);
        grid.setWidthFull();
    }

    private void refreshGrid() {
        ExpenseStatus status = statusFilter.getValue();
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();

        List<Expense> expenses = expenseService.findByUser(username, status, from, to);
        grid.setItems(expenses);

        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText("Total: " + formatCurrency(total));
    }

    private Span createStatusBadge(ExpenseStatus status) {
        Span badge = new Span(switch (status) {
            case PENDING -> "Pending";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
        });
        badge.addClassName("badge");
        badge.addClassName(switch (status) {
            case PENDING -> "badge-pending";
            case APPROVED -> "badge-approved";
            case REJECTED -> "badge-rejected";
        });
        return badge;
    }

    private String formatCategory(ExpenseCategory category) {
        return switch (category) {
            case TRAVEL -> "Travel";
            case MEALS -> "Meals";
            case OFFICE_SUPPLIES -> "Office Supplies";
            case OTHER -> "Other";
        };
    }

    private String formatCurrency(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
