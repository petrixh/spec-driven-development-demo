package com.example.specdriven.tickets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Route("")
public class TicketBrowseView extends VerticalLayout implements HasUrlParameter<String> {

    private static final String ALL_FILTER = "All";

    private final TicketService ticketService;
    private final List<Ticket> allTickets;
    private final Div ticketGrid = new Div();
    private final HorizontalLayout filterBar = new HorizontalLayout();

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        this.allTickets = ticketService.getAllTickets();

        addStyleNames();
        buildLayout();
        populateGrid(allTickets);
        setFilterActive(ALL_FILTER);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }

    private void addStyleNames() {
        addClassName("browse-page");
        getStyle().set("background-color", "#0f0f0f");
        getStyle().set("color", "#fafafa");
        getStyle().set("min-height", "100vh");
        getStyle().set("font-family", "Inter, system-ui, -apple-system, sans-serif");
    }

    private void buildLayout() {
        Div mainContainer = new Div();
        mainContainer.addClassName("page-container");
        mainContainer.addClassName("browse-header");

        H1 title = new H1("Transit Tickets");
        title.addClassName("browse-title");

        Span subtitle = new Span("Find and purchase your ride");
        subtitle.addClassName("browse-subtitle");

        mainContainer.add(title, subtitle);

        filterBar.addClassName("filter-bar");
        filterBar.setSpacing(false);
        filterBar.setPadding(false);

        String[] filters = {ALL_FILTER, "Bus", "Train", "Metro", "Ferry"};

        for (String filter : filters) {
            Span filterBtn = new Span(filter);
            filterBtn.addClassName("filter-btn");
            if (filter.equals(ALL_FILTER)) {
                filterBtn.addClassName("active");
            }

            filterBtn.addClickListener(e -> {
                handleFilterClick(filter);
            });

            filterBar.add(filterBtn);
        }

        ticketGrid.addClassName("ticket-grid");

        add(mainContainer, filterBar, ticketGrid);
        setSizeFull();
    }

    private void handleFilterClick(String filter) {
        setFilterActive(filter);
        filterTickets(filter);
    }

    private void setFilterActive(String filter) {
        List<Span> btns = filterBar.getChildren()
                .filter(c -> c instanceof Span)
                .map(c -> (Span) c)
                .toList();
        for (Span child : btns) {
            child.getElement().getClassList().remove("active");
        }
        for (Span child : btns) {
            if (child.getElement().getText().equals(filter)) {
                child.getElement().getClassList().add("active");
                break;
            }
        }
    }

    private void filterTickets(String filter) {
        List<Ticket> filtered;
        if (ALL_FILTER.equals(filter)) {
            filtered = allTickets;
        } else {
            Ticket.TransitMode mode = Ticket.TransitMode.valueOf(filter.toUpperCase());
            filtered = ticketService.getTicketsByMode(mode);
        }
        populateGrid(filtered);
    }

    private void populateGrid(List<Ticket> tickets) {
        ticketGrid.removeAll();

        for (Ticket ticket : tickets) {
            Div card = buildTicketCard(ticket);
            ticketGrid.add(card);
        }
    }

    private Div buildTicketCard(Ticket ticket) {
        Div card = new Div();
        card.addClassName("ticket-card");
        card.addClassName("mode-" + ticket.getTransitMode().name().toLowerCase());

        card.addClickListener(e -> navigateToDetail(ticket.getId()));

        Span icon = new Span(getModeEmoji(ticket.getTransitMode()));
        icon.addClassName("ticket-card-icon");

        Span name = new Span(ticket.getName());
        name.addClassName("ticket-card-name");

        HorizontalLayout badges = new HorizontalLayout();
        badges.setSpacing(true);
        badges.setPadding(false);

        String modeName = ticket.getTransitMode().name().charAt(0) + ticket.getTransitMode().name().substring(1).toLowerCase();
        Span modeBadge = new Span(modeName);
        modeBadge.addClassName("badge");
        modeBadge.addClassName("badge-mode");
        modeBadge.addClassName("mode-" + ticket.getTransitMode().name().toLowerCase());

        String typeName = ticket.getTicketType().name().replaceAll("_", " ");
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        Span typeBadge = new Span(typeName);
        typeBadge.addClassName("badge");
        typeBadge.addClassName("badge-type");

        badges.add(modeBadge, typeBadge);

        Span price = new Span(formatPrice(ticket.getPrice()));
        price.addClassName("ticket-card-price");
        price.addClassName("mode-" + ticket.getTransitMode().name().toLowerCase());

        card.add(icon, name, badges, price);
        return card;
    }

    private void navigateToDetail(Long ticketId) {
        UI.getCurrent().navigate("ticket/" + ticketId);
    }

    private String getModeEmoji(Ticket.TransitMode mode) {
        return switch (mode) {
            case BUS -> "\uD83D\uDE8C";
            case TRAIN -> "\uD83D\uDE86";
            case METRO -> "\uD83D\uDE87";
            case FERRY -> "\u26F4\uFE0F";
        };
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
