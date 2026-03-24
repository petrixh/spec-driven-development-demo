package com.example.specdriven.admin;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/dashboard", layout = AdminLayout.class)
@PageTitle("Dashboard | re:solve")
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {

    private final TicketService ticketService;

    public DashboardView(TicketService ticketService) {
        this.ticketService = ticketService;

        addClassName("admin-content");
        setPadding(true);

        add(new H2("Dashboard"));

        createSummaryCards();
        createBreakdownTables();
    }

    private void createSummaryCards() {
        long openCount = ticketService.countByStatus(com.example.specdriven.domain.Status.OPEN);
        long inProgressCount = ticketService.countByStatus(com.example.specdriven.domain.Status.IN_PROGRESS);
        long resolvedToday = ticketService.countResolvedToday();
        long closedToday = ticketService.countClosedToday();

        FlexLayout cards = new FlexLayout();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.getStyle()
                .set("gap", "var(--vaadin-space-m)")
                .set("margin-bottom", "var(--vaadin-space-l)");

        cards.add(
                createStatCard("Open", openCount, "var(--status-open)"),
                createStatCard("In Progress", inProgressCount, "var(--status-in-progress)"),
                createStatCard("Resolved Today", resolvedToday, "var(--status-resolved)"),
                createStatCard("Closed Today", closedToday, "var(--status-closed)")
        );

        add(cards);
    }

    private Div createStatCard(String label, long value, String color) {
        Div card = new Div();
        card.addClassName("stat-card");

        Span labelSpan = new Span(label);
        labelSpan.addClassName("stat-label");

        Span valueSpan = new Span(String.valueOf(value));
        valueSpan.addClassName("stat-value");
        valueSpan.getStyle().set("color", color);

        card.add(labelSpan, valueSpan);
        return card;
    }

    private void createBreakdownTables() {
        HorizontalLayout tables = new HorizontalLayout();
        tables.setWidthFull();
        tables.getStyle().set("gap", "var(--vaadin-space-m)");

        // Category breakdown
        VerticalLayout categorySection = new VerticalLayout();
        categorySection.setPadding(false);
        categorySection.add(new H3("Tickets by Category"));

        Grid<CategoryCount> categoryGrid = new Grid<>();
        categoryGrid.addColumn(CategoryCount::category).setHeader("Category");
        categoryGrid.addColumn(CategoryCount::count).setHeader("Count");
        categoryGrid.setItems(
                java.util.Arrays.stream(Category.values())
                        .map(c -> new CategoryCount(c.name(), ticketService.countNonClosedByCategory(c)))
                        .toList()
        );
        categoryGrid.setAllRowsVisible(true);
        categorySection.add(categoryGrid);

        // Priority breakdown
        VerticalLayout prioritySection = new VerticalLayout();
        prioritySection.setPadding(false);
        prioritySection.add(new H3("Tickets by Priority"));

        Grid<PriorityCount> priorityGrid = new Grid<>();
        priorityGrid.addColumn(PriorityCount::priority).setHeader("Priority");
        priorityGrid.addColumn(PriorityCount::count).setHeader("Count");
        priorityGrid.setItems(
                java.util.Arrays.stream(Priority.values())
                        .map(p -> new PriorityCount(p.name(), ticketService.countNonClosedByPriority(p)))
                        .toList()
        );
        priorityGrid.setAllRowsVisible(true);
        prioritySection.add(priorityGrid);

        tables.add(categorySection, prioritySection);
        add(tables);
    }

    record CategoryCount(String category, long count) {
    }

    record PriorityCount(String priority, long count) {
    }
}
