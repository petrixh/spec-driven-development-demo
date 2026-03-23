package com.example.specdriven.stock;

import com.example.specdriven.layout.MainLayout;
import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "receive", layout = MainLayout.class)
@PageTitle("Receive Stock — Stash.log")
@PermitAll
public class ReceiveStockView extends VerticalLayout {

    private final StockService stockService;
    private final ProductRepository productRepository;

    private final ComboBox<Product> productField = new ComboBox<>("Product");
    private final IntegerField quantityField = new IntegerField("Quantity");
    private final TextField referenceField = new TextField("Reference (optional)");
    private final Button receiveButton = new Button("Receive");

    public ReceiveStockView(StockService stockService, ProductRepository productRepository) {
        this.stockService = stockService;
        this.productRepository = productRepository;

        setSizeFull();
        setMaxWidth("600px");

        configureFields();

        H1 title = new H1("Receive Stock");

        FormLayout form = new FormLayout();
        form.add(productField, quantityField, referenceField);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        receiveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        receiveButton.addClickListener(event -> receiveStock());

        add(title, form, receiveButton);
    }

    private void configureFields() {
        productField.setItems(productRepository.findAll());
        productField.setItemLabelGenerator(p -> p.getName() + " (" + p.getSku() + ")");
        productField.setRequiredIndicatorVisible(true);
        productField.setWidthFull();

        quantityField.setMin(1);
        quantityField.setValue(1);
        quantityField.setStepButtonsVisible(true);
        quantityField.setRequiredIndicatorVisible(true);
        quantityField.setWidthFull();

        referenceField.setPlaceholder("e.g., PO-12345 or Supplier name");
        referenceField.setWidthFull();
    }

    private void receiveStock() {
        Product product = productField.getValue();
        Integer quantity = quantityField.getValue();

        if (product == null) {
            productField.setInvalid(true);
            productField.setErrorMessage("Please select a product");
            return;
        }
        if (quantity == null || quantity < 1) {
            quantityField.setInvalid(true);
            quantityField.setErrorMessage("Quantity must be at least 1");
            return;
        }

        try {
            String reference = referenceField.getValue();
            Product updated = stockService.receiveStock(product, quantity,
                    reference != null && !reference.isBlank() ? reference : null);

            Notification.show(
                    "Received " + quantity + " × " + updated.getName()
                            + " — new stock level: " + updated.getCurrentStock(),
                    5000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            clearForm();
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 0, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void clearForm() {
        productField.clear();
        productField.setInvalid(false);
        quantityField.setValue(1);
        quantityField.setInvalid(false);
        referenceField.clear();
        // Refresh product list to show updated stock
        productField.setItems(productRepository.findAll());
    }
}
