package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.math.BigDecimal;

@Route("ticket")
@PageTitle("Ticket Detail")
public class TicketDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final TicketService ticketService;

    private static final int MIN_QTY = 1;
    private static final int MAX_QTY = 5;

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;
        addClassName("page-container");
        setPadding(false);
        setSpacing(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Long ticketId) {
        removeAll();
        Ticket ticket = ticketService.findById(ticketId).orElse(null);
        if (ticket == null) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        render(ticket);
    }

    private void render(Ticket ticket) {
        RouterLink back = new RouterLink("← Back to Browse", TicketBrowseView.class);
        back.addClassName("back-link");
        add(back);

        Div layout = new Div();
        layout.addClassName("detail-layout");

        layout.add(infoCard(ticket));
        layout.add(purchasePanel(ticket));

        add(layout);
    }

    private Div infoCard(Ticket ticket) {
        Div card = new Div();
        card.addClassName("detail-card");

        Div emoji = new Div();
        emoji.setText(ticket.getTransitMode().getEmoji());
        emoji.addClassName("detail-emoji");

        H2 title = new H2(ticket.getName());
        title.addClassName("detail-title");

        HorizontalLayout badges = new HorizontalLayout();
        badges.setPadding(false);
        badges.setSpacing(false);
        badges.addClassName("detail-badges");
        Span modeBadge = new Span(ticket.getTransitMode().getDisplayName());
        modeBadge.addClassName("badge");
        modeBadge.addClassName("badge-mode");
        modeBadge.addClassName(ticket.getTransitMode().getCssKey());
        Span typeBadge = new Span(ticket.getTicketType().getDisplayName());
        typeBadge.addClassName("badge");
        typeBadge.addClassName("badge-type");
        badges.add(modeBadge, typeBadge);

        Paragraph description = new Paragraph(ticket.getDescription());
        description.addClassName("detail-description");

        Div price = new Div();
        Span priceValue = new Span(TicketBrowseView.formatPrice(ticket.getPrice()));
        priceValue.addClassName("detail-price-value");
        priceValue.addClassName("mode-" + ticket.getTransitMode().getCssKey());
        Span priceUnit = new Span(" per ticket");
        priceUnit.addClassName("detail-price-unit");
        price.addClassName("detail-price");
        price.add(priceValue, priceUnit);

        card.add(emoji, title, badges, description, price);
        return card;
    }

    private Div purchasePanel(Ticket ticket) {
        Div panel = new Div();
        panel.addClassName("purchase-panel");

        Span quantityLabel = new Span("Quantity");
        quantityLabel.addClassName("section-label");

        int[] qty = {MIN_QTY};

        Span valueDisplay = new Span(String.valueOf(qty[0]));
        valueDisplay.addClassName("stepper-value");

        Span subtotalValue = new Span(TicketBrowseView.formatPrice(ticket.getPrice()));
        subtotalValue.addClassName("subtotal-value");

        Button minus = new Button("−");
        minus.addClassName("stepper-btn");
        Button plus = new Button("+");
        plus.addClassName("stepper-btn");

        Runnable updateUi = () -> {
            valueDisplay.setText(String.valueOf(qty[0]));
            BigDecimal total = ticket.getPrice().multiply(BigDecimal.valueOf(qty[0]));
            subtotalValue.setText(TicketBrowseView.formatPrice(total));
            minus.setEnabled(qty[0] > MIN_QTY);
            plus.setEnabled(qty[0] < MAX_QTY);
        };
        updateUi.run();

        minus.addClickListener(e -> {
            if (qty[0] > MIN_QTY) {
                qty[0]--;
                updateUi.run();
            }
        });
        plus.addClickListener(e -> {
            if (qty[0] < MAX_QTY) {
                qty[0]++;
                updateUi.run();
            }
        });

        Div stepper = new Div(minus, valueDisplay, plus);
        stepper.addClassName("quantity-stepper");

        Div subtotalBox = new Div();
        subtotalBox.addClassName("subtotal-box");
        Span subtotalLabel = new Span("SUBTOTAL");
        subtotalLabel.addClassName("subtotal-label");
        subtotalBox.add(subtotalLabel, subtotalValue);

        Button cta = new Button("Continue to Checkout");
        cta.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cta.addClassName("primary-cta");
        cta.addClickListener(e -> UI.getCurrent().navigate(
                "checkout/" + ticket.getId() + "/" + qty[0]));

        panel.add(quantityLabel, stepper, subtotalBox, cta);
        return panel;
    }
}
