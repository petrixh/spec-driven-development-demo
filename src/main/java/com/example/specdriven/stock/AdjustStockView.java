package com.example.specdriven.stock;

import com.example.specdriven.layout.MainLayout;
import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "adjust", layout = MainLayout.class)
@PageTitle("Adjust Stock — Stash.log")
@RolesAllowed("ADMIN")
public class AdjustStockView extends VerticalLayout {

    private final StockService stockService;
    private final ProductRepository productRepository;

    private final ComboBox<Product> productField = new ComboBox<>("Product");
    private final Span currentStockLabel = new Span();
    private final IntegerField quantityField = new IntegerField("Adjustment Quantity");
    private final TextArea reasonField = new TextArea("Reason");
    private final Button adjustButton = new Button("Apply Adjustment");

    public AdjustStockView(StockService stockService, ProductRepository productRepository) {
        this.stockService = stockService;
        this.productRepository = productRepository;

        setSizeFull();
        setMaxWidth("600px");

        configureFields();

        H1 title = new H1("Adjust Stock");

        FormLayout form = new FormLayout();
        form.add(productField, currentStockLabel, quantityField, reasonField);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        adjustButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        adjustButton.addClickListener(event -> adjustStock());

        add(title, form, adjustButton);
    }

    private void configureFields() {
        productField.setItems(productRepository.findAll());
        productField.setItemLabelGenerator(p -> p.getName() + " (" + p.getSku() + ")");
        productField.setRequiredIndicatorVisible(true);
        productField.setWidthFull();
        productField.addValueChangeListener(event -> {
            Product p = event.getValue();
            if (p != null) {
                currentStockLabel.setText("Current stock: " + p.getCurrentStock());
            } else {
                currentStockLabel.setText("");
            }
        });

        currentStockLabel.getStyle().set("font-weight", "bold");

        quantityField.setRequiredIndicatorVisible(true);
        quantityField.setStepButtonsVisible(true);
        quantityField.setHelperText("Positive to add, negative to subtract");
        quantityField.setWidthFull();

        reasonField.setRequiredIndicatorVisible(true);
        reasonField.setPlaceholder("e.g., Damaged goods, Cycle count correction");
        reasonField.setMinLength(3);
        reasonField.setWidthFull();
    }

    private void adjustStock() {
        Product product = productField.getValue();
        Integer quantity = quantityField.getValue();
        String reason = reasonField.getValue();

        if (product == null) {
            productField.setInvalid(true);
            productField.setErrorMessage("Please select a product");
            return;
        }
        if (quantity == null || quantity == 0) {
            quantityField.setInvalid(true);
            quantityField.setErrorMessage("Quantity must be non-zero");
            return;
        }
        if (reason == null || reason.trim().length() < 3) {
            reasonField.setInvalid(true);
            reasonField.setErrorMessage("Reason must be at least 3 characters");
            return;
        }

        try {
            int oldStock = product.getCurrentStock();
            Product updated = stockService.adjustStock(product, quantity, reason.trim());

            Notification.show(
                    updated.getName() + ": " + oldStock + " → " + updated.getCurrentStock()
                            + " (" + (quantity > 0 ? "+" : "") + quantity + ")",
                    5000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            clearForm();
        } catch (IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 0, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void clearForm() {
        productField.clear();
        productField.setInvalid(false);
        quantityField.clear();
        quantityField.setInvalid(false);
        reasonField.clear();
        reasonField.setInvalid(false);
        currentStockLabel.setText("");
        productField.setItems(productRepository.findAll());
    }
}
