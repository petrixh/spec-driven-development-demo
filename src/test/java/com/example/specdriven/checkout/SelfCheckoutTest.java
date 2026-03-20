package com.example.specdriven.checkout;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SelfCheckoutTest extends SpringBrowserlessTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private SelfCheckoutView view;

    // Known barcodes from SampleDataInitializer
    private static final String COCA_COLA_BARCODE = "5000159484695";
    private static final String NUTELLA_BARCODE = "3017620422003";
    private static final String UNKNOWN_BARCODE = "9999999999999";
    private static final String VALID_CARD = "CARD001";

    @BeforeEach
    void setUp() {
        view = navigate(SelfCheckoutView.class);
    }

    private void scanBarcode(String barcode) {
        view.barcodeInput.setValue(barcode);
        view.handleBarcodeScan();
    }

    @Test
    void scanKnownBarcodeAddsProductToItemList() {
        scanBarcode(COCA_COLA_BARCODE);

        assertEquals(1, view.items.size());
        assertEquals("Coca-Cola 1.5L", view.items.get(0).getProduct().getName());
    }

    @Test
    void scanUnknownBarcodeShowsErrorAndDoesNotAdd() {
        scanBarcode(UNKNOWN_BARCODE);

        assertEquals(0, view.items.size());

        Notification notification = $(Notification.class).single();
        assertTrue(test(notification).getText().contains("Unknown product"));
    }

    @Test
    void scanSameProductTwiceIncrementsQuantity() {
        scanBarcode(COCA_COLA_BARCODE);
        scanBarcode(COCA_COLA_BARCODE);

        assertEquals(1, view.items.size());
        assertEquals(2, view.items.get(0).getQuantity());
    }

    @Test
    void runningTotalIsCorrectAfterMultipleProducts() {
        scanBarcode(COCA_COLA_BARCODE); // 1.89
        scanBarcode(NUTELLA_BARCODE);   // 4.99
        scanBarcode(COCA_COLA_BARCODE); // 1.89 again → qty 2

        // Total should be 1.89*2 + 4.99 = 8.77
        assertEquals("€8.77", view.totalLabel.getText());
    }

    @Test
    void payButtonIsDisabledWhenNoItems() {
        assertFalse(view.payButton.isEnabled());
    }

    @Test
    void payButtonIsEnabledAfterScanningProduct() {
        scanBarcode(COCA_COLA_BARCODE);
        assertTrue(view.payButton.isEnabled());
    }

    @Test
    void pressingPayShowsPaymentDialog() {
        scanBarcode(COCA_COLA_BARCODE);
        test(view.payButton).click();

        Dialog paymentDialog = $(Dialog.class).single();
        assertTrue(paymentDialog.isOpened());
    }

    @Test
    void cancelPaymentReturnsToScanMode() {
        scanBarcode(COCA_COLA_BARCODE);
        test(view.payButton).click();
        assertTrue(view.paymentMode);

        Dialog paymentDialog = $(Dialog.class).single();
        Button cancelButton = $(Button.class, paymentDialog)
                .withText("Cancel")
                .single();
        test(cancelButton).click();

        assertFalse(view.paymentMode);
    }

    @Test
    void productScansAreIgnoredDuringPaymentMode() {
        scanBarcode(COCA_COLA_BARCODE);
        test(view.payButton).click();

        scanBarcode(NUTELLA_BARCODE);

        assertEquals(1, view.items.size());
    }

    @Test
    void totalStartsAtZero() {
        assertEquals("€0.00", view.totalLabel.getText());
    }
}
