package com.example.specdriven.dashboard;

import com.example.specdriven.layout.MainLayout;
import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import com.example.specdriven.stock.StockEvent;
import com.example.specdriven.stock.StockEventRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard — Stash.log")
@PermitAll
public class DashboardView extends VerticalLayout {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public DashboardView(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;

        setSizeFull();
        setPadding(true);

        add(new H1("Dashboard"));
        add(createSummaryCards());
        add(createLowStockSection());
        add(createRecentActivitySection());
    }

    private FlexLayout createSummaryCards() {
        List<Product> products = productRepository.findAll();

        long totalProducts = products.size();
        BigDecimal totalValue = products.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getCurrentStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long lowStockCount = products.stream()
                .filter(p -> p.getCurrentStock() > 0 && p.getCurrentStock() <= p.getReorderPoint())
                .count();
        long outOfStockCount = products.stream()
                .filter(p -> p.getCurrentStock() == 0)
                .count();

        FlexLayout cards = new FlexLayout();
        cards.setWidthFull();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.getStyle().set("gap", "var(--vaadin-space-m)");

        cards.add(createCard("Total Products", String.valueOf(totalProducts), null));
        cards.add(createCard("Total Stock Value", "$" + totalValue.setScale(2).toPlainString(), null));
        cards.add(createCard("Low Stock Items", String.valueOf(lowStockCount), lowStockCount > 0 ? "warning" : null));
        cards.add(createCard("Out of Stock", String.valueOf(outOfStockCount), outOfStockCount > 0 ? "error" : null));

        return cards;
    }

    private Div createCard(String title, String value, String variant) {
        Div card = new Div();
        card.getStyle()
                .set("padding", "var(--vaadin-space-l)")
                .set("border-radius", "var(--vaadin-border-radius-m)")
                .set("border", "1px solid var(--vaadin-contrast-10pct)")
                .set("min-width", "200px")
                .set("flex", "1");

        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("font-size", "var(--aura-font-size-s)")
                .set("color", "var(--vaadin-contrast-60pct)")
                .set("display", "block");

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-size", "var(--aura-font-size-xl)")
                .set("font-weight", "bold")
                .set("display", "block")
                .set("margin-top", "var(--vaadin-space-xs)");

        if ("warning".equals(variant)) {
            valueSpan.getStyle().set("color", "var(--vaadin-warning-color)");
        } else if ("error".equals(variant)) {
            valueSpan.getStyle().set("color", "var(--vaadin-error-color)");
        }

        card.add(titleSpan, valueSpan);
        return card;
    }

    private VerticalLayout createLowStockSection() {
        H2 heading = new H2("Low-Stock Alerts");
        heading.getStyle().set("font-size", "var(--aura-font-size-l)");

        List<Product> lowStock = productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() <= p.getReorderPoint())
                .sorted(Comparator.comparingDouble(p ->
                        p.getReorderPoint() == 0 ? 0 : (double) p.getCurrentStock() / p.getReorderPoint()))
                .limit(10)
                .toList();

        Grid<Product> grid = new Grid<>(Product.class, false);
        grid.addColumn(Product::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setAutoWidth(true);
        grid.addColumn(Product::getCurrentStock).setHeader("Current Stock").setAutoWidth(true);
        grid.addColumn(Product::getReorderPoint).setHeader("Reorder Point").setAutoWidth(true);
        grid.setItems(lowStock);
        grid.setAllRowsVisible(true);

        VerticalLayout section = new VerticalLayout(heading, grid);
        section.setPadding(false);
        return section;
    }

    private VerticalLayout createRecentActivitySection() {
        H2 heading = new H2("Recent Activity");
        heading.getStyle().set("font-size", "var(--aura-font-size-l)");

        List<StockEvent> events = stockEventRepository.findTop10ByOrderByTimestampDesc();

        Grid<StockEvent> grid = new Grid<>(StockEvent.class, false);
        grid.addColumn(e -> e.getTimestamp().format(TIME_FORMAT)).setHeader("Timestamp").setAutoWidth(true);
        grid.addColumn(e -> e.getProduct().getName()).setHeader("Product").setAutoWidth(true);
        grid.addColumn(e -> e.getType().name()).setHeader("Type").setAutoWidth(true);
        grid.addColumn(StockEvent::getQuantity).setHeader("Quantity").setAutoWidth(true);
        grid.addColumn(e -> e.getReason() != null ? e.getReason() : "").setHeader("Reference / Reason").setAutoWidth(true);
        grid.setItems(events);
        grid.setAllRowsVisible(true);

        VerticalLayout section = new VerticalLayout(heading, grid);
        section.setPadding(false);
        return section;
    }
}
