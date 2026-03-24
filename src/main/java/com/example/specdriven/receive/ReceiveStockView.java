package com.example.specdriven.receive;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
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

@Route("receive")
@PageTitle("Receive Stock")
@PermitAll
public class ReceiveStockView extends VerticalLayout {

    private final ReceiveStockService receiveStockService;

    private final ComboBox<Product> productSelector = new ComboBox<>("Product");
    private final IntegerField quantity = new IntegerField("Quantity");
    private final TextField reference = new TextField("Reference (optional)");
    private final Button receiveButton = new Button("Receive");

    public ReceiveStockView(ReceiveStockService receiveStockService, ProductRepository productRepository) {
        this.receiveStockService = receiveStockService;
        addClassName("view-content");

        H1 title = new H1("Receive Stock");

        productSelector.setItems(productRepository.findAll());
        productSelector.setItemLabelGenerator(p -> p.getName() + " (" + p.getSku() + ")");
        productSelector.setRequiredIndicatorVisible(true);
        productSelector.setWidthFull();

        quantity.setMin(1);
        quantity.setRequiredIndicatorVisible(true);
        quantity.setWidthFull();

        reference.setWidthFull();

        receiveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        receiveButton.addClickListener(e -> receiveStock());

        FormLayout form = new FormLayout();
        form.add(productSelector, quantity, reference);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setMaxWidth("500px");

        add(title, form, receiveButton);
    }

    private void receiveStock() {
        Product product = productSelector.getValue();
        Integer qty = quantity.getValue();

        if (product == null) {
            productSelector.setInvalid(true);
            productSelector.setErrorMessage("Please select a product");
            return;
        }

        if (qty == null || qty < 1) {
            quantity.setInvalid(true);
            quantity.setErrorMessage("Quantity must be at least 1");
            return;
        }

        Product updated = receiveStockService.receiveStock(product, qty, reference.getValue());

        Notification.show(
                String.format("Received %d units of %s. New stock level: %d",
                        qty, updated.getName(), updated.getCurrentStock()),
                3000, Notification.Position.BOTTOM_STRETCH)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Reset form
        productSelector.clear();
        quantity.clear();
        reference.clear();
        productSelector.setInvalid(false);
        quantity.setInvalid(false);
    }
}
