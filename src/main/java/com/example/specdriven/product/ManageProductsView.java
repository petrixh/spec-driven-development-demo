package com.example.specdriven.product;

import com.example.specdriven.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.math.BigDecimal;

@Route(value = "products", layout = MainLayout.class)
@PageTitle("Products — Stash.log")
@RolesAllowed("ADMIN")
public class ManageProductsView extends VerticalLayout {

    private final ProductService productService;

    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final TextField nameField = new TextField("Name");
    private final TextField skuField = new TextField("SKU");
    private final TextField categoryField = new TextField("Category");
    private final BigDecimalField unitPriceField = new BigDecimalField("Unit Price");
    private final IntegerField reorderPointField = new IntegerField("Reorder Point");
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button addButton = new Button("Add product");

    private final Binder<Product> binder = new BeanValidationBinder<>(Product.class);
    private Product currentProduct;

    public ManageProductsView(ProductService productService) {
        this.productService = productService;
        setSizeFull();

        configureGrid();
        configureForm();
        configureBinder();

        VerticalLayout formLayout = createFormLayout();
        SplitLayout splitLayout = new SplitLayout(createGridLayout(), formLayout);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(65);

        add(splitLayout);
        refreshGrid();
        clearForm();
    }

    private void configureGrid() {
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getCategory).setHeader("Category").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getUnitPrice).setHeader("Unit Price").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getReorderPoint).setHeader("Reorder Point").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getCurrentStock).setHeader("Current Stock").setSortable(true).setAutoWidth(true);
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            Product selected = event.getValue();
            if (selected != null) {
                editProduct(selected);
            } else {
                clearForm();
            }
        });
    }

    private void configureForm() {
        nameField.setRequiredIndicatorVisible(true);
        skuField.setRequiredIndicatorVisible(true);
        unitPriceField.setRequiredIndicatorVisible(true);
        reorderPointField.setValue(0);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> saveProduct());

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> confirmDelete());

        addButton.addClickListener(event -> {
            grid.asSingleSelect().clear();
            clearForm();
            currentProduct = new Product();
            nameField.focus();
        });
    }

    private void configureBinder() {
        binder.forField(nameField)
                .asRequired("Name is required")
                .bind(Product::getName, Product::setName);
        binder.forField(skuField)
                .asRequired("SKU is required")
                .bind(Product::getSku, Product::setSku);
        binder.forField(categoryField)
                .bind(Product::getCategory, Product::setCategory);
        binder.forField(unitPriceField)
                .asRequired("Unit price is required")
                .withValidator(price -> price != null && price.compareTo(BigDecimal.ZERO) > 0,
                        "Unit price must be positive")
                .bind(Product::getUnitPrice, Product::setUnitPrice);
        binder.forField(reorderPointField)
                .withValidator(rp -> rp != null && rp >= 0, "Reorder point must be non-negative")
                .bind(Product::getReorderPoint, Product::setReorderPoint);
    }

    private VerticalLayout createGridLayout() {
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        VerticalLayout layout = new VerticalLayout(toolbar, grid);
        layout.setSizeFull();
        layout.setFlexGrow(1, grid);
        return layout;
    }

    private VerticalLayout createFormLayout() {
        H2 formTitle = new H2("Product Details");
        formTitle.getStyle().set("font-size", "var(--aura-font-size-l)");

        FormLayout form = new FormLayout();
        form.add(nameField, skuField, categoryField, unitPriceField, reorderPointField);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        VerticalLayout layout = new VerticalLayout(formTitle, form, buttons);
        layout.setPadding(true);
        layout.setWidth("100%");
        return layout;
    }

    private void editProduct(Product product) {
        currentProduct = product;
        binder.readBean(product);
        deleteButton.setEnabled(true);
    }

    private void clearForm() {
        currentProduct = null;
        binder.readBean(new Product());
        reorderPointField.setValue(0);
        deleteButton.setEnabled(false);
    }

    private void saveProduct() {
        if (currentProduct == null) {
            currentProduct = new Product();
        }
        try {
            binder.writeBean(currentProduct);
            productService.save(currentProduct);
            Notification.show("Product saved", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            clearForm();
        } catch (ValidationException e) {
            // Binder validation errors are shown inline
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 0, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete() {
        if (currentProduct == null || currentProduct.getId() == null) {
            return;
        }

        if (productService.hasStockEvents(currentProduct)) {
            Notification.show("Cannot delete: product has stock events", 0, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete product");
        dialog.setText("Are you sure you want to delete \"" + currentProduct.getName() + "\"?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            try {
                productService.delete(currentProduct);
                Notification.show("Product deleted", 3000, Notification.Position.BOTTOM_STRETCH)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                refreshGrid();
                clearForm();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 0, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(productService.findAll());
    }
}
