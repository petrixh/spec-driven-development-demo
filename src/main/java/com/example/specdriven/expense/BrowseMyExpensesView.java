package com.example.specdriven.expense;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("my-expenses")
@PageTitle("My Expenses — GreenLedger")
@RolesAllowed("EMPLOYEE")
public class BrowseMyExpensesView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final Grid<Expense> grid = new Grid<>();
    private final Span totalLabel = new Span();
    private ComboBox<String> statusFilter;
    private DatePicker fromDate;
    private DatePicker toDate;

    public BrowseMyExpensesView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        addClassName("view-content");

        H2 title = new H2("My Expenses");

        // Filters
        statusFilter = new ComboBox<>("Status");
        statusFilter.setItems("All", "Pending", "Approved", "Rejected");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        fromDate = new DatePicker("From");
        fromDate.addValueChangeListener(e -> refreshGrid());

        toDate = new DatePicker("To");
        toDate.addValueChangeListener(e -> refreshGrid());

        HorizontalLayout filters = new HorizontalLayout(statusFilter, fromDate, toDate);
        filters.setAlignItems(Alignment.BASELINE);

        // Grid
        Grid.Column<Expense> dateColumn = grid.addColumn(e -> e.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .setHeader("Date")
                .setKey("date")
                .setSortable(true)
                .setComparator(Expense::getDate);

        grid.addColumn(e -> e.getCategory().getLabel())
                .setHeader("Category")
                .setSortable(true);

        grid.addColumn(Expense::getDescription)
                .setHeader("Description")
                .setSortable(true);

        grid.addColumn(e -> String.format("$%.2f", e.getAmount()))
                .setHeader("Amount")
                .setSortable(true)
                .setComparator(Expense::getAmount);

        grid.addColumn(new ComponentRenderer<>(expense -> {
            Span badge = new Span(expense.getStatus().name());
            badge.addClassName("badge");
            switch (expense.getStatus()) {
                case PENDING -> badge.addClassName("badge-pending");
                case APPROVED -> badge.addClassName("badge-approved");
                case REJECTED -> badge.addClassName("badge-rejected");
            }
            return badge;
        })).setHeader("Status");

        grid.sort(List.of(new GridSortOrder<>(dateColumn, SortDirection.DESCENDING)));
        grid.setWidth("100%");

        // Total
        totalLabel.getStyle()
                .set("font-weight", "var(--aura-font-weight-semibold)")
                .set("font-size", "var(--aura-font-size-l)");

        refreshGrid();

        add(title, filters, grid, totalLabel);
    }

    private void refreshGrid() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Expense> expenses = expenseService.getExpensesForEmployee(username);

        // Apply status filter
        String status = statusFilter.getValue();
        if (status != null && !status.equals("All")) {
            Expense.Status filterStatus = Expense.Status.valueOf(status.toUpperCase());
            expenses = expenses.stream()
                    .filter(e -> e.getStatus() == filterStatus)
                    .toList();
        }

        // Apply date range filter
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        if (from != null) {
            expenses = expenses.stream()
                    .filter(e -> !e.getDate().isBefore(from))
                    .toList();
        }
        if (to != null) {
            expenses = expenses.stream()
                    .filter(e -> !e.getDate().isAfter(to))
                    .toList();
        }

        grid.setItems(expenses);

        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
}
