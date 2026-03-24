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
        cards.getStyle().set("gap", "var(--vaadin-gap-m)");

        cards.add(createCard("Total Products", String.valueOf(totalProducts), null));
        cards.add(createCard("Total Stock Value", "$" + totalValue.setScale(2).toPlainString(), null));
        cards.add(createCard("Low Stock Items", String.valueOf(lowStockCount), lowStockCount > 0 ? "warning" : null));
        cards.add(createCard("Out of Stock", String.valueOf(outOfStockCount), outOfStockCount > 0 ? "error" : null));

        return cards;
    }

    private Div createCard(String title, String value, String variant) {
        Div card = new Div();
        card.addClassName("summary-card");

        Span titleSpan = new Span(title);
        titleSpan.addClassName("card-title");

        Span valueSpan = new Span(value);
        valueSpan.addClassName("card-value");
        if (variant != null) {
            valueSpan.addClassName(variant);
        }

        card.add(titleSpan, valueSpan);
        return card;
    }

    private VerticalLayout createLowStockSection() {
        H2 heading = new H2("Low-Stock Alerts");
        heading.addClassName("section-heading");

        List<Product> lowStock = productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() > 0 && p.getCurrentStock() <= p.getReorderPoint())
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
        heading.addClassName("section-heading");

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
