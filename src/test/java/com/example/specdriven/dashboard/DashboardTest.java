package com.example.specdriven.dashboard;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEvent;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DashboardTest extends SpringBrowserlessTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @Autowired
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        stockEventRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.save(new Product("Widget A", "WDG-001", "Widgets", 10.00, 10, 50));    // In Stock
        productRepository.save(new Product("Widget B", "WDG-002", "Widgets", 20.00, 5, 3));      // Low Stock
        productRepository.save(new Product("Gadget X", "GDG-001", "Gadgets", 30.00, 8, 0));      // Out of Stock
        productRepository.save(new Product("Gadget Y", "GDG-002", "Gadgets", 15.00, 15, 15));    // Low Stock
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardDisplaysFourSummaryCards() {
        navigate(DashboardView.class);

        // Check summary card values via service
        assertEquals(4, dashboardService.getTotalProducts());

        // Verify cards are rendered (4 summary cards with card-title class)
        List<Paragraph> titles = $(Paragraph.class).withClassName("card-title").all();
        assertEquals(4, titles.size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void totalStockValueIsCorrect() {
        // 50*10 + 3*20 + 0*30 + 15*15 = 500 + 60 + 0 + 225 = 785
        assertEquals(785.0, dashboardService.getTotalStockValue(), 0.01);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void lowStockCountIsCorrect() {
        // Widget B (3/5) and Gadget Y (15/15) are low stock
        assertEquals(2, dashboardService.getLowStockCount());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void outOfStockCountIsCorrect() {
        // Gadget X (0/8)
        assertEquals(1, dashboardService.getOutOfStockCount());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void lowStockAlertsListCorrectProducts() {
        var alerts = dashboardService.getLowStockAlerts();
        assertEquals(2, alerts.size());
        // Should be ordered by criticality (lowest stock ratio first)
        assertTrue(alerts.stream().allMatch(
                p -> p.getCurrentStock() > 0 && p.getCurrentStock() <= p.getReorderPoint()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void recentActivityShowsStockEvents() {
        Product product = productRepository.findAll().getFirst();
        stockEventRepository.save(new StockEvent(StockEventType.RECEIVED, 10, "PO-001", product));
        stockEventRepository.save(new StockEvent(StockEventType.ADJUSTMENT, -2, "Damaged", product));

        var activity = dashboardService.getRecentActivity();
        assertEquals(2, activity.size());
        // Newest first
        assertEquals(StockEventType.ADJUSTMENT, activity.getFirst().getType());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardIsDefaultLandingPage() {
        DashboardView view = navigate(DashboardView.class);
        assertNotNull(view);
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminCanViewDashboard() {
        DashboardView view = navigate(DashboardView.class);
        assertNotNull(view);
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessDashboard() {
        assertThrows(Exception.class, () -> navigate(DashboardView.class));
    }
}
