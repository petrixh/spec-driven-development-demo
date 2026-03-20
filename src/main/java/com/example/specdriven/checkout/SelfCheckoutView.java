package com.example.specdriven.checkout;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("")
@AnonymousAllowed
public class SelfCheckoutView extends VerticalLayout {

    static final String EMPLOYEE_CODE = "112233";

    private final CheckoutService checkoutService;

    final List<TransactionItem> items = new ArrayList<>();
    Transaction currentTransaction = new Transaction();

    final Grid<TransactionItem> itemGrid;
    final Span totalLabel;
    final Button payButton;
    final TextField barcodeInput;
    boolean paymentMode = false;
    boolean employeeMode = false;

    // Employee mode components
    final Span employeeBadge;
    final Button paidByCashButton;
    final Button selectCustomerButton;
    final Button exitEmployeeModeButton;
    final Button callEmployeeButton;

    public SelfCheckoutView(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);
        addClassName("checkout-view");

        // Header with employee mode badge
        H2 header = new H2("BuyBuy Self-Checkout");
        header.addClassName("checkout-header");

        employeeBadge = new Span("Employee Mode");
        employeeBadge.addClassName("employee-badge");
        employeeBadge.setVisible(false);

        HorizontalLayout headerRow = new HorizontalLayout(header, employeeBadge);
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);
        headerRow.setWidthFull();

        // Item grid
        itemGrid = new Grid<>();
        itemGrid.addColumn(item -> item.getProduct().getName())
                .setHeader("Product")
                .setFlexGrow(3);
        itemGrid.addColumn(TransactionItem::getQuantity)
                .setHeader("Qty")
                .setFlexGrow(1);
        itemGrid.addColumn(item -> formatPrice(item.getPriceAtScan()))
                .setHeader("Unit Price")
                .setFlexGrow(1);
        itemGrid.addColumn(item -> formatPrice(item.getLineTotal()))
                .setHeader("Total")
                .setFlexGrow(1);
        itemGrid.setItems(items);
        itemGrid.addClassName("item-grid");
        itemGrid.setSizeFull();

        // Right panel
        totalLabel = new Span("€0.00");
        totalLabel.addClassName("total-price");

        Span totalCaption = new Span("Total");
        totalCaption.addClassName("total-caption");

        VerticalLayout totalSection = new VerticalLayout(totalCaption, totalLabel);
        totalSection.setAlignItems(FlexComponent.Alignment.CENTER);
        totalSection.setSpacing(false);
        totalSection.setPadding(false);

        payButton = new Button("Pay", e -> openPaymentDialog());
        payButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        payButton.addClassName("pay-button");
        payButton.getStyle().set("--vaadin-button-primary-background", "#2E7D32");
        payButton.setEnabled(false);

        // Employee mode buttons (hidden by default)
        paidByCashButton = new Button("Paid by Cash", e -> handleCashPayment());
        paidByCashButton.addClassName("paid-by-cash-button");
        paidByCashButton.getStyle().set("background-color", "#1565C0");
        paidByCashButton.getStyle().set("color", "white");
        paidByCashButton.setVisible(false);

        selectCustomerButton = new Button("Select Customer", e -> openSelectCustomerDialog());
        selectCustomerButton.addClassName("select-customer-button");
        selectCustomerButton.getStyle().set("background-color", "#1565C0");
        selectCustomerButton.getStyle().set("color", "white");
        selectCustomerButton.setVisible(false);

        exitEmployeeModeButton = new Button("Exit Employee Mode", e -> exitEmployeeMode());
        exitEmployeeModeButton.addClassName("exit-employee-button");
        exitEmployeeModeButton.setVisible(false);

        callEmployeeButton = new Button("Call Employee", e -> openEmployeeCodeDialog());
        callEmployeeButton.addClassName("call-employee-button");
        callEmployeeButton.getStyle().set("background-color", "#F57C00");
        callEmployeeButton.getStyle().set("color", "white");

        VerticalLayout actionPanel = new VerticalLayout(
                totalSection, payButton, paidByCashButton, selectCustomerButton,
                callEmployeeButton, exitEmployeeModeButton
        );
        actionPanel.setAlignItems(FlexComponent.Alignment.STRETCH);
        actionPanel.setWidth("300px");
        actionPanel.addClassName("action-panel");

        // Main content
        HorizontalLayout content = new HorizontalLayout(itemGrid, actionPanel);
        content.setSizeFull();
        content.setFlexGrow(1, itemGrid);
        content.addClassName("checkout-content");

        // Barcode input
        barcodeInput = new TextField();
        barcodeInput.addClassName("barcode-input");
        barcodeInput.setPlaceholder("Scan barcode...");
        barcodeInput.setWidthFull();
        barcodeInput.addKeyPressListener(Key.ENTER, e -> handleBarcodeScan());
        barcodeInput.setAutofocus(true);

        add(headerRow, barcodeInput, content);
        setFlexGrow(1, content);
    }

    void handleBarcodeScan() {
        String barcode = barcodeInput.getValue().trim();
        barcodeInput.clear();

        if (barcode.isEmpty()) {
            return;
        }

        if (paymentMode) {
            return;
        }

        Optional<Product> productOpt = checkoutService.findProductByBarcode(barcode);
        if (productOpt.isEmpty()) {
            Notification notification = Notification.show("Unknown product: " + barcode, 3000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            barcodeInput.focus();
            return;
        }

        Product product = productOpt.get();

        Optional<TransactionItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            TransactionItem newItem = new TransactionItem(product, 1);
            items.add(newItem);
            currentTransaction.addItem(newItem);
        }

        itemGrid.getDataProvider().refreshAll();
        updateTotal();
        updateButtonStates();
        barcodeInput.focus();
    }

    private void updateTotal() {
        BigDecimal total = items.stream()
                .map(TransactionItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText(formatPrice(total));
    }

    private void updateButtonStates() {
        boolean hasItems = !items.isEmpty();
        payButton.setEnabled(hasItems);
        paidByCashButton.setEnabled(hasItems);
        selectCustomerButton.setEnabled(hasItems);
    }

    // --- Employee Mode ---

    void openEmployeeCodeDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Employee Authentication");
        dialog.addClassName("employee-code-dialog");

        Span prompt = new Span("Enter employee code");
        TextField codeInput = new TextField();
        codeInput.setPlaceholder("Code...");
        codeInput.setWidthFull();
        codeInput.setAutofocus(true);

        Span errorMsg = new Span();
        errorMsg.addClassName("payment-error");
        errorMsg.setVisible(false);

        codeInput.addKeyPressListener(Key.ENTER, e -> {
            String code = codeInput.getValue().trim();
            if (EMPLOYEE_CODE.equals(code)) {
                dialog.close();
                enterEmployeeMode();
            } else {
                errorMsg.setText("Invalid code. Try again.");
                errorMsg.setVisible(true);
                codeInput.clear();
                codeInput.focus();
            }
        });

        Button submitButton = new Button("Submit", e -> {
            String code = codeInput.getValue().trim();
            if (EMPLOYEE_CODE.equals(code)) {
                dialog.close();
                enterEmployeeMode();
            } else {
                errorMsg.setText("Invalid code. Try again.");
                errorMsg.setVisible(true);
                codeInput.clear();
                codeInput.focus();
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout dialogContent = new VerticalLayout(prompt, codeInput, errorMsg);
        dialogContent.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(dialogContent);
        dialog.getFooter().add(cancelButton, submitButton);

        dialog.open();
    }

    void enterEmployeeMode() {
        employeeMode = true;
        employeeBadge.setVisible(true);
        paidByCashButton.setVisible(true);
        selectCustomerButton.setVisible(true);
        exitEmployeeModeButton.setVisible(true);
        callEmployeeButton.setVisible(false);
        updateButtonStates();
        barcodeInput.focus();
    }

    void exitEmployeeMode() {
        employeeMode = false;
        employeeBadge.setVisible(false);
        paidByCashButton.setVisible(false);
        selectCustomerButton.setVisible(false);
        exitEmployeeModeButton.setVisible(false);
        callEmployeeButton.setVisible(true);
        barcodeInput.focus();
    }

    private void handleCashPayment() {
        if (items.isEmpty()) {
            return;
        }
        checkoutService.completeCashTransaction(currentTransaction);
        showCashConfirmation();
    }

    private void showCashConfirmation() {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Transaction Complete");
        confirmDialog.setCloseOnOutsideClick(false);
        confirmDialog.setCloseOnEsc(false);

        Span message = new Span("Cash payment received. Transaction complete.");
        message.addClassName("confirmation-message");

        Span receiptNote = new Span("No receipt can be printed.");
        receiptNote.addClassName("receipt-note");

        Button doneButton = new Button("Done", e -> {
            confirmDialog.close();
            exitEmployeeMode();
            resetView();
        });
        doneButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        VerticalLayout content = new VerticalLayout(message, receiptNote);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        confirmDialog.add(content);
        confirmDialog.getFooter().add(doneButton);

        confirmDialog.open();
    }

    // --- Select Customer Dialog ---

    private void openSelectCustomerDialog() {
        if (items.isEmpty()) {
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Select Customer");
        dialog.setWidth("500px");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by name or customer number...");
        searchField.setWidthFull();

        Grid<Customer> customerGrid = new Grid<>();
        customerGrid.addColumn(Customer::getName).setHeader("Name").setFlexGrow(2);
        customerGrid.addColumn(Customer::getCustomerNumber).setHeader("Card Number").setFlexGrow(1);
        customerGrid.setItems(checkoutService.findCustomersBySearch(""));
        customerGrid.setHeight("300px");

        searchField.addValueChangeListener(e ->
                customerGrid.setItems(checkoutService.findCustomersBySearch(e.getValue())));

        Button confirmButton = new Button("Confirm", e -> {
            customerGrid.getSelectedItems().stream().findFirst().ifPresent(customer -> {
                checkoutService.completeTransaction(currentTransaction, customer);
                dialog.close();
                showConfirmation(customer);
            });
        });
        confirmButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        confirmButton.setEnabled(false);

        customerGrid.addSelectionListener(e ->
                confirmButton.setEnabled(!e.getAllSelectedItems().isEmpty()));

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout dialogContent = new VerticalLayout(searchField, customerGrid);
        dialogContent.setSpacing(true);
        dialogContent.setPadding(false);
        dialog.add(dialogContent);
        dialog.getFooter().add(cancelButton, confirmButton);

        dialog.open();
    }

    // --- Payment Dialog (customer self-pay) ---

    private void openPaymentDialog() {
        paymentMode = true;

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Payment");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.addClassName("payment-dialog");

        Span prompt = new Span("Scan your store card");
        prompt.addClassName("payment-prompt");

        TextField cardInput = new TextField();
        cardInput.setPlaceholder("Scan card...");
        cardInput.setWidthFull();
        cardInput.setAutofocus(true);

        Span errorMessage = new Span();
        errorMessage.addClassName("payment-error");
        errorMessage.setVisible(false);

        cardInput.addKeyPressListener(Key.ENTER, e -> {
            String cardNumber = cardInput.getValue().trim();
            cardInput.clear();

            if (cardNumber.isEmpty()) {
                return;
            }

            Optional<Customer> customerOpt = checkoutService.findCustomerByNumber(cardNumber);
            if (customerOpt.isEmpty()) {
                errorMessage.setText("Unknown card: " + cardNumber + ". Try again or cancel.");
                errorMessage.setVisible(true);
                cardInput.focus();
                return;
            }

            checkoutService.completeTransaction(currentTransaction, customerOpt.get());
            dialog.close();
            paymentMode = false;
            showConfirmation(customerOpt.get());
        });

        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
            paymentMode = false;
            barcodeInput.focus();
        });

        VerticalLayout dialogContent = new VerticalLayout(prompt, cardInput, errorMessage);
        dialogContent.setAlignItems(FlexComponent.Alignment.CENTER);
        dialogContent.setSpacing(true);
        dialog.add(dialogContent);
        dialog.getFooter().add(cancelButton);

        dialog.open();
    }

    private void showConfirmation(Customer customer) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Transaction Complete");
        confirmDialog.setCloseOnOutsideClick(false);
        confirmDialog.setCloseOnEsc(false);
        confirmDialog.addClassName("confirmation-dialog");

        Span message = new Span("Thank you, " + customer.getName() + "! Your purchase is complete.");
        message.addClassName("confirmation-message");

        Span receiptNote = new Span("No receipt can be printed.");
        receiptNote.addClassName("receipt-note");

        Button doneButton = new Button("Done", e -> {
            confirmDialog.close();
            if (employeeMode) {
                exitEmployeeMode();
            }
            resetView();
        });
        doneButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        VerticalLayout content = new VerticalLayout(message, receiptNote);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        confirmDialog.add(content);
        confirmDialog.getFooter().add(doneButton);

        confirmDialog.open();
    }

    void resetView() {
        items.clear();
        currentTransaction = new Transaction();
        itemGrid.getDataProvider().refreshAll();
        updateTotal();
        updateButtonStates();
        barcodeInput.clear();
        barcodeInput.focus();
    }

    private String formatPrice(BigDecimal price) {
        return "€" + price.setScale(2).toPlainString();
    }
}
