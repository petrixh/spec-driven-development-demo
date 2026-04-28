package com.example.specdriven.tickets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.math.BigDecimal;
import java.util.List;

@Route("")
@PageTitle("Transit Tickets")
public class TicketBrowseView extends VerticalLayout {

    private final TicketService ticketService;

    private final Div ticketGrid = new Div();
    private final HorizontalLayout filterBar = new HorizontalLayout();
    private TransitMode activeFilter; // null == "All"

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;

        setPadding(false);
        setSpacing(false);
        addClassName("page-container");

        Div header = new Div();
        header.addClassName("browse-header");
        H1 title = new H1("Transit Tickets");
        title.addClassName("browse-title");
        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("browse-subtitle");
        header.add(title, subtitle);

        filterBar.addClassName("filter-bar");
        filterBar.setPadding(false);
        filterBar.setSpacing(false);
        buildFilterBar();

        ticketGrid.addClassName("ticket-grid");

        add(header, filterBar, ticketGrid);

        renderTickets();
    }

    private void buildFilterBar() {
        filterBar.removeAll();
        filterBar.add(filterButton("All", null));
        for (TransitMode mode : TransitMode.values()) {
            filterBar.add(filterButton(mode.getDisplayName(), mode));
        }
    }

    private Button filterButton(String label, TransitMode mode) {
        Button button = new Button(label, (ClickEvent<Button> e) -> selectFilter(mode));
        button.addClassName("filter-btn");
        if (mode == activeFilter) {
            button.addClassName("active");
        }
        return button;
    }

    private void selectFilter(TransitMode mode) {
        if (this.activeFilter == mode) {
            return;
        }
        this.activeFilter = mode;
        buildFilterBar();
        renderTickets();
    }

    private void renderTickets() {
        ticketGrid.removeAll();
        List<Ticket> tickets = ticketService.findByMode(activeFilter);
        for (Ticket ticket : tickets) {
            ticketGrid.add(ticketCard(ticket));
        }
    }

    private RouterLink ticketCard(Ticket ticket) {
        RouterLink link = new RouterLink(TicketDetailView.class, ticket.getId());
        link.addClassName("ticket-card");
        link.addClassName("mode-" + ticket.getTransitMode().getCssKey());

        Div emoji = new Div();
        emoji.setText(ticket.getTransitMode().getEmoji());
        emoji.addClassName("ticket-card-icon");

        Div name = new Div();
        name.setText(ticket.getName());
        name.addClassName("ticket-card-name");

        Div badges = new Div();
        badges.addClassName("ticket-card-badges");
        Span modeBadge = new Span(ticket.getTransitMode().getDisplayName());
        modeBadge.addClassName("badge");
        modeBadge.addClassName("badge-mode");
        modeBadge.addClassName(ticket.getTransitMode().getCssKey());
        Span typeBadge = new Span(ticket.getTicketType().getDisplayName());
        typeBadge.addClassName("badge");
        typeBadge.addClassName("badge-type");
        badges.add(modeBadge, typeBadge);

        Div price = new Div();
        price.setText(formatPrice(ticket.getPrice()));
        price.addClassName("ticket-card-price");
        price.addClassName("mode-" + ticket.getTransitMode().getCssKey());

        link.add(emoji, name, badges, price);
        return link;
    }

    static String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
