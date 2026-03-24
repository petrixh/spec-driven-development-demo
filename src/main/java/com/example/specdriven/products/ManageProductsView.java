package com.example.specdriven.products;

import com.example.specdriven.data.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("products")
@PageTitle("Products")
@RolesAllowed("ADMIN")
public class ManageProductsView extends VerticalLayout {

    private final ProductService productService;

    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

    private final TextField name = new TextField("Name");
    private final TextField sku = new TextField("SKU");
    private final TextField category = new TextField("Category");
    private final NumberField unitPrice = new NumberField("Unit Price");
    private final IntegerField reorderPoint = new IntegerField("Reorder Point");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private Product currentProduct;

    public ManageProductsView(ProductService productService) {
        this.productService = productService;
        addClassName("view-content");
        setSizeFull();

        H1 title = new H1("Products");
        Button addButton = new Button("Add product", e -> {
            grid.asSingleSelect().clear();
            editProduct(new Product());
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(title, addButton);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.setWidthFull();
        toolbar.expand(title);

        configureGrid();
        configureForm();

        SplitLayout splitLayout = new SplitLayout(grid, createFormLayout());
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(65);

        add(toolbar, splitLayout);
        setFlexGrow(1, splitLayout);

        refreshGrid();
        clearForm();
    }

    private void configureGrid() {
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setSortable(true);
        grid.addColumn(Product::getCategory).setHeader("Category").setSortable(true);
        grid.addColumn(p -> String.format("$%.2f", p.getUnitPrice())).setHeader("Unit Price").setSortable(true);
        grid.addColumn(Product::getReorderPoint).setHeader("Reorder Point").setSortable(true);
        grid.addColumn(Product::getCurrentStock).setHeader("Current Stock").setSortable(true);
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editProduct(e.getValue());
            } else {
                clearForm();
            }
        });
    }

    private void configureForm() {
        name.setRequiredIndicatorVisible(true);
        sku.setRequiredIndicatorVisible(true);
        unitPrice.setRequiredIndicatorVisible(true);
        unitPrice.setStep(0.01);
        unitPrice.setMin(0.01);
        reorderPoint.setMin(0);
        reorderPoint.setValue(0);

        binder.forField(name)
                .asRequired("Name is required")
                .bind(Product::getName, Product::setName);
        binder.forField(sku)
                .asRequired("SKU is required")
                .bind(Product::getSku, Product::setSku);
        binder.forField(category)
                .bind(Product::getCategory, Product::setCategory);
        binder.forField(unitPrice)
                .asRequired("Unit Price is required")
                .withValidator(v -> v != null && v > 0, "Unit Price must be positive")
                .bind(Product::getUnitPrice, Product::setUnitPrice);
        binder.forField(reorderPoint)
                .bind(Product::getReorderPoint, Product::setReorderPoint);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> saveProduct());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(e -> confirmDelete());

        cancel.addClickListener(e -> {
            grid.asSingleSelect().clear();
            clearForm();
        });
    }

    private VerticalLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, sku, category, unitPrice, reorderPoint);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        HorizontalLayout buttons = new HorizontalLayout(save, delete, cancel);
        buttons.setWidthFull();

        VerticalLayout formWrapper = new VerticalLayout(formLayout, buttons);
        formWrapper.setPadding(true);
        return formWrapper;
    }

    private void editProduct(Product product) {
        currentProduct = product;
        binder.readBean(product);
        delete.setVisible(product.getId() != null);
        save.setEnabled(true);
    }

    private void clearForm() {
        currentProduct = null;
        binder.readBean(new Product());
        reorderPoint.setValue(0);
        delete.setVisible(false);
        save.setEnabled(false);
    }

    private void saveProduct() {
        if (currentProduct == null) {
            return;
        }
        try {
            binder.writeBean(currentProduct);

            if (productService.isSkuDuplicate(currentProduct.getSku(), currentProduct.getId())) {
                sku.setInvalid(true);
                sku.setErrorMessage("SKU already exists");
                return;
            }

            productService.save(currentProduct);
            Notification.show("Product saved.", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            clearForm();
            grid.asSingleSelect().clear();
        } catch (ValidationException e) {
            Notification.show("Please fix validation errors.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete() {
        if (currentProduct == null || currentProduct.getId() == null) {
            return;
        }

        if (productService.hasStockEvents(currentProduct.getId())) {
            Notification.show("Cannot delete a product with stock events.", 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete Product");
        dialog.setText("Are you sure you want to delete \"" + currentProduct.getName() + "\"?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            productService.delete(currentProduct);
            Notification.show("Product deleted.", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            clearForm();
            grid.asSingleSelect().clear();
        });
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(productService.findAll());
    }
}
