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
class CustomerAdminTest extends SpringBrowserlessTest {

    private CustomerAdminView view;

    @BeforeEach
    void setUp() {
        view = navigate(CustomerAdminView.class);
    }

    @Test
    void adminCanSeeCustomerGrid() {
        assertNotNull(view.customerGrid);
        assertTrue(view.customerGrid.getListDataView().getItemCount() > 0);
    }

    @Test
    void adminCanAddNewCustomer() {
        long countBefore = view.customerGrid.getListDataView().getItemCount();

        Button addBtn = $(Button.class).withText("Add Customer").single();
        test(addBtn).click();

        view.nameField.setValue("New Customer");
        view.cardNumberField.setValue("NEWCARD999");
        view.saveCustomer();
        view.refreshGrid();

        long countAfter = view.customerGrid.getListDataView().getItemCount();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void addingDuplicateCardNumberShowsError() {
        Button addBtn = $(Button.class).withText("Add Customer").single();
        test(addBtn).click();

        // CARD001 already exists from sample data
        view.nameField.setValue("Duplicate Customer");
        view.cardNumberField.setValue("CARD001");
        view.saveCustomer();

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("already exists"));
    }

    @Test
    void validationRejectsBlankName() {
        Button addBtn = $(Button.class).withText("Add Customer").single();
        test(addBtn).click();

        view.nameField.setValue("");
        view.cardNumberField.setValue("SOMECARD");
        view.saveCustomer();

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("Name is required"));
    }

    @Test
    void validationRejectsBlankCardNumber() {
        Button addBtn = $(Button.class).withText("Add Customer").single();
        test(addBtn).click();

        view.nameField.setValue("Some Customer");
        view.cardNumberField.setValue("");
        view.saveCustomer();

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("Card number is required"));
    }
}
