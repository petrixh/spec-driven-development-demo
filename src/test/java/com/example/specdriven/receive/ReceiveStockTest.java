package com.example.specdriven.receive;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReceiveStockTest extends SpringBrowserlessTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @BeforeEach
    void setUp() {
        stockEventRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.save(new Product("Widget A", "WDG-001", "Widgets", 9.99, 10, 50));
    }

    @Test
    @WithMockUser(roles = "USER")
    @SuppressWarnings("unchecked")
    void receiveStockIncreasesProductStock() {
        navigate(ReceiveStockView.class);

        Product product = productRepository.findAll().getFirst();

        ComboBox<Product> productSelector = $(ComboBox.class).single();
        test(productSelector).selectItem("Widget A (WDG-001)");

        IntegerField qty = $(IntegerField.class).single();
        test(qty).setValue(25);

        test($(Button.class).withText("Receive").single()).click();

        Product updated = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(75, updated.getCurrentStock());
    }

    @Test
    @WithMockUser(roles = "USER")
    @SuppressWarnings("unchecked")
    void successNotificationShowsProductAndNewLevel() {
        navigate(ReceiveStockView.class);

        Product product = productRepository.findAll().getFirst();

        ComboBox<Product> productSelector = $(ComboBox.class).single();
        test(productSelector).selectItem("Widget A (WDG-001)");

        IntegerField qty = $(IntegerField.class).single();
        test(qty).setValue(10);

        test($(Button.class).withText("Receive").single()).click();

        assertTrue($(Notification.class).exists());
        Notification notification = $(Notification.class).last();
        String text = test(notification).getText();
        assertTrue(text.contains("Widget A"));
        assertTrue(text.contains("60"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void submitWithoutProductShowsValidation() {
        navigate(ReceiveStockView.class);

        IntegerField qty = $(IntegerField.class).single();
        test(qty).setValue(10);

        test($(Button.class).withText("Receive").single()).click();

        // Product selector should be invalid
        ComboBox<?> productSelector = $(ComboBox.class).single();
        assertTrue(productSelector.isInvalid());
    }

    @Test
    @WithMockUser(roles = "USER")
    @SuppressWarnings("unchecked")
    void stockEventIsCreated() {
        navigate(ReceiveStockView.class);

        Product product = productRepository.findAll().getFirst();

        ComboBox<Product> productSelector = $(ComboBox.class).single();
        test(productSelector).selectItem("Widget A (WDG-001)");

        IntegerField qty = $(IntegerField.class).single();
        test(qty).setValue(5);

        test($(Button.class).withText("Receive").single()).click();

        var events = stockEventRepository.findTop10ByOrderByTimestampDesc();
        assertEquals(1, events.size());
        assertEquals(StockEventType.RECEIVED, events.getFirst().getType());
        assertEquals(5, events.getFirst().getQuantity());
    }

    @Test
    @WithMockUser(roles = "USER")
    @SuppressWarnings("unchecked")
    void formResetsAfterSuccessfulSubmission() {
        navigate(ReceiveStockView.class);

        Product product = productRepository.findAll().getFirst();

        ComboBox<Product> productSelector = $(ComboBox.class).single();
        test(productSelector).selectItem("Widget A (WDG-001)");

        IntegerField qty = $(IntegerField.class).single();
        test(qty).setValue(5);

        TextField ref = $(TextField.class).single();
        test(ref).setValue("PO-123");

        test($(Button.class).withText("Receive").single()).click();

        // Form should be cleared
        assertTrue(productSelector.isEmpty());
        assertTrue(qty.isEmpty());
        assertTrue(ref.isEmpty());
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccess() {
        assertThrows(Exception.class, () -> navigate(ReceiveStockView.class));
    }
}
