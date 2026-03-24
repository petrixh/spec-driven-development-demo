package com.example.specdriven.dashboard;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.StockEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.time.format.DateTimeFormatter;

@Route("")
@PageTitle("Dashboard")
@PermitAll
public class DashboardView extends VerticalLayout {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final DashboardService dashboardService;

    public DashboardView(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
        addClassName("view-content");
        setSizeFull();

        add(new H1("Dashboard"));
        add(createSummaryCards());
        add(createLowStockAlerts());
        add(createRecentActivity());
    }

    private FlexLayout createSummaryCards() {
        FlexLayout cards = new FlexLayout();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.getStyle().set("gap", "var(--vaadin-gap-m)");

        cards.add(createCard("Total Products", String.valueOf(dashboardService.getTotalProducts()), null));
        cards.add(createCard("Total Stock Value", String.format("$%,.2f", dashboardService.getTotalStockValue()), null));
        cards.add(createCard("Low-Stock Items",
                String.valueOf(dashboardService.getLowStockCount()),
                dashboardService.getLowStockCount() > 0 ? "card-value-warning" : null));
        cards.add(createCard("Out-of-Stock Items",
                String.valueOf(dashboardService.getOutOfStockCount()),
                dashboardService.getOutOfStockCount() > 0 ? "card-value-error" : null));

        return cards;
    }

    private Div createCard(String title, String value, String valueClass) {
        Div card = new Div();
        card.addClassName("summary-card");

        Paragraph titleP = new Paragraph(title);
        titleP.addClassName("card-title");

        Paragraph valueP = new Paragraph(value);
        valueP.addClassName("card-value");
        if (valueClass != null) {
            valueP.addClassName(valueClass);
        }

        card.add(titleP, valueP);
        return card;
    }

    private VerticalLayout createLowStockAlerts() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);

        section.add(new H2("Low-Stock Alerts"));

        var alerts = dashboardService.getLowStockAlerts();
        if (alerts.isEmpty()) {
            section.add(new Paragraph("No low-stock items."));
        } else {
            Grid<Product> grid = new Grid<>(Product.class, false);
            grid.addColumn(Product::getName).setHeader("Name");
            grid.addColumn(Product::getSku).setHeader("SKU");
            grid.addColumn(Product::getCurrentStock).setHeader("Current Stock");
            grid.addColumn(Product::getReorderPoint).setHeader("Reorder Point");
            grid.setItems(alerts);
            grid.setAllRowsVisible(true);
            section.add(grid);
        }

        return section;
    }

    private VerticalLayout createRecentActivity() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);

        section.add(new H2("Recent Activity"));

        var activity = dashboardService.getRecentActivity();
        if (activity.isEmpty()) {
            section.add(new Paragraph("No recent activity."));
        } else {
            Grid<StockEvent> grid = new Grid<>(StockEvent.class, false);
            grid.addColumn(e -> e.getTimestamp().format(TIME_FORMAT)).setHeader("Timestamp");
            grid.addColumn(e -> e.getProduct().getName()).setHeader("Product");
            grid.addColumn(e -> e.getType().name()).setHeader("Event Type");
            grid.addColumn(StockEvent::getQuantity).setHeader("Quantity");
            grid.addColumn(e -> e.getReason() != null ? e.getReason() : "").setHeader("Reference/Reason");
            grid.setItems(activity);
            grid.setAllRowsVisible(true);
            section.add(grid);
        }

        return section;
    }
}
