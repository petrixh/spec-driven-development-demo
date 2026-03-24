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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;

@Route("review-expenses")
@PageTitle("Review Expenses — GreenLedger")
@RolesAllowed("MANAGER")
public class ReviewExpensesView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final Grid<Expense> grid = new Grid<>();
    private final VerticalLayout detailPanel = new VerticalLayout();

    public ReviewExpensesView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        addClassName("view-content");

        H2 title = new H2("Review Expenses");

        // Grid
        grid.addColumn(Expense::getEmployeeUsername).setHeader("Employee").setSortable(true);
        grid.addColumn(e -> e.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).setHeader("Date").setSortable(true);
        grid.addColumn(e -> e.getCategory().getLabel()).setHeader("Category").setSortable(true);
        grid.addColumn(Expense::getDescription).setHeader("Description");
        grid.addColumn(e -> String.format("$%.2f", e.getAmount())).setHeader("Amount").setSortable(true);
        grid.addColumn(e -> e.hasReceipt() ? "Yes" : "No").setHeader("Receipt");
        grid.setWidth("100%");

        grid.addSelectionListener(event ->
                event.getFirstSelectedItem().ifPresentOrElse(this::showDetail, this::clearDetail));

        // Detail panel
        detailPanel.setVisible(false);
        detailPanel.setPadding(true);
        detailPanel.getStyle()
                .set("background", "var(--aura-surface-color)")
                .set("border-radius", "var(--vaadin-radius-l)")
                .set("margin-top", "var(--vaadin-gap-m)");

        refreshGrid();

        add(title, grid, detailPanel);
    }

    private void refreshGrid() {
        grid.setItems(expenseService.getPendingExpenses());
        grid.deselectAll();
        clearDetail();
    }

    private void showDetail(Expense expense) {
        detailPanel.removeAll();
        detailPanel.setVisible(true);

        H3 detailTitle = new H3("Expense Details");

        Paragraph employee = new Paragraph("Employee: " + expense.getEmployeeUsername());
        Paragraph date = new Paragraph("Date: " + expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Paragraph category = new Paragraph("Category: " + expense.getCategory().getLabel());
        Paragraph description = new Paragraph("Description: " + expense.getDescription());
        Paragraph amount = new Paragraph("Amount: $" + String.format("%.2f", expense.getAmount()));

        detailPanel.add(detailTitle, employee, date, category, description, amount);

        if (expense.hasReceipt()) {
            StreamResource resource = new StreamResource(
                    expense.getReceiptFileName(),
                    () -> new ByteArrayInputStream(expense.getReceipt()));
            resource.setContentType(expense.getReceiptMimeType());
            Image receiptImage = new Image(resource, "Receipt");
            receiptImage.addClassName("receipt-image");
            detailPanel.add(receiptImage);
        }

        Button approveBtn = new Button("Approve");
        approveBtn.addThemeVariants(ButtonVariant.PRIMARY);
        approveBtn.getStyle().set("background", "#388E3C").set("color", "white");
        approveBtn.addClickListener(e -> {
            expenseService.approve(expense.getId());
            Notification.show("Expense approved", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.SUCCESS);
            refreshGrid();
        });

        Button rejectBtn = new Button("Reject");
        rejectBtn.addThemeVariants(ButtonVariant.ERROR);
        rejectBtn.addClickListener(e -> showRejectDialog(expense));

        HorizontalLayout actions = new HorizontalLayout(approveBtn, rejectBtn);
        actions.setAlignItems(FlexComponent.Alignment.CENTER);

        detailPanel.add(actions);
    }

    private void showRejectDialog(Expense expense) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Reject Expense");

        TextArea commentField = new TextArea("Reason for rejection");
        commentField.setRequiredIndicatorVisible(true);
        commentField.setWidthFull();

        Span errorMsg = new Span();
        errorMsg.getStyle().set("color", "var(--aura-red-text)");
        errorMsg.setVisible(false);

        Button confirmReject = new Button("Confirm Rejection", e -> {
            if (commentField.getValue() == null || commentField.getValue().isBlank()) {
                commentField.setInvalid(true);
                commentField.setErrorMessage("A rejection comment is required");
                errorMsg.setText("A rejection comment is required");
                errorMsg.setVisible(true);
                return;
            }
            expenseService.reject(expense.getId(), commentField.getValue());
            dialog.close();
            Notification.show("Expense rejected", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
            refreshGrid();
        });
        confirmReject.addThemeVariants(ButtonVariant.ERROR);

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.add(commentField, errorMsg);
        dialog.getFooter().add(cancel, confirmReject);
        dialog.open();
    }

    private void clearDetail() {
        detailPanel.removeAll();
        detailPanel.setVisible(false);
    }
}
