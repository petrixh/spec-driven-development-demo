package com.example.specdriven.admin;

import com.example.specdriven.data.Category;
import com.example.specdriven.data.Priority;
import com.example.specdriven.data.Status;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Map;

@Route(value = "admin/dashboard", layout = AdminLayout.class)
@PageTitle("Dashboard — re:solve")
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {

    public DashboardView(TicketService ticketService) {
        setPadding(true);
        setSpacing(true);

        add(new H2("Dashboard"));

        // Summary cards
        long openCount = ticketService.countByStatus(Status.OPEN);
        long inProgressCount = ticketService.countByStatus(Status.IN_PROGRESS);
        long resolvedToday = ticketService.countResolvedToday();
        long closedToday = ticketService.countClosedToday();

        FlexLayout cards = new FlexLayout();
        cards.setWidthFull();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.getStyle().set("gap", "var(--vaadin-space-m)");

        cards.add(
                createCard("Open", String.valueOf(openCount), "var(--resolve-status-open)"),
                createCard("In Progress", String.valueOf(inProgressCount), "var(--resolve-status-in-progress)"),
                createCard("Resolved Today", String.valueOf(resolvedToday), "var(--resolve-status-resolved)"),
                createCard("Closed Today", String.valueOf(closedToday), "var(--resolve-status-closed)")
        );
        add(cards);

        // Breakdown tables
        HorizontalLayout tables = new HorizontalLayout();
        tables.setWidthFull();
        tables.setSpacing(true);

        Map<Category, Long> byCategory = ticketService.countNonClosedByCategory();
        Map<Priority, Long> byPriority = ticketService.countNonClosedByPriority();

        tables.add(createBreakdownTable("By Category", byCategory, Category.values()));
        tables.add(createBreakdownTable("By Priority", byPriority, Priority.values()));
        tables.setFlexGrow(1, tables.getComponentAt(0));
        tables.setFlexGrow(1, tables.getComponentAt(1));

        add(tables);
    }

    private Div createCard(String label, String value, String color) {
        Div card = new Div();
        card.addClassName("resolve-card");
        card.getStyle()
                .set("flex", "1 1 200px")
                .set("min-width", "200px")
                .set("text-align", "center");

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-size", "var(--aura-font-size-xxl, 2rem)")
                .set("font-weight", "700")
                .set("color", color)
                .set("display", "block");

        Span labelSpan = new Span(label);
        labelSpan.addClassName("resolve-meta");
        labelSpan.getStyle()
                .set("display", "block")
                .set("margin-top", "var(--vaadin-space-xs)");

        card.add(valueSpan, labelSpan);
        return card;
    }

    private <E extends Enum<E>> VerticalLayout createBreakdownTable(String title, Map<E, Long> data, E[] allValues) {
        VerticalLayout table = new VerticalLayout();
        table.setPadding(false);
        table.setSpacing(false);

        table.add(new H3(title));

        Div tableDiv = new Div();
        tableDiv.getStyle()
                .set("background", "var(--resolve-card-bg)")
                .set("border-radius", "var(--resolve-card-radius)")
                .set("border", "1px solid var(--resolve-border-color)")
                .set("overflow", "hidden");

        for (E value : allValues) {
            long count = data.getOrDefault(value, 0L);
            Div row = new Div();
            row.getStyle()
                    .set("display", "flex")
                    .set("justify-content", "space-between")
                    .set("padding", "var(--vaadin-space-s) var(--vaadin-space-m)")
                    .set("border-bottom", "1px solid var(--resolve-border-light)");

            Span nameSpan = new Span(value.name().replace("_", " "));
            Span countSpan = new Span(String.valueOf(count));
            countSpan.getStyle().set("font-weight", "600");

            row.add(nameSpan, countSpan);
            tableDiv.add(row);
        }

        table.add(tableDiv);
        return table;
    }
}
