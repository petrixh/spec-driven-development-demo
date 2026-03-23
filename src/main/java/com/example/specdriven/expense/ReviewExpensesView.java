package com.example.specdriven.expense;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.text.NumberFormat;
import java.util.Locale;

@Route("review-expenses")
@PageTitle("Review Expenses — GreenLedger")
@RolesAllowed("MANAGER")
public class ReviewExpensesView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final Grid<Expense> grid = new Grid<>(Expense.class, false);
    private final VerticalLayout detailPanel = new VerticalLayout();

    public ReviewExpensesView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        setPadding(true);
        setSizeFull();

        H2 title = new H2("Review Expenses");

        configureGrid();
        configureDetailPanel();

        HorizontalLayout content = new HorizontalLayout(grid, detailPanel);
        content.setSizeFull();
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, detailPanel);

        add(title, content);
        setFlexGrow(1, content);

        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(Expense::getSubmitterUsername).setHeader("Employee").setAutoWidth(true);
        grid.addColumn(Expense::getDate).setHeader("Date").setAutoWidth(true);
        grid.addColumn(e -> formatCategory(e.getCategory())).setHeader("Category").setAutoWidth(true);
        grid.addColumn(Expense::getDescription).setHeader("Description").setFlexGrow(1);
        grid.addColumn(e -> formatCurrency(e.getAmount())).setHeader("Amount").setAutoWidth(true);
        grid.addComponentColumn(e -> {
            Span icon = new Span(e.getReceipt() != null ? "Yes" : "No");
            return icon;
        }).setHeader("Receipt").setAutoWidth(true);

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue()));
        grid.setSizeFull();
    }

    private void configureDetailPanel() {
        detailPanel.setWidth("400px");
        detailPanel.setPadding(true);
        detailPanel.setVisible(false);
        detailPanel.getStyle().set("background", "var(--vaadin-background-color)")
                .set("border-radius", "var(--vaadin-radius-l)");
    }

    private void showDetail(Expense expense) {
        detailPanel.removeAll();
        if (expense == null) {
            detailPanel.setVisible(false);
            return;
        }
        detailPanel.setVisible(true);

        H3 detailTitle = new H3("Expense Details");
        detailPanel.add(detailTitle);
        detailPanel.add(new Paragraph("Employee: " + expense.getSubmitterUsername()));
        detailPanel.add(new Paragraph("Date: " + expense.getDate()));
        detailPanel.add(new Paragraph("Category: " + formatCategory(expense.getCategory())));
        detailPanel.add(new Paragraph("Description: " + expense.getDescription()));
        detailPanel.add(new Paragraph("Amount: " + formatCurrency(expense.getAmount())));

        if (expense.getReceipt() != null) {
            StreamResource resource = new StreamResource(
                    expense.getReceiptFileName(),
                    () -> new ByteArrayInputStream(expense.getReceipt()));
            resource.setContentType(expense.getReceiptContentType());
            Image receiptImage = new Image(resource, "Receipt");
            receiptImage.setMaxWidth("100%");
            receiptImage.setMaxHeight("300px");
            detailPanel.add(receiptImage);
        }

        Button approveBtn = new Button("Approve", e -> approve(expense));
        approveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        approveBtn.getStyle().set("background", "#2E7D32");

        Button rejectBtn = new Button("Reject", e -> openRejectDialog(expense));
        rejectBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout actions = new HorizontalLayout(approveBtn, rejectBtn);
        detailPanel.add(actions);
    }

    private void approve(Expense expense) {
        expenseService.approve(expense.getId());
        Notification.show("Expense approved!", 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        refreshGrid();
        detailPanel.setVisible(false);
    }

    private void openRejectDialog(Expense expense) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Reject Expense");

        TextArea commentField = new TextArea("Reason for rejection");
        commentField.setWidthFull();
        commentField.setRequired(true);
        commentField.setRequiredIndicatorVisible(true);

        Span errorMsg = new Span();
        errorMsg.getStyle().set("color", "var(--vaadin-input-field-invalid-color, #d32f2f)");

        dialog.add(commentField, errorMsg);

        Button confirmBtn = new Button("Reject", e -> {
            if (commentField.getValue() == null || commentField.getValue().isBlank()) {
                commentField.setInvalid(true);
                commentField.setErrorMessage("Comment is required for rejection");
                errorMsg.setText("Comment is required for rejection");
                return;
            }
            expenseService.reject(expense.getId(), commentField.getValue());
            dialog.close();
            Notification.show("Expense rejected.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            refreshGrid();
            detailPanel.setVisible(false);
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, confirmBtn);
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(expenseService.findPending());
        grid.asSingleSelect().clear();
    }

    private String formatCategory(ExpenseCategory category) {
        return switch (category) {
            case TRAVEL -> "Travel";
            case MEALS -> "Meals";
            case OFFICE_SUPPLIES -> "Office Supplies";
            case OTHER -> "Other";
        };
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
