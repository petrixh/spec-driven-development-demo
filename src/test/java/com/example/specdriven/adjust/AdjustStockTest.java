package com.example.specdriven.adjust;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdjustStockTest extends SpringBrowserlessTest {

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
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void selectingProductDisplaysCurrentStock() {
        navigate(AdjustStockView.class);

        ComboBox<Product> productSelector = $(ComboBox.class).single();
        test(productSelector).selectItem("Widget A (WDG-001)");

        Span stockDisplay = $(Span.class).withCondition(s -> s.getText().contains("Current stock")).single();
        assertEquals("Current stock: 50", stockDisplay.getText());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void positiveAdjustmentIncreasesStock() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(10);
        test($(TextArea.class).single()).setValue("Cycle count correction");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        Product updated = productRepository.findAll().getFirst();
        assertEquals(60, updated.getCurrentStock());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void negativeAdjustmentDecreasesStock() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(-5);
        test($(TextArea.class).single()).setValue("Damaged goods");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        Product updated = productRepository.findAll().getFirst();
        assertEquals(45, updated.getCurrentStock());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void adjustmentBelowZeroIsRejected() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(-51);
        test($(TextArea.class).single()).setValue("Too much");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        // Stock should be unchanged
        Product unchanged = productRepository.findAll().getFirst();
        assertEquals(50, unchanged.getCurrentStock());

        // Adjustment field should show error
        IntegerField field = $(IntegerField.class).single();
        assertTrue(field.isInvalid());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void missingReasonShowsValidation() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(5);

        test($(Button.class).withText("Apply Adjustment").single()).click();

        TextArea reasonField = $(TextArea.class).single();
        assertTrue(reasonField.isInvalid());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void shortReasonShowsValidation() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(5);
        test($(TextArea.class).single()).setValue("ab");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        TextArea reasonField = $(TextArea.class).single();
        assertTrue(reasonField.isInvalid());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void stockEventIsCreatedWithCorrectType() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(-3);
        test($(TextArea.class).single()).setValue("Inventory correction");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        var events = stockEventRepository.findTop10ByOrderByTimestampDesc();
        assertEquals(1, events.size());
        assertEquals(StockEventType.ADJUSTMENT, events.getFirst().getType());
        assertEquals(-3, events.getFirst().getQuantity());
        assertEquals("Inventory correction", events.getFirst().getReason());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void successNotificationShowsOldAndNewStockLevels() {
        navigate(AdjustStockView.class);

        test($(ComboBox.class).single()).selectItem("Widget A (WDG-001)");
        test($(IntegerField.class).single()).setValue(10);
        test($(TextArea.class).single()).setValue("Recount adjustment");

        test($(Button.class).withText("Apply Adjustment").single()).click();

        assertTrue($(Notification.class).exists());
        String text = test($(Notification.class).last()).getText();
        assertTrue(text.contains("50")); // old stock
        assertTrue(text.contains("60")); // new stock
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccess() {
        assertThrows(Exception.class, () -> navigate(AdjustStockView.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminUserCannotAccess() {
        assertThrows(Exception.class, () -> navigate(AdjustStockView.class));
    }
}
