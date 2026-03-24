package com.example.specdriven.products;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEvent;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ManageProductsTest extends SpringBrowserlessTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @BeforeEach
    void setUp() {
        stockEventRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.save(new Product("Test Widget", "TST-001", "Widgets", 9.99, 10, 50));
        productRepository.save(new Product("Test Gadget", "TST-002", "Gadgets", 19.99, 5, 3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProductWithAllRequiredFields() {
        navigate(ManageProductsView.class);

        $(Button.class).withText("Add product").single();
        test($(Button.class).withText("Add product").single()).click();

        test($(TextField.class).withCaption("Name").single()).setValue("New Product");
        test($(TextField.class).withCaption("SKU").single()).setValue("NEW-001");
        test($(TextField.class).withCaption("Category").single()).setValue("New Category");
        test($(NumberField.class).withCaption("Unit Price").single()).setValue(29.99);
        test($(IntegerField.class).withCaption("Reorder Point").single()).setValue(5);

        test($(Button.class).withText("Save").single()).click();

        assertTrue(productRepository.existsBySku("NEW-001"));
        assertTrue($(Notification.class).exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void duplicateSkuShowsValidationError() {
        navigate(ManageProductsView.class);

        test($(Button.class).withText("Add product").single()).click();

        test($(TextField.class).withCaption("Name").single()).setValue("Duplicate");
        test($(TextField.class).withCaption("SKU").single()).setValue("TST-001");
        test($(NumberField.class).withCaption("Unit Price").single()).setValue(5.00);

        test($(Button.class).withText("Save").single()).click();

        TextField skuField = $(TextField.class).withCaption("SKU").single();
        assertTrue(skuField.isInvalid());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void missingMandatoryFieldsPreventsSave() {
        navigate(ManageProductsView.class);

        test($(Button.class).withText("Add product").single()).click();
        test($(Button.class).withText("Save").single()).click();

        assertEquals(2, productRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unitPriceRejectsNonPositiveValues() {
        navigate(ManageProductsView.class);

        test($(Button.class).withText("Add product").single()).click();

        test($(TextField.class).withCaption("Name").single()).setValue("Bad Price");
        test($(TextField.class).withCaption("SKU").single()).setValue("BAD-001");
        // NumberField with min=0.01 rejects 0.0 at the component level,
        // so setting it via tester throws. Verify that behavior:
        NumberField priceField = $(NumberField.class).withCaption("Unit Price").single();
        assertThrows(IllegalArgumentException.class, () -> test(priceField).setValue(0.0));

        // The product should not have been saved
        assertFalse(productRepository.existsBySku("BAD-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void editExistingProduct() {
        navigate(ManageProductsView.class);

        Grid<Product> grid = $(Grid.class).single();
        Product product = productRepository.findAll().getFirst();
        grid.select(product);

        test($(TextField.class).withCaption("Name").single()).setValue("Updated Widget");
        test($(Button.class).withText("Save").single()).click();

        Product updated = productRepository.findById(product.getId()).orElseThrow();
        assertEquals("Updated Widget", updated.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void deleteProductShowsConfirmation() {
        navigate(ManageProductsView.class);

        Grid<Product> grid = $(Grid.class).single();
        Product product = productRepository.findAll().getFirst();
        grid.select(product);

        test($(Button.class).withText("Delete").single()).click();

        ConfirmDialog dialog = $(ConfirmDialog.class).single();
        assertNotNull(dialog);
        test(dialog).confirm();

        assertEquals(1, productRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void productWithStockEventsCannotBeDeleted() {
        Product product = productRepository.findAll().getFirst();
        stockEventRepository.save(new StockEvent(StockEventType.RECEIVED, 10, null, product));

        navigate(ManageProductsView.class);

        Grid<Product> grid = $(Grid.class).single();
        grid.select(product);

        test($(Button.class).withText("Delete").single()).click();

        assertEquals(2, productRepository.count());
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessProducts() {
        assertThrows(Exception.class, () -> navigate(ManageProductsView.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminUserCannotAccessProducts() {
        assertThrows(Exception.class, () -> navigate(ManageProductsView.class));
    }
}
