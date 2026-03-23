package com.example.specdriven.inventory;

import com.example.specdriven.layout.MainLayout;
import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@Route(value = "inventory", layout = MainLayout.class)
@PageTitle("Inventory — Stash.log")
@PermitAll
public class BrowseInventoryView extends VerticalLayout {

    private final ProductRepository productRepository;
    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final TextField searchField = new TextField();
    private final ComboBox<String> categoryFilter = new ComboBox<>();

    public BrowseInventoryView(ProductRepository productRepository) {
        this.productRepository = productRepository;
        setSizeFull();

        configureGrid();
        configureFilters();

        HorizontalLayout toolbar = new HorizontalLayout(searchField, categoryFilter);
        toolbar.setWidthFull();

        add(toolbar, grid);
        setFlexGrow(1, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getCategory).setHeader("Category").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getUnitPrice).setHeader("Unit Price").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getCurrentStock).setHeader("Current Stock").setSortable(true).setAutoWidth(true);
        grid.addComponentColumn(this::createStatusBadge).setHeader("Status").setSortable(true).setAutoWidth(true);
        grid.setSizeFull();
    }

    private void configureFilters() {
        searchField.setPlaceholder("Search by name or SKU...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> refreshGrid());

        categoryFilter.setPlaceholder("All categories");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.addValueChangeListener(event -> refreshGrid());

        // Populate categories
        List<String> categories = productRepository.findAll().stream()
                .map(Product::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .sorted()
                .toList();
        categoryFilter.setItems(categories);
    }

    private Span createStatusBadge(Product product) {
        Span badge = new Span();
        if (product.getCurrentStock() == 0) {
            badge.setText("Out of Stock");
            badge.getElement().getThemeList().add("badge error");
        } else if (product.getCurrentStock() <= product.getReorderPoint()) {
            badge.setText("Low Stock");
            badge.getElement().getThemeList().add("badge warning");
        } else {
            badge.setText("In Stock");
            badge.getElement().getThemeList().add("badge success");
        }
        return badge;
    }

    private void refreshGrid() {
        String search = searchField.getValue();
        String category = categoryFilter.getValue();

        List<Product> products = productRepository.findAll();

        if (search != null && !search.isBlank()) {
            String lower = search.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lower)
                            || p.getSku().toLowerCase().contains(lower))
                    .toList();
        }

        if (category != null && !category.isBlank()) {
            products = products.stream()
                    .filter(p -> category.equals(p.getCategory()))
                    .toList();
        }

        grid.setItems(products);
    }
}
