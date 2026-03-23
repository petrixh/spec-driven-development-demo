package com.example.specdriven.checkout;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.PasswordField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmployeeModeTest extends SpringBrowserlessTest {

    private SelfCheckoutView view;

    private static final String COCA_COLA_BARCODE = "5000159484695";

    @BeforeEach
    void setUp() {
        view = navigate(SelfCheckoutView.class);
    }

    private void scanBarcode(String barcode) {
        view.barcodeInput.setValue(barcode);
        view.handleBarcodeScan();
    }

    private void enterEmployeeMode() {
        view.openEmployeeCodeDialog();
        Dialog dialog = $(Dialog.class).single();
        PasswordField codeInput = $(PasswordField.class, dialog).single();
        codeInput.setValue(SelfCheckoutView.EMPLOYEE_CODE);
        Button submitButton = $(Button.class, dialog).withText("Submit").single();
        test(submitButton).click();
    }

    @Test
    void callEmployeeButtonIsAlwaysVisible() {
        assertTrue(view.callEmployeeButton.isVisible());
    }

    @Test
    void pressingCallEmployeeShowsCodeInput() {
        view.openEmployeeCodeDialog();
        Dialog dialog = $(Dialog.class).single();
        assertTrue(dialog.isOpened());
        assertTrue($(PasswordField.class, dialog).exists());
    }

    @Test
    void enteringCorrectCodeActivatesEmployeeMode() {
        enterEmployeeMode();

        assertTrue(view.employeeMode);
        assertTrue(view.employeeBadge.isVisible());
        assertTrue(view.paidByCashButton.isVisible());
        assertTrue(view.selectCustomerButton.isVisible());
        assertTrue(view.exitEmployeeModeButton.isVisible());
        assertFalse(view.callEmployeeButton.isVisible());
    }

    @Test
    void enteringWrongCodeShowsError() {
        view.openEmployeeCodeDialog();
        Dialog dialog = $(Dialog.class).single();
        PasswordField codeInput = $(PasswordField.class, dialog).single();
        codeInput.setValue("000000");
        Button submitButton = $(Button.class, dialog).withText("Submit").single();
        test(submitButton).click();

        assertFalse(view.employeeMode);
        // Dialog should still be open
        assertTrue(dialog.isOpened());
        // Error message should be visible
        Span error = $(Span.class, dialog).withClassName("payment-error").single();
        assertTrue(error.isVisible());
    }

    @Test
    void paidByCashOnlyVisibleInEmployeeMode() {
        assertFalse(view.paidByCashButton.isVisible());
        enterEmployeeMode();
        assertTrue(view.paidByCashButton.isVisible());
    }

    @Test
    void paidByCashCompletesTransaction() {
        enterEmployeeMode();
        scanBarcode(COCA_COLA_BARCODE);

        test(view.paidByCashButton).click();

        // Confirmation dialog should appear
        Dialog confirmDialog = $(Dialog.class).single();
        assertTrue(confirmDialog.isOpened());
    }

    @Test
    void viewResetsAfterCashPayment() {
        enterEmployeeMode();
        scanBarcode(COCA_COLA_BARCODE);
        assertEquals(1, view.items.size());

        test(view.paidByCashButton).click();

        Dialog confirmDialog = $(Dialog.class).single();
        Button doneButton = $(Button.class, confirmDialog).withText("Done").single();
        test(doneButton).click();

        assertEquals(0, view.items.size());
        assertFalse(view.employeeMode);
    }

    @Test
    void exitEmployeeModeReturnsToNormalMode() {
        enterEmployeeMode();
        assertTrue(view.employeeMode);

        test(view.exitEmployeeModeButton).click();

        assertFalse(view.employeeMode);
        assertFalse(view.paidByCashButton.isVisible());
        assertFalse(view.selectCustomerButton.isVisible());
        assertTrue(view.callEmployeeButton.isVisible());
    }

    @Test
    void selectCustomerOnlyVisibleInEmployeeMode() {
        assertFalse(view.selectCustomerButton.isVisible());
        enterEmployeeMode();
        assertTrue(view.selectCustomerButton.isVisible());
    }

    @Test
    void selectCustomerOpensCustomerDialog() {
        enterEmployeeMode();
        scanBarcode(COCA_COLA_BARCODE);

        test(view.selectCustomerButton).click();

        Dialog dialog = $(Dialog.class).single();
        assertTrue(dialog.isOpened());
        // Should have a customer grid inside
        assertTrue($(Grid.class, dialog).exists());
    }
}
