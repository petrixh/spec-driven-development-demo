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
        setMaxWidth("1200px");
        getStyle().set("margin", "0 auto");

        add(new H2("Dashboard"));

        // Summary cards
        long openCount = ticketService.countByStatus(Status.OPEN);
        long inProgressCount = ticketService.countByStatus(Status.IN_PROGRESS);
        long resolvedToday = ticketService.countResolvedToday();
        long closedToday = ticketService.countClosedToday();

        FlexLayout cards = new FlexLayout();
        cards.setWidthFull();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.getStyle().set("gap", "var(--vaadin-gap-m)");

        cards.add(
                createCard("Open", String.valueOf(openCount), "var(--resolve-status-open)"),
                createCard("In Progress", String.valueOf(inProgressCount), "var(--resolve-status-in-progress)"),
                createCard("Resolved Today", String.valueOf(resolvedToday), "var(--resolve-status-resolved)"),
                createCard("Closed Today", String.valueOf(closedToday), "var(--resolve-status-closed)")
        );
        add(cards);

        // Breakdown tables
        FlexLayout tables = new FlexLayout();
        tables.setWidthFull();
        tables.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        tables.getStyle().set("gap", "var(--vaadin-gap-m)");

        Map<Category, Long> byCategory = ticketService.countNonClosedByCategory();
        Map<Priority, Long> byPriority = ticketService.countNonClosedByPriority();

        Div categoryTable = createBreakdownTable("By Category", byCategory, Category.values());
        Div priorityTable = createBreakdownTable("By Priority", byPriority, Priority.values());

        categoryTable.getStyle().set("flex", "1 1 300px");
        priorityTable.getStyle().set("flex", "1 1 300px");

        tables.add(categoryTable, priorityTable);
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
                .set("margin-top", "var(--vaadin-gap-xs)");

        card.add(valueSpan, labelSpan);
        return card;
    }

    private <E extends Enum<E>> Div createBreakdownTable(String title, Map<E, Long> data, E[] allValues) {
        Div wrapper = new Div();
        wrapper.addClassName("resolve-card");

        H3 heading = new H3(title);
        heading.getStyle().set("margin", "0 0 var(--vaadin-gap-s) 0");
        wrapper.add(heading);

        for (E value : allValues) {
            long count = data.getOrDefault(value, 0L);
            Div row = new Div();
            row.getStyle()
                    .set("display", "flex")
                    .set("justify-content", "space-between")
                    .set("gap", "var(--vaadin-gap-m)")
                    .set("padding", "var(--vaadin-gap-s) 0")
                    .set("border-bottom", "1px solid var(--resolve-border-light)");

            Span nameSpan = new Span(value.name().replace("_", " "));
            Span countSpan = new Span(String.valueOf(count));
            countSpan.getStyle().set("font-weight", "600");

            row.add(nameSpan, countSpan);
            wrapper.add(row);
        }

        return wrapper;
    }
}
