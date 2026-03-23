package com.example.specdriven.expense;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.security.core.userdetails.UserDetails;

@Route("submit-expense")
@PageTitle("Submit Expense — GreenLedger")
@PermitAll
public class SubmitExpenseView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final AuthenticationContext authContext;

    private final NumberField amountField = new NumberField("Amount ($)");
    private final DatePicker dateField = new DatePicker("Date");
    private final Select<ExpenseCategory> categoryField = new Select<>();
    private final TextArea descriptionField = new TextArea("Description");
    private final Upload receiptUpload;

    private byte[] receiptData;
    private String receiptFileName;
    private String receiptContentType;

    public SubmitExpenseView(ExpenseService expenseService, AuthenticationContext authContext) {
        this.expenseService = expenseService;
        this.authContext = authContext;

        setPadding(true);
        setMaxWidth("800px");

        H2 title = new H2("Submit Expense");
        title.addClassName("view-title");

        amountField.setMin(0.01);
        amountField.setStep(0.01);
        amountField.setRequired(true);
        amountField.setRequiredIndicatorVisible(true);

        dateField.setMax(LocalDate.now());
        dateField.setRequired(true);
        dateField.setRequiredIndicatorVisible(true);

        categoryField.setLabel("Category");
        categoryField.setItems(ExpenseCategory.values());
        categoryField.setItemLabelGenerator(cat -> switch (cat) {
            case TRAVEL -> "Travel";
            case MEALS -> "Meals";
            case OFFICE_SUPPLIES -> "Office Supplies";
            case OTHER -> "Other";
        });
        categoryField.setRequiredIndicatorVisible(true);

        descriptionField.setRequired(true);
        descriptionField.setRequiredIndicatorVisible(true);
        descriptionField.setMaxLength(500);

        receiptUpload = new Upload(UploadHandler.inMemory((metadata, data) -> {
            receiptData = data;
            receiptFileName = metadata.fileName();
            receiptContentType = metadata.contentType();
        }));
        receiptUpload.setAcceptedFileTypes("image/jpeg", "image/png", ".jpg", ".jpeg", ".png");
        receiptUpload.setMaxFiles(1);

        FormLayout formLayout = new FormLayout();
        formLayout.add(amountField, dateField, categoryField, descriptionField);
        formLayout.setColspan(descriptionField, 2);
        formLayout.addClassName("submit-form");

        Button submitButton = new Button("Submit", e -> handleSubmit());
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClassName("btn-approve");

        add(title, formLayout, receiptUpload, submitButton);
    }

    private void handleSubmit() {
        if (!validateForm()) {
            return;
        }

        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(amountField.getValue()));
        expense.setDate(dateField.getValue());
        expense.setCategory(categoryField.getValue());
        expense.setDescription(descriptionField.getValue());
        expense.setReceipt(receiptData);
        expense.setReceiptFileName(receiptFileName);
        expense.setReceiptContentType(receiptContentType);

        String username = authContext.getAuthenticatedUser(UserDetails.class)
                .map(UserDetails::getUsername)
                .orElse("unknown");
        expense.setSubmitterUsername(username);

        expenseService.submit(expense);

        Notification.show("Expense submitted successfully!", 3000,
                Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        getUI().ifPresent(ui -> ui.navigate("my-expenses"));
    }

    private boolean validateForm() {
        boolean valid = true;

        if (amountField.getValue() == null || amountField.getValue() <= 0) {
            amountField.setInvalid(true);
            amountField.setErrorMessage("Amount must be greater than zero");
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

        return valid;
    }
}
