package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Route("confirmation")
@PageTitle("Confirmation")
public class TicketConfirmationView extends VerticalLayout implements HasUrlParameter<String> {

    private static final DateTimeFormatter PURCHASED_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.ENGLISH);

    private final PurchaseService purchaseService;

    public TicketConfirmationView(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        addClassName("confirmation-container");
        setPadding(false);
        setSpacing(false);
    }

    @Override
    public void setParameter(BeforeEvent event, String code) {
        removeAll();
        Optional<PurchaseOrder> order = parseUuid(code).flatMap(purchaseService::findByConfirmationCode);
        if (order.isEmpty()) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        render(order.get());
    }

    private Optional<UUID> parseUuid(String code) {
        try {
            return Optional.of(UUID.fromString(code));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private void render(PurchaseOrder order) {
        Ticket ticket = order.getTicket();

        Div successIcon = new Div();
        successIcon.setText("✓");
        successIcon.addClassName("success-icon");

        H1 heading = new H1("Purchase Successful!");
        heading.addClassName("success-heading");

        Div subtitle = new Div();
        subtitle.setText("Show this code when boarding");
        subtitle.addClassName("success-subtitle");

        Div codeBox = new Div();
        codeBox.addClassName("confirmation-code-box");
        Div codeLabel = new Div();
        codeLabel.setText("CONFIRMATION CODE");
        codeLabel.addClassName("confirmation-code-label");
        Div codeValue = new Div();
        codeValue.setText(order.getConfirmationCode().toString());
        codeValue.addClassName("confirmation-code-value");
        codeBox.add(codeLabel, codeValue);

        Div details = new Div();
        details.addClassName("details-list");
        details.add(detailRow("Ticket", ticket.getName()));
        details.add(detailRow("Mode",
                ticket.getTransitMode().getDisplayName() + " · " + ticket.getTicketType().getDisplayName()));
        details.add(detailRow("Quantity", String.valueOf(order.getQuantity())));
        details.add(detailRow("Total", TicketBrowseView.formatPrice(order.getTotalPrice())));
        details.add(detailRow("Card", "**** **** **** " + order.getCardLastFour()));
        details.add(detailRow("Purchased", order.getPurchasedAt().format(PURCHASED_FMT)));

        Button buyAnother = new Button("Buy Another Ticket");
        buyAnother.addClassName("secondary-cta");
        buyAnother.addClickListener(e -> UI.getCurrent().navigate(TicketBrowseView.class));

        add(successIcon, heading, subtitle, codeBox, details, buyAnother);
    }

    private Div detailRow(String label, String value) {
        Div row = new Div();
        row.addClassName("details-row");
        Span l = new Span(label);
        l.addClassName("details-row-label");
        Span v = new Span(value);
        v.addClassName("details-row-value");
        row.add(l, v);
        return row;
    }
}
