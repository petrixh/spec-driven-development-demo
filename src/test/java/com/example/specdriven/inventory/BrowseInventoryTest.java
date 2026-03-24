package com.example.specdriven.inventory;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEventRepository;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BrowseInventoryTest extends SpringBrowserlessTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @BeforeEach
    void setUp() {
        stockEventRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.save(new Product("Widget A", "WDG-001", "Widgets", 9.99, 10, 50));   // In Stock
        productRepository.save(new Product("Widget B", "WDG-002", "Widgets", 14.99, 5, 3));    // Low Stock
        productRepository.save(new Product("Gadget X", "GDG-001", "Gadgets", 24.99, 8, 0));    // Out of Stock
        productRepository.save(new Product("Gadget Y", "GDG-002", "Gadgets", 19.99, 15, 15));  // Low Stock (at reorder point)
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void gridDisplaysAllProducts() {
        navigate(InventoryView.class);

        Grid<Product> grid = $(Grid.class).single();
        assertEquals(4, grid.getGenericDataView().getItems().count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void searchFiltersByNameOrSku() {
        navigate(InventoryView.class);

        TextField searchField = $(TextField.class).single();
        test(searchField).setValue("Widget");

        Grid<Product> grid = $(Grid.class).single();
        assertEquals(2, grid.getGenericDataView().getItems().count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void searchFiltersBySku() {
        navigate(InventoryView.class);

        TextField searchField = $(TextField.class).single();
        test(searchField).setValue("GDG");

        Grid<Product> grid = $(Grid.class).single();
        assertEquals(2, grid.getGenericDataView().getItems().count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SuppressWarnings("unchecked")
    void categoryFilterFiltersGrid() {
        navigate(InventoryView.class);

        ComboBox<String> categoryFilter = $(ComboBox.class).single();
        test(categoryFilter).selectItem("Widgets");

        Grid<Product> grid = $(Grid.class).single();
        assertEquals(2, grid.getGenericDataView().getItems().count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void lowStockProductsGetWarningBadge() {
        Product lowStock = new Product("Low", "L-001", "Test", 1.0, 10, 5);
        Span badge = InventoryView.createStatusBadge(lowStock);
        assertTrue(badge.getClassNames().contains("stock-badge-warning"));
        assertEquals("Low Stock", badge.getText());

        // At reorder point also counts as low stock
        Product atReorder = new Product("At", "A-001", "Test", 1.0, 10, 10);
        Span badge2 = InventoryView.createStatusBadge(atReorder);
        assertTrue(badge2.getClassNames().contains("stock-badge-warning"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void outOfStockProductsGetErrorBadge() {
        Product outOfStock = new Product("Empty", "E-001", "Test", 1.0, 5, 0);
        Span badge = InventoryView.createStatusBadge(outOfStock);
        assertTrue(badge.getClassNames().contains("stock-badge-error"));
        assertEquals("Out of Stock", badge.getText());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void inStockProductsGetSuccessBadge() {
        Product inStock = new Product("Full", "F-001", "Test", 1.0, 5, 50);
        Span badge = InventoryView.createStatusBadge(inStock);
        assertTrue(badge.getClassNames().contains("stock-badge-success"));
        assertEquals("In Stock", badge.getText());
    }

    @Test
    @WithMockUser(roles = "USER")
    @SuppressWarnings("unchecked")
    void nonAdminUserCanAccessInventory() {
        navigate(InventoryView.class);

        Grid<Product> grid = $(Grid.class).single();
        assertEquals(4, grid.getGenericDataView().getItems().count());
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessInventory() {
        assertThrows(Exception.class, () -> navigate(InventoryView.class));
    }
}
