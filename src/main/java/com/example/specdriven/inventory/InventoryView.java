package com.example.specdriven.inventory;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@Route("inventory")
@PageTitle("Inventory")
@PermitAll
public class InventoryView extends VerticalLayout {

    private final ProductRepository productRepository;
    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final TextField searchField = new TextField();
    private final ComboBox<String> categoryFilter = new ComboBox<>("Category");

    private String currentSearch = "";
    private String currentCategory = null;

    public InventoryView(ProductRepository productRepository) {
        this.productRepository = productRepository;
        addClassName("view-content");
        setSizeFull();

        H1 title = new H1("Inventory");

        configureFilters();
        configureGrid();

        HorizontalLayout toolbar = new HorizontalLayout(searchField, categoryFilter);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.setWidthFull();

        add(title, toolbar, grid);
        setFlexGrow(1, grid);

        refreshGrid();
    }

    private void configureFilters() {
        searchField.setPlaceholder("Search by name or SKU...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> {
            currentSearch = e.getValue();
            refreshGrid();
        });

        categoryFilter.setItems(productRepository.findDistinctCategories());
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.setPlaceholder("All categories");
        categoryFilter.addValueChangeListener(e -> {
            currentCategory = e.getValue();
            refreshGrid();
        });
    }

    private void configureGrid() {
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setSortable(true);
        grid.addColumn(Product::getCategory).setHeader("Category").setSortable(true);
        grid.addColumn(p -> String.format("$%.2f", p.getUnitPrice())).setHeader("Unit Price").setSortable(true);
        grid.addColumn(Product::getCurrentStock).setHeader("Current Stock").setSortable(true);
        grid.addComponentColumn(InventoryView::createStatusBadge).setHeader("Status").setSortable(true);
        grid.setSizeFull();
    }

    static Span createStatusBadge(Product product) {
        String text;
        String className;
        if (product.getCurrentStock() == 0) {
            text = "Out of Stock";
            className = "stock-badge stock-badge-error";
        } else if (product.getCurrentStock() <= product.getReorderPoint()) {
            text = "Low Stock";
            className = "stock-badge stock-badge-warning";
        } else {
            text = "In Stock";
            className = "stock-badge stock-badge-success";
        }
        Span badge = new Span(text);
        badge.addClassNames(className.split(" "));
        return badge;
    }

    private void refreshGrid() {
        List<Product> products = productRepository.findAll();

        if (currentSearch != null && !currentSearch.isBlank()) {
            String lower = currentSearch.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lower)
                            || p.getSku().toLowerCase().contains(lower))
                    .toList();
        }

        if (currentCategory != null && !currentCategory.isBlank()) {
            products = products.stream()
                    .filter(p -> currentCategory.equals(p.getCategory()))
                    .toList();
        }

        grid.setItems(products);
    }
}
