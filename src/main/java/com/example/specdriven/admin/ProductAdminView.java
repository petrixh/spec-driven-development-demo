package com.example.specdriven.admin;

import com.example.specdriven.checkout.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;

@Route("admin/products")
@RolesAllowed("ADMIN")
public class ProductAdminView extends VerticalLayout {

    private final ProductService productService;
    private final OpenFoodFactsService openFoodFactsService;

    final Grid<Product> productGrid;
    final TextField barcodeField;
    final TextField nameField;
    final NumberField priceField;
    final Button saveButton;
    final Button cancelButton;
    final Button lookupButton;
    private final Button addButton;
    private final VerticalLayout formLayout;

    private Product editingProduct;

    public ProductAdminView(ProductService productService, OpenFoodFactsService openFoodFactsService) {
        this.productService = productService;
        this.openFoodFactsService = openFoodFactsService;

        H2 header = new H2("Product Administration");

        // Product grid
        productGrid = new Grid<>();
        productGrid.addColumn(Product::getBarcode).setHeader("Barcode").setFlexGrow(2);
        productGrid.addColumn(Product::getName).setHeader("Name").setFlexGrow(3);
        productGrid.addColumn(p -> "€" + p.getPrice().setScale(2).toPlainString()).setHeader("Price").setFlexGrow(1);
        productGrid.addComponentColumn(product -> {
            Button editBtn = new Button("Edit", e -> editProduct(product));
            Button deleteBtn = new Button("Delete", e -> confirmDelete(product));
            deleteBtn.addThemeVariants(ButtonVariant.AURA_DANGER);
            HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Actions").setFlexGrow(2);
        productGrid.setHeight("400px");

        // Form
        barcodeField = new TextField("Barcode");
        barcodeField.setWidthFull();

        nameField = new TextField("Name");
        nameField.setWidthFull();

        priceField = new NumberField("Price (€)");
        priceField.setWidthFull();
        priceField.setMin(0.01);
        priceField.setStep(0.01);

        lookupButton = new Button("Lookup", e -> lookupBarcode());
        HorizontalLayout barcodeRow = new HorizontalLayout(barcodeField, lookupButton);
        barcodeRow.setAlignItems(Alignment.BASELINE);
        barcodeRow.setWidthFull();
        barcodeField.setWidth("100%");

        saveButton = new Button("Save", e -> saveProduct());
        saveButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        cancelButton = new Button("Cancel", e -> clearForm());

        HorizontalLayout formButtons = new HorizontalLayout(saveButton, cancelButton);

        addButton = new Button("Add Product", e -> startNewProduct());
        addButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        formLayout = new VerticalLayout(barcodeRow, nameField, priceField, formButtons);
        formLayout.setWidth("400px");
        formLayout.setPadding(false);
        formLayout.setVisible(false);
        formLayout.addClassName("product-form");

        HorizontalLayout content = new HorizontalLayout(productGrid, formLayout);
        content.setSizeFull();
        content.setFlexGrow(1, productGrid);

        add(header, addButton, content);
        setSizeFull();
        refreshGrid();
    }

    private void lookupBarcode() {
        String barcode = barcodeField.getValue().trim();
        if (barcode.isEmpty()) {
            return;
        }
        openFoodFactsService.lookupProductName(barcode).ifPresent(name -> {
            nameField.setValue(name);
            Notification.show("Product name found: " + name, 3000, Notification.Position.MIDDLE);
        });
    }

    private void startNewProduct() {
        editingProduct = null;
        barcodeField.setValue("");
        barcodeField.setReadOnly(false);
        nameField.setValue("");
        priceField.setValue(null);
        showForm(true);
    }

    private void editProduct(Product product) {
        editingProduct = product;
        barcodeField.setValue(product.getBarcode());
        barcodeField.setReadOnly(true);
        nameField.setValue(product.getName());
        priceField.setValue(product.getPrice().doubleValue());
        showForm(true);
    }

    private void showForm(boolean visible) {
        formLayout.setVisible(visible);
    }

    void saveProduct() {
        String barcode = barcodeField.getValue().trim();
        String name = nameField.getValue().trim();
        Double price = priceField.getValue();

        if (barcode.isEmpty()) {
            Notification.show("Barcode is required", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        if (name.isEmpty()) {
            Notification.show("Name is required", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        if (price == null || price <= 0) {
            Notification.show("Price must be greater than zero", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            Product product;
            if (editingProduct != null) {
                product = editingProduct;
                product.setName(name);
                product.setPrice(BigDecimal.valueOf(price));
            } else {
                product = new Product(barcode, name, BigDecimal.valueOf(price));
            }
            productService.save(product);
            Notification.show("Product saved", 3000, Notification.Position.MIDDLE);
            clearForm();
            refreshGrid();
        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete(Product product) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete Product");
        dialog.setText("Are you sure you want to delete '" + product.getName() + "'?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.addConfirmListener(e -> {
            productService.delete(product);
            Notification.show("Product deleted", 3000, Notification.Position.MIDDLE);
            refreshGrid();
        });
        dialog.open();
    }

    private void clearForm() {
        editingProduct = null;
        barcodeField.setValue("");
        barcodeField.setReadOnly(false);
        nameField.setValue("");
        priceField.setValue(null);
        showForm(false);
    }

    void refreshGrid() {
        productGrid.setItems(productService.findAll());
    }
}
