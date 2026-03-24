package com.example.specdriven.expense;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Route("submit-expense")
@PageTitle("Submit Expense — GreenLedger")
@RolesAllowed("EMPLOYEE")
public class SubmitExpenseView extends VerticalLayout {

    private final ExpenseService expenseService;

    private byte[] uploadedReceiptData;
    private String uploadedReceiptFileName;
    private String uploadedReceiptMimeType;

    public SubmitExpenseView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        addClassName("view-content");

        H2 title = new H2("Submit Expense");

        BigDecimalField amountField = new BigDecimalField("Amount");
        amountField.setRequiredIndicatorVisible(true);
        amountField.setWidth("100%");
        amountField.setMaxWidth("400px");

        DatePicker dateField = new DatePicker("Date");
        dateField.setRequiredIndicatorVisible(true);
        dateField.setMax(LocalDate.now());
        dateField.setWidth("100%");
        dateField.setMaxWidth("400px");

        ComboBox<Expense.Category> categoryField = new ComboBox<>("Category");
        categoryField.setItems(Expense.Category.values());
        categoryField.setItemLabelGenerator(Expense.Category::getLabel);
        categoryField.setRequiredIndicatorVisible(true);
        categoryField.setWidth("100%");
        categoryField.setMaxWidth("400px");

        TextArea descriptionField = new TextArea("Description");
        descriptionField.setRequiredIndicatorVisible(true);
        descriptionField.setWidth("100%");
        descriptionField.setMaxWidth("400px");

        Upload receiptUpload = new Upload(UploadHandler.inMemory((metadata, data) -> {
            uploadedReceiptData = data;
            uploadedReceiptFileName = metadata.fileName();
            uploadedReceiptMimeType = metadata.contentType();
        }));
        receiptUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        receiptUpload.setMaxFiles(1);
        receiptUpload.setWidth("100%");
        receiptUpload.setMaxWidth("400px");

        receiptUpload.addFileRejectedListener(event -> {
            Notification.show(event.getErrorMessage(), 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        });

        Button submitButton = new Button("Submit");
        submitButton.addThemeVariants(ButtonVariant.PRIMARY);
        submitButton.addClickListener(event -> {
            // Validate
            boolean valid = true;

            if (amountField.getValue() == null || amountField.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                amountField.setInvalid(true);
                amountField.setErrorMessage("Amount must be a positive number");
                valid = false;
            } else {
                amountField.setInvalid(false);
            }

            if (dateField.getValue() == null) {
                dateField.setInvalid(true);
                dateField.setErrorMessage("Date is required");
                valid = false;
            } else if (dateField.getValue().isAfter(LocalDate.now())) {
                dateField.setInvalid(true);
                dateField.setErrorMessage("Date cannot be in the future");
                valid = false;
            } else {
                dateField.setInvalid(false);
            }

            if (categoryField.getValue() == null) {
                categoryField.setInvalid(true);
                categoryField.setErrorMessage("Category is required");
                valid = false;
            } else {
                categoryField.setInvalid(false);
            }

            if (descriptionField.getValue() == null || descriptionField.getValue().isBlank()) {
                descriptionField.setInvalid(true);
                descriptionField.setErrorMessage("Description is required");
                valid = false;
            } else {
                descriptionField.setInvalid(false);
            }

            if (!valid) {
                return;
            }

            Expense expense = new Expense();
            expense.setAmount(amountField.getValue());
            expense.setDate(dateField.getValue());
            expense.setCategory(categoryField.getValue());
            expense.setDescription(descriptionField.getValue());
            expense.setEmployeeUsername(getCurrentUsername());

            if (uploadedReceiptData != null) {
                expense.setReceipt(uploadedReceiptData);
                expense.setReceiptFileName(uploadedReceiptFileName);
                expense.setReceiptMimeType(uploadedReceiptMimeType);
            }

            expenseService.submit(expense);

            Notification.show("Expense submitted successfully!", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.SUCCESS);

            UI.getCurrent().navigate("my-expenses");
        });

        add(title, amountField, dateField, categoryField, descriptionField, receiptUpload, submitButton);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
