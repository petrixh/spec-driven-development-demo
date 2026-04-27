package com.example.specdriven.tickets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
@PageTitle("Transit Tickets")
public class TicketBrowseView extends VerticalLayout {
    private final TicketService ticketService;
    private final Div ticketGrid = new Div();
    private final List<NativeButton> filterButtons = new ArrayList<>();

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        setPadding(false);
        setSpacing(false);
        setWidth("100%");

        add(buildNavBar(), buildContent());
        renderTickets(null);
    }

    private Component buildNavBar() {
        Div nav = new Div();
        nav.addClassName("desktop-nav");
        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("nav-brand");
        nav.add(brand);
        nav.add(buildNavLinks("browse"));
        return nav;
    }

    private Div buildNavLinks(String active) {
        Div links = new Div();
        links.addClassName("nav-links");
        String[] labels = {"Browse", "Detail", "Checkout", "Confirmation"};
        String[] keys   = {"browse", "detail", "checkout", "confirmation"};
        for (int i = 0; i < labels.length; i++) {
            NativeButton link = new NativeButton(labels[i]);
            link.addClassName("nav-link");
            if (keys[i].equals(active)) link.addClassName("active");
            if ("browse".equals(keys[i])) link.addClickListener(e -> UI.getCurrent().navigate(TicketBrowseView.class));
            links.add(link);
        }
        return links;
    }

    private Component buildContent() {
        Div container = new Div();
        container.addClassName("page-container");
        ticketGrid.addClassName("ticket-grid");
        container.add(buildHeader(), buildFilterBar(), ticketGrid);
        return container;
    }

    private Component buildHeader() {
        Div header = new Div();
        header.addClassName("browse-header");
        H1 title = new H1("Transit Tickets");
        title.addClassName("browse-title");
        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("browse-subtitle");
        header.add(title, subtitle);
        return header;
    }

    private Component buildFilterBar() {
        Div bar = new Div();
        bar.addClassName("filter-bar");

        NativeButton allBtn = createFilterButton("All", null);
        allBtn.addClassName("active");
        filterButtons.add(allBtn);
        bar.add(allBtn);

        for (TransitMode mode : TransitMode.values()) {
            NativeButton btn = createFilterButton(mode.displayName(), mode);
            filterButtons.add(btn);
            bar.add(btn);
        }
        return bar;
    }

    private NativeButton createFilterButton(String label, TransitMode mode) {
        NativeButton btn = new NativeButton(label);
        btn.addClassName("filter-btn");
        btn.addClickListener(e -> {
            filterButtons.forEach(b -> b.removeClassName("active"));
            btn.addClassName("active");
            renderTickets(mode);
        });
        return btn;
    }

    private void renderTickets(TransitMode mode) {
        ticketGrid.removeAll();
        List<Ticket> tickets = mode == null ? ticketService.findAll() : ticketService.findByMode(mode);
        tickets.forEach(ticket -> ticketGrid.add(buildTicketCard(ticket)));
    }

    private Component buildTicketCard(Ticket ticket) {
        Div card = new Div();
        card.addClassName("ticket-card");
        card.addClassName("mode-" + ticket.getTransitMode().cssClass());

        Span icon = new Span(ticket.getTransitMode().emoji());
        icon.addClassName("ticket-card-icon");

        Div name = new Div();
        name.addClassName("ticket-card-name");
        name.setText(ticket.getName());

        Div badges = new Div();
        badges.getStyle().set("display", "flex").set("gap", "6px").set("margin-bottom", "10px");

        Span modeBadge = new Span(ticket.getTransitMode().displayName());
        modeBadge.addClassNames("badge", "badge-mode", ticket.getTransitMode().cssClass());

        Span typeBadge = new Span(ticket.getTicketType().displayName());
        typeBadge.addClassNames("badge", "badge-type");
        badges.add(modeBadge, typeBadge);

        Span price = new Span(String.format("$%.2f", ticket.getPrice()));
        price.addClassNames("ticket-card-price", "mode-" + ticket.getTransitMode().cssClass());

        card.add(icon, name, badges, price);
        card.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));
        return card;
    }
}
