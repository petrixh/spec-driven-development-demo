package com.example.specdriven.admin;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
class ProductAdminTest extends SpringBrowserlessTest {

    private ProductAdminView view;

    @BeforeEach
    void setUp() {
        view = navigate(ProductAdminView.class);
    }

    @Test
    void adminCanSeeProductGrid() {
        assertNotNull(view.productGrid);
        // Sample data should be loaded
        assertTrue(view.productGrid.getListDataView().getItemCount() > 0);
    }

    @Test
    void adminCanAddNewProduct() {
        long countBefore = view.productGrid.getListDataView().getItemCount();

        // Click Add Product button
        Button addBtn = $(Button.class).withText("Add Product").single();
        test(addBtn).click();

        // Fill in the form
        view.barcodeField.setValue("NEW_BARCODE_123");
        view.nameField.setValue("Test Product");
        view.priceField.setValue(9.99);

        view.saveProduct();

        view.refreshGrid();
        long countAfter = view.productGrid.getListDataView().getItemCount();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void addingDuplicateBarcodeShowsError() {
        // Click Add Product
        Button addBtn = $(Button.class).withText("Add Product").single();
        test(addBtn).click();

        // Use an existing barcode
        view.barcodeField.setValue("5000159484695"); // Coca-Cola barcode
        view.nameField.setValue("Duplicate Product");
        view.priceField.setValue(1.00);

        view.saveProduct();

        // Should show error notification
        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("already exists"));
    }

    @Test
    void validationRejectsBlankName() {
        Button addBtn = $(Button.class).withText("Add Product").single();
        test(addBtn).click();

        view.barcodeField.setValue("VALID_BARCODE");
        view.nameField.setValue("");
        view.priceField.setValue(1.00);

        view.saveProduct();

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("Name is required"));
    }

    @Test
    void validationRejectsZeroPrice() {
        Button addBtn = $(Button.class).withText("Add Product").single();
        test(addBtn).click();

        view.barcodeField.setValue("VALID_BARCODE_2");
        view.nameField.setValue("Some Product");
        view.priceField.setValue(0.0);

        view.saveProduct();

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("Price must be greater than zero"));
    }
}
