package com.example.specdriven.admin;

import com.example.specdriven.checkout.Customer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/customers", layout = AdminLayout.class)
@RolesAllowed("ADMIN")
public class CustomerAdminView extends VerticalLayout {

    private final CustomerService customerService;

    final Grid<Customer> customerGrid;
    final TextField nameField;
    final TextField cardNumberField;
    final Button saveButton;
    final Button cancelButton;
    private final Button addButton;
    private final VerticalLayout formLayout;

    private Customer editingCustomer;

    public CustomerAdminView(CustomerService customerService) {
        this.customerService = customerService;

        H2 header = new H2("Customer Administration");

        // Customer grid
        customerGrid = new Grid<>();
        customerGrid.addColumn(Customer::getName).setHeader("Name").setFlexGrow(3);
        customerGrid.addColumn(Customer::getCustomerNumber).setHeader("Card Number").setFlexGrow(2);
        customerGrid.addComponentColumn(customer -> {
            Button editBtn = new Button("Edit", e -> editCustomer(customer));
            Button deleteBtn = new Button("Delete", e -> confirmDelete(customer));
            deleteBtn.addThemeVariants(ButtonVariant.AURA_DANGER);
            HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Actions").setFlexGrow(2);
        customerGrid.setHeight("400px");

        // Form
        nameField = new TextField("Name");
        nameField.setWidthFull();

        cardNumberField = new TextField("Card Number");
        cardNumberField.setWidthFull();

        saveButton = new Button("Save", e -> saveCustomer());
        saveButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        cancelButton = new Button("Cancel", e -> clearForm());

        HorizontalLayout formButtons = new HorizontalLayout(saveButton, cancelButton);

        addButton = new Button("Add Customer", e -> startNewCustomer());
        addButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        formLayout = new VerticalLayout(nameField, cardNumberField, formButtons);
        formLayout.setWidth("400px");
        formLayout.setPadding(false);
        formLayout.setVisible(false);

        HorizontalLayout content = new HorizontalLayout(customerGrid, formLayout);
        content.setSizeFull();
        content.setFlexGrow(1, customerGrid);

        add(header, addButton, content);
        setSizeFull();
        refreshGrid();
    }

    private void startNewCustomer() {
        editingCustomer = null;
        nameField.setValue("");
        cardNumberField.setValue("");
        cardNumberField.setReadOnly(false);
        formLayout.setVisible(true);
    }

    private void editCustomer(Customer customer) {
        editingCustomer = customer;
        nameField.setValue(customer.getName());
        cardNumberField.setValue(customer.getCustomerNumber());
        cardNumberField.setReadOnly(true);
        formLayout.setVisible(true);
    }

    void saveCustomer() {
        String name = nameField.getValue().trim();
        String cardNumber = cardNumberField.getValue().trim();

        if (name.isEmpty()) {
            Notification.show("Name is required", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        if (cardNumber.isEmpty()) {
            Notification.show("Card number is required", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            Customer customer;
            if (editingCustomer != null) {
                customer = editingCustomer;
                customer.setName(name);
                customer.setCustomerNumber(cardNumber);
            } else {
                customer = new Customer(name, cardNumber);
            }
            customerService.save(customer);
            Notification.show("Customer saved", 3000, Notification.Position.MIDDLE);
            clearForm();
            refreshGrid();
        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete(Customer customer) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete Customer");
        dialog.setText("Are you sure you want to delete '" + customer.getName() + "'?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.addConfirmListener(e -> {
            customerService.delete(customer);
            Notification.show("Customer deleted", 3000, Notification.Position.MIDDLE);
            refreshGrid();
        });
        dialog.open();
    }

    private void clearForm() {
        editingCustomer = null;
        nameField.setValue("");
        cardNumberField.setValue("");
        cardNumberField.setReadOnly(false);
        formLayout.setVisible(false);
    }

    void refreshGrid() {
        customerGrid.setItems(customerService.findAll());
    }
}
