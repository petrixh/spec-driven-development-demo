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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("")
public class SelfCheckoutView extends VerticalLayout {

    private final CheckoutService checkoutService;

    final List<TransactionItem> items = new ArrayList<>();
    Transaction currentTransaction = new Transaction();

    final Grid<TransactionItem> itemGrid;
    final Span totalLabel;
    final Button payButton;
    final TextField barcodeInput;
    boolean paymentMode = false;

    public SelfCheckoutView(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);
        addClassName("checkout-view");

        // Header
        H2 header = new H2("BuyBuy Self-Checkout");
        header.addClassName("checkout-header");

        // Item grid - left/main area
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

        // Right panel - total and actions
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

        Button callEmployeeButton = new Button("Call Employee");
        callEmployeeButton.addClassName("call-employee-button");
        callEmployeeButton.getStyle().set("background-color", "#F57C00");
        callEmployeeButton.getStyle().set("color", "white");

        VerticalLayout actionPanel = new VerticalLayout(totalSection, payButton, callEmployeeButton);
        actionPanel.setAlignItems(FlexComponent.Alignment.STRETCH);
        actionPanel.setWidth("300px");
        actionPanel.addClassName("action-panel");

        // Main content layout
        HorizontalLayout content = new HorizontalLayout(itemGrid, actionPanel);
        content.setSizeFull();
        content.setFlexGrow(1, itemGrid);
        content.addClassName("checkout-content");

        // Hidden barcode input
        barcodeInput = new TextField();
        barcodeInput.addClassName("barcode-input");
        barcodeInput.setPlaceholder("Scan barcode...");
        barcodeInput.setWidthFull();
        barcodeInput.addKeyPressListener(Key.ENTER, e -> handleBarcodeScan());
        barcodeInput.setAutofocus(true);

        add(header, barcodeInput, content);
        setFlexGrow(1, content);
    }

    void handleBarcodeScan() {
        String barcode = barcodeInput.getValue().trim();
        barcodeInput.clear();

        if (barcode.isEmpty()) {
            return;
        }

        if (paymentMode) {
            // Ignore product scans during payment mode
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

        // Check if product already in list - increment quantity (BR-02)
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
        payButton.setEnabled(true);
        barcodeInput.focus();
    }

    private void updateTotal() {
        BigDecimal total = items.stream()
                .map(TransactionItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText(formatPrice(total));
    }

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

            // Complete transaction (BR-06)
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
            resetView();
        });
        doneButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        VerticalLayout content = new VerticalLayout(message, receiptNote);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        confirmDialog.add(content);
        confirmDialog.getFooter().add(doneButton);

        confirmDialog.open();
    }

    private void resetView() {
        items.clear();
        currentTransaction = new Transaction();
        itemGrid.getDataProvider().refreshAll();
        updateTotal();
        payButton.setEnabled(false);
        barcodeInput.clear();
        barcodeInput.focus();
    }

    private String formatPrice(BigDecimal price) {
        return "€" + price.setScale(2).toPlainString();
    }
}
