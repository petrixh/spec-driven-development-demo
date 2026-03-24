package com.example.specdriven.adjust;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
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

@Route("adjust")
@PageTitle("Adjust Stock")
@RolesAllowed("ADMIN")
public class AdjustStockView extends VerticalLayout {

    private final AdjustStockService adjustStockService;

    private final ComboBox<Product> productSelector = new ComboBox<>("Product");
    private final Span currentStockDisplay = new Span();
    private final IntegerField adjustmentQuantity = new IntegerField("Adjustment Quantity");
    private final TextArea reason = new TextArea("Reason");
    private final Button applyButton = new Button("Apply Adjustment");

    public AdjustStockView(AdjustStockService adjustStockService, ProductRepository productRepository) {
        this.adjustStockService = adjustStockService;
        addClassName("view-content");

        H1 title = new H1("Adjust Stock");

        productSelector.setItems(productRepository.findAll());
        productSelector.setItemLabelGenerator(p -> p.getName() + " (" + p.getSku() + ")");
        productSelector.setRequiredIndicatorVisible(true);
        productSelector.setWidthFull();
        productSelector.addValueChangeListener(e -> {
            Product p = e.getValue();
            if (p != null) {
                currentStockDisplay.setText("Current stock: " + p.getCurrentStock());
            } else {
                currentStockDisplay.setText("");
            }
        });

        currentStockDisplay.getStyle()
                .set("font-weight", "var(--aura-font-weight-semibold)")
                .set("font-size", "var(--aura-font-size-l)");

        adjustmentQuantity.setRequiredIndicatorVisible(true);
        adjustmentQuantity.setWidthFull();
        adjustmentQuantity.setHelperText("Positive to add, negative to subtract");

        reason.setRequiredIndicatorVisible(true);
        reason.setWidthFull();
        reason.setMinLength(3);
        reason.setHelperText("At least 3 characters");

        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClickListener(e -> applyAdjustment());

        FormLayout form = new FormLayout();
        form.add(productSelector, currentStockDisplay, adjustmentQuantity, reason);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setMaxWidth("500px");

        add(title, form, applyButton);
    }

    private void applyAdjustment() {
        Product product = productSelector.getValue();
        Integer qty = adjustmentQuantity.getValue();
        String reasonText = reason.getValue();

        // Validate product
        if (product == null) {
            productSelector.setInvalid(true);
            productSelector.setErrorMessage("Please select a product");
            return;
        }

        // Validate quantity (non-zero)
        if (qty == null || qty == 0) {
            adjustmentQuantity.setInvalid(true);
            adjustmentQuantity.setErrorMessage("Quantity must be non-zero");
            return;
        }

        // Validate reason
        if (reasonText == null || reasonText.trim().length() < 3) {
            reason.setInvalid(true);
            reason.setErrorMessage("Reason must be at least 3 characters");
            return;
        }

        // Check below-zero guard
        if (product.getCurrentStock() + qty < 0) {
            adjustmentQuantity.setInvalid(true);
            adjustmentQuantity.setErrorMessage(
                    "Adjustment would bring stock below zero (current: %d)".formatted(product.getCurrentStock()));
            return;
        }

        int oldStock = product.getCurrentStock();

        try {
            Product updated = adjustStockService.adjustStock(product, qty, reasonText.trim());

            Notification.show(
                    String.format("Stock adjusted for %s: %d → %d",
                            updated.getName(), oldStock, updated.getCurrentStock()),
                    3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Reset form
            productSelector.clear();
            adjustmentQuantity.clear();
            reason.clear();
            currentStockDisplay.setText("");
            productSelector.setInvalid(false);
            adjustmentQuantity.setInvalid(false);
            reason.setInvalid(false);

        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
