package com.example.specdriven.tickets;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Route("checkout")
public class TicketCheckoutView extends VerticalLayout implements HasUrlParameter<String> {

    private static final int MAX_QUANTITY = 5;

    private final TicketService ticketService;
    private final PurchaseService purchaseService;
    private Ticket ticket;
    private Integer quantity;

    private final TextField cardholderName = new TextField();
    private final TextField cardNumber = new TextField();
    private final TextField expiration = new TextField();
    private final TextField cvv = new TextField();
    private final Span orderTotal = new Span();

    public TicketCheckoutView(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        addStyleNames();
        buildLayout();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter == null || parameter.isEmpty() || !parameter.contains("-")) {
            UI.getCurrent().navigate("");
            return;
        }
        String[] parts = parameter.split("-", 2);
        if (parts.length != 2) {
            UI.getCurrent().navigate("");
            return;
        }
        Long ticketId;
        try {
            ticketId = Long.parseLong(parts[0]);
            quantity = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            UI.getCurrent().navigate("");
            return;
        }
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            UI.getCurrent().navigate("");
            return;
        }
        ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            UI.getCurrent().navigate("");
            return;
        }
        populateCheckout(quantity);
    }

    private void addStyleNames() {
        addClassName("checkout-page");
        getStyle().set("background-color", "#0f0f0f");
        getStyle().set("color", "#fafafa");
        getStyle().set("min-height", "100vh");
        getStyle().set("font-family", "Inter, system-ui, -apple-system, sans-serif");
    }

    private void buildLayout() {
        Div mainContainer = new Div();
        mainContainer.addClassName("page-container");

        Span backLink = new Span("\u2190 Back");
        backLink.addClassName("back-link");
        backLink.addClickListener(e -> {
            if (ticket != null && ticket.getId() != null) {
                UI.getCurrent().getPage().executeJs(
                    "window.location.href = $0",
                    "ticket/" + ticket.getId()
                );
            } else {
                UI.getCurrent().getPage().executeJs(
                    "window.location.href = $0",
                    ""
                );
            }
        });

        H1 title = new H1("Checkout");
        title.addClassName("browse-title");

        Div contentWrapper = new Div();
        contentWrapper.addClassName("checkout-content");

        mainContainer.add(backLink, title, contentWrapper);
        add(mainContainer);
        setSizeFull();
    }

    private void populateCheckout(Integer quantity) {
        Div mainContainer = getChildren()
                .filter(el -> el instanceof Div)
                .findFirst()
                .map(el -> (Div) el)
                .orElse(null);
        if (mainContainer == null) return;

        mainContainer.removeAll();

        Span backLink = new Span("\u2190 Back");
        backLink.addClassName("back-link");
        backLink.addClickListener(e -> {
            if (ticket != null && ticket.getId() != null) {
                UI.getCurrent().getPage().executeJs(
                    "window.location.href = $0",
                    "ticket/" + ticket.getId()
                );
            } else {
                UI.getCurrent().getPage().executeJs(
                    "window.location.href = $0",
                    ""
                );
            }
        });

        H1 title = new H1("Checkout");
        title.addClassName("browse-title");

        Div contentWrapper = new Div();
        contentWrapper.addClassName("checkout-content");

        mainContainer.add(backLink, title, contentWrapper);

        Div summaryCard = buildOrderSummaryCard(quantity);
        Div paymentCard = buildPaymentCard(quantity);

        contentWrapper.add(paymentCard, summaryCard);
    }

    private Div buildOrderSummaryCard(Integer quantity) {
        Div card = new Div();
        card.addClassName("summary-card");

        Span header = new Span("ORDER SUMMARY");
        header.addClassName("section-header");

        String totalStr = formatPrice(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));

        Span ticketName = new Span(ticket.getName());
        ticketName.getStyle().set("font-size", "15px");
        ticketName.getStyle().set("font-weight", "700");
        ticketName.getStyle().set("color", "#fafafa");

        orderTotal.addClassName("summary-total");
        orderTotal.setText(totalStr);

        HorizontalLayout titleRow = new HorizontalLayout(ticketName, orderTotal);
        titleRow.setAlignItems(FlexLayout.Alignment.BASELINE);
        titleRow.setSpacing(true);
        titleRow.setPadding(false);
        titleRow.getStyle().set("width", "100%");
        titleRow.getStyle().set("justify-content", "space-between");

        String modeName = ticket.getTransitMode().name().charAt(0) + ticket.getTransitMode().name().substring(1).toLowerCase();
        String typeName = ticket.getTicketType().name().replaceAll("_", " ");
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        Span subtitle = new Span(modeName + " \u00b7 " + typeName + " \u00b7 Qty: " + quantity);
        subtitle.getStyle().set("font-size", "13px");
        subtitle.getStyle().set("color", "#737373");
        subtitle.getStyle().set("display", "block");
        subtitle.getStyle().set("margin-bottom", "8px");

        Span unitPrice = new Span("Unit: " + formatPrice(ticket.getPrice()));
        unitPrice.getStyle().set("font-size", "12px");
        unitPrice.getStyle().set("color", "#737373");
        unitPrice.getStyle().set("margin-bottom", "12px");
        unitPrice.getStyle().set("display", "block");

        card.add(header, titleRow, subtitle, unitPrice);
        return card;
    }

    private Div buildPaymentCard(Integer quantity) {
        Div card = new Div();
        card.addClassName("payment-card");

        Span header = new Span("PAYMENT DETAILS");
        header.addClassName("section-header");

        cardholderName.addClassName("checkout-field");
        cardholderName.setLabel("Cardholder Name");
        cardholderName.setPlaceholder("John Doe");
        cardholderName.setWidthFull();

        cardNumber.addClassName("checkout-field");
        cardNumber.setLabel("Card Number");
        cardNumber.setPlaceholder("1234 5678 9012 3456");
        cardNumber.setWidthFull();
        cardNumber.addValueChangeListener(e -> {
            String val = e.getValue().replaceAll("\\s+", "");
            if (!val.isEmpty()) {
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < val.length() && i < 16; i++) {
                    if (i > 0 && i % 4 == 0) formatted.append(" ");
                    formatted.append(val.charAt(i));
                }
                cardNumber.setValue(formatted.toString());
            }
        });

        expiration.addClassName("checkout-field");
        expiration.setLabel("Expiration (MM/YY)");
        expiration.setPlaceholder("MM/YY");

        cvv.addClassName("checkout-field");
        cvv.setLabel("CVV");
        cvv.setPlaceholder("123");

        HorizontalLayout expiryRow = new HorizontalLayout(expiration, cvv);
        expiryRow.addClassName("payment-row-inline");
        expiryRow.setSpacing(true);
        expiryRow.setPadding(false);
        expiryRow.getStyle().set("width", "100%");

        Button purchaseBtn = new Button("Purchase");
        purchaseBtn.getElement().getStyle().set("background", "linear-gradient(135deg, #f59e0b, #d97706)");
        purchaseBtn.getElement().getStyle().set("color", "#000000");
        purchaseBtn.getElement().getStyle().set("font-weight", "700");
        purchaseBtn.getElement().getStyle().set("border-radius", "12px");
        purchaseBtn.getElement().getStyle().set("min-height", "48px");
        purchaseBtn.getElement().getStyle().set("font-size", "15px");
        purchaseBtn.getElement().getStyle().set("width", "100%");
        purchaseBtn.getElement().getStyle().set("border", "none");
        purchaseBtn.setEnabled(false);
        purchaseBtn.addClickListener(e -> handlePurchase(quantity));

        HasValue.ValueChangeListener<HasValue.ValueChangeEvent<String>> validator = e -> {
            boolean valid = cardholderName.getValue().trim().length() > 0
                    && cardNumber.getValue().replaceAll("\\s+", "").matches("^\\d{16}$")
                    && expiration.getValue().matches("^\\d{2}/\\d{2}$")
                    && cvv.getValue().matches("^\\d{3}$");
            purchaseBtn.setEnabled(valid);
        };
        cardholderName.addValueChangeListener(validator);
        cardNumber.addValueChangeListener(validator);
        expiration.addValueChangeListener(validator);
        cvv.addValueChangeListener(validator);

        card.add(header, cardholderName, cardNumber, expiryRow, purchaseBtn);
        return card;
    }

    private void handlePurchase(Integer quantity) {
        if (ticket == null) return;
        String raw = cardNumber.getValue().trim().replaceAll("\\s+", "");
        String lastFour = raw.substring(Math.max(0, raw.length() - 4));
        try {
            PurchaseOrder order = purchaseService.createPurchase(ticket.getId(), quantity, lastFour);
            UI.getCurrent().getPage().executeJs(
                "window.location.href = $0",
                "confirmation/" + order.getConfirmationCode()
            );
        } catch (Exception ex) {
            UI.getCurrent().navigate("");
        }
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
