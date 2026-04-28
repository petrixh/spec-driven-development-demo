package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Route("checkout/:ticketId([0-9]+)/:quantity([0-9]+)")
@PageTitle("Checkout")
public class TicketCheckoutView extends VerticalLayout implements BeforeEnterObserver {

    private static final Pattern CARD_NUMBER = Pattern.compile("^\\d{16}$");
    private static final Pattern EXPIRATION = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final Pattern CVV = Pattern.compile("^\\d{3}$");

    private final TicketService ticketService;
    private final PurchaseService purchaseService;

    public TicketCheckoutView(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        addClassName("page-container");
        setPadding(false);
        setSpacing(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        RouteParameters params = event.getRouteParameters();
        Long ticketId = params.getLong("ticketId").orElse(null);
        Integer quantity = params.getInteger("quantity").orElse(null);
        if (ticketId == null || quantity == null) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        Ticket ticket = ticketService.findById(ticketId).orElse(null);
        if (ticket == null || quantity < 1 || quantity > 5) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        render(ticket, quantity);
    }

    private void render(Ticket ticket, int quantity) {
        BigDecimal total = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));

        Button back = new Button("← Back to Details");
        back.addClassName("back-link");
        back.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));
        add(back);

        H1 title = new H1("Checkout");
        title.addClassName("page-title");
        add(title);

        Div layout = new Div();
        layout.addClassName("checkout-layout");

        Div summary = orderSummaryCard(ticket, quantity, total);
        Div payment = paymentCard(ticket, quantity, total);

        layout.add(summary, payment);
        add(layout);
    }

    private Div orderSummaryCard(Ticket ticket, int quantity, BigDecimal total) {
        Div card = new Div();
        card.addClassName("summary-card");
        card.addClassName("checkout-summary-sticky");

        Div header = new Div();
        header.setText("ORDER SUMMARY");
        header.addClassName("section-header");

        card.add(header, mobileSummary(ticket, quantity, total), desktopSummary(ticket, quantity, total));
        return card;
    }

    private Div mobileSummary(Ticket ticket, int quantity, BigDecimal total) {
        Div container = new Div();
        container.addClassName("summary-mobile");

        Div left = new Div();
        left.addClassName("summary-mobile-left");
        Div name = new Div();
        name.setText(ticket.getName());
        name.addClassName("summary-ticket-name");
        Div subtitle = new Div();
        subtitle.setText(ticket.getTransitMode().getDisplayName()
                + " · " + ticket.getTicketType().getDisplayName()
                + " · Qty: " + quantity);
        subtitle.addClassName("summary-subtitle");
        Div unit = new Div();
        unit.setText("Unit: " + TicketBrowseView.formatPrice(ticket.getPrice()));
        unit.addClassName("summary-unit");
        left.add(name, subtitle, unit);

        Span totalValue = new Span(TicketBrowseView.formatPrice(total));
        totalValue.addClassName("summary-mobile-total");

        container.add(left, totalValue);
        return container;
    }

    private Div desktopSummary(Ticket ticket, int quantity, BigDecimal total) {
        Div container = new Div();
        container.addClassName("summary-desktop");

        Div headerRow = new Div();
        headerRow.addClassName("summary-header-row");

        Div iconBox = new Div();
        iconBox.setText(ticket.getTransitMode().getEmoji());
        iconBox.addClassName("summary-icon-box");
        iconBox.addClassName("mode-" + ticket.getTransitMode().getCssKey());

        Div nameCol = new Div();
        nameCol.addClassName("summary-name-col");
        Div name = new Div();
        name.setText(ticket.getName());
        name.addClassName("summary-ticket-name");
        Div subtitle = new Div();
        subtitle.setText(ticket.getTransitMode().getDisplayName() + " · " + ticket.getTicketType().getDisplayName());
        subtitle.addClassName("summary-subtitle");
        nameCol.add(name, subtitle);

        headerRow.add(iconBox, nameCol);

        Div divider1 = new Div();
        divider1.addClassName("summary-divider");

        Div qtyRow = summaryRow("Quantity", String.valueOf(quantity));
        Div priceRow = summaryRow("Unit Price", TicketBrowseView.formatPrice(ticket.getPrice()));

        Div divider2 = new Div();
        divider2.addClassName("summary-divider");

        Div totalRow = new Div();
        totalRow.addClassName("summary-total-row");
        Span totalLabel = new Span("Total");
        totalLabel.addClassName("summary-total-label");
        Span totalValue = new Span(TicketBrowseView.formatPrice(total));
        totalValue.addClassName("summary-total");
        totalRow.add(totalLabel, totalValue);

        container.add(headerRow, divider1, qtyRow, priceRow, divider2, totalRow);
        return container;
    }

    private Div summaryRow(String label, String value) {
        Div row = new Div();
        row.addClassName("summary-row");
        Span l = new Span(label);
        l.addClassName("summary-row-label");
        Span v = new Span(value);
        v.addClassName("summary-row-value");
        row.add(l, v);
        return row;
    }

    private Div paymentCard(Ticket ticket, int quantity, BigDecimal total) {
        Div card = new Div();
        card.addClassName("payment-card");

        Div header = new Div();
        header.setText("PAYMENT DETAILS");
        header.addClassName("section-header");

        TextField cardholder = new TextField("Cardholder Name");
        cardholder.setPlaceholder("Jane Rider");
        cardholder.setWidthFull();
        cardholder.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

        TextField cardNumber = new TextField("Card Number");
        cardNumber.setPlaceholder("1234 5678 9012 3456");
        cardNumber.setWidthFull();
        cardNumber.setMaxLength(19);
        cardNumber.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);
        cardNumber.addValueChangeListener(e -> {
            String formatted = formatCardNumber(e.getValue());
            if (!formatted.equals(e.getValue())) {
                cardNumber.setValue(formatted);
            }
        });

        TextField expiration = new TextField("Expiration");
        expiration.setPlaceholder("MM/YY");
        expiration.setMaxLength(5);
        expiration.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);
        expiration.addValueChangeListener(e -> {
            String formatted = formatExpiration(e.getValue());
            if (!formatted.equals(e.getValue())) {
                expiration.setValue(formatted);
            }
        });

        TextField cvv = new TextField("CVV");
        cvv.setPlaceholder("123");
        cvv.setMaxLength(3);
        cvv.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

        Div sideBySide = new Div(expiration, cvv);
        sideBySide.addClassName("payment-row-inline");

        Button purchase = new Button("Purchase");
        purchase.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        purchase.addClassName("primary-cta");
        purchase.setEnabled(false);

        Runnable validate = () -> {
            boolean ok = !cardholder.getValue().isBlank()
                    && CARD_NUMBER.matcher(cardNumber.getValue().replace(" ", "")).matches()
                    && EXPIRATION.matcher(expiration.getValue()).matches()
                    && CVV.matcher(cvv.getValue()).matches();
            purchase.setEnabled(ok);
        };
        cardholder.addValueChangeListener(e -> validate.run());
        cardNumber.addValueChangeListener(e -> validate.run());
        expiration.addValueChangeListener(e -> validate.run());
        cvv.addValueChangeListener(e -> validate.run());

        purchase.addClickListener(e -> {
            PurchaseOrder order = purchaseService.purchase(
                    ticket.getId(), quantity, cardNumber.getValue());
            UI.getCurrent().navigate("confirmation/" + order.getConfirmationCode());
        });

        card.add(header, cardholder, cardNumber, sideBySide, purchase);
        return card;
    }

    private static String formatCardNumber(String value) {
        if (value == null) return "";
        String digits = value.replaceAll("\\D", "");
        if (digits.length() > 16) digits = digits.substring(0, 16);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    private static String formatExpiration(String value) {
        if (value == null) return "";
        String digits = value.replaceAll("\\D", "");
        if (digits.length() > 4) digits = digits.substring(0, 4);
        if (digits.length() <= 2) return digits;
        return digits.substring(0, 2) + "/" + digits.substring(2);
    }
}
