package com.example.specdriven.tickets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

@Route("checkout/:ticketId/:quantity")
@PageTitle("Checkout")
public class TicketCheckoutView extends VerticalLayout implements BeforeEnterObserver {
    private final TicketService ticketService;
    private final PurchaseService purchaseService;

    private Ticket ticket;
    private int quantity;
    private TextField cardholderField;
    private TextField cardNumberField;
    private TextField expirationField;
    private TextField cvvField;
    private NativeButton purchaseBtn;

    public TicketCheckoutView(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        setPadding(false);
        setSpacing(false);
        setWidth("100%");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters params = event.getRouteParameters();
        try {
            Long ticketId = Long.parseLong(params.get("ticketId").orElse("0"));
            quantity = Integer.parseInt(params.get("quantity").orElse("1"));
            ticket = ticketService.findById(ticketId).orElse(null);
        } catch (NumberFormatException e) {
            ticket = null;
        }
        if (ticket == null || quantity < 1 || quantity > 5) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        removeAll();
        add(buildNavBar(), buildContent());
    }

    private Component buildNavBar() {
        Div nav = new Div();
        nav.addClassName("desktop-nav");
        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("nav-brand");
        nav.add(brand);
        nav.add(buildNavLinks("checkout"));
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

        NativeButton back = new NativeButton("← Back to Details");
        back.addClassName("back-link");
        back.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));

        H1 heading = new H1("Checkout");
        heading.getStyle().set("font-size", "22px").set("font-weight", "800")
               .set("color", "#fafafa").set("margin", "0 0 16px");

        Div layout = buildCheckoutLayout();
        container.add(back, heading, layout);
        return container;
    }

    private Div buildCheckoutLayout() {
        Div layout = new Div();
        layout.addClassName("checkout-layout");

        Div summaryDiv = buildOrderSummary();
        summaryDiv.addClassNames("checkout-summary", "checkout-summary-sticky");

        Div formDiv = buildPaymentForm();
        formDiv.addClassName("checkout-form");

        layout.add(summaryDiv, formDiv);
        return layout;
    }

    private Div buildOrderSummary() {
        Div card = new Div();
        card.addClassName("summary-card");

        Div header = new Div();
        header.addClassName("section-header");
        header.setText("Order Summary");

        // Icon + name/subtitle row
        Div ticketRow = new Div();
        ticketRow.addClassName("summary-ticket-row");

        Div iconBox = new Div();
        iconBox.addClassNames("summary-icon-box", "mode-" + ticket.getTransitMode().cssClass());
        iconBox.setText(ticket.getTransitMode().emoji());

        Div nameBlock = new Div();
        nameBlock.getStyle().set("flex", "1");
        Div nameEl = new Div();
        nameEl.getStyle().set("font-size", "15px").set("font-weight", "700").set("color", "#fafafa").set("margin-bottom", "2px");
        nameEl.setText(ticket.getName());
        Div subtitleEl = new Div();
        subtitleEl.getStyle().set("font-size", "12px").set("color", "#737373");
        subtitleEl.setText(ticket.getTransitMode().displayName() + " · " + ticket.getTicketType().displayName());
        nameBlock.add(nameEl, subtitleEl);

        ticketRow.add(iconBox, nameBlock);

        Div divider = new Div();
        divider.getStyle().set("border-top", "1px solid #2a2a2a").set("margin", "12px 0");

        Div quantityRow = buildSummaryRow("Quantity", String.valueOf(quantity));
        Div unitPriceRow = buildSummaryRow("Unit Price", String.format("$%.2f", ticket.getPrice()));

        Div totalRow = new Div();
        totalRow.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center").set("margin-top", "12px").set("padding-top", "12px").set("border-top", "1px solid #2a2a2a");

        Div totalLabel = new Div();
        totalLabel.getStyle().set("font-size", "14px").set("font-weight", "600").set("color", "#a3a3a3");
        totalLabel.setText("Total");

        java.math.BigDecimal total = ticket.getPrice().multiply(java.math.BigDecimal.valueOf(quantity));
        Span totalValue = new Span(String.format("$%.2f", total));
        totalValue.addClassName("summary-total");

        totalRow.add(totalLabel, totalValue);
        card.add(header, ticketRow, divider, quantityRow, unitPriceRow, totalRow);
        return card;
    }

    private Div buildSummaryRow(String label, String value) {
        Div row = new Div();
        row.getStyle().set("display", "flex").set("justify-content", "space-between").set("margin-bottom", "8px");
        Div labelEl = new Div();
        labelEl.getStyle().set("font-size", "13px").set("color", "#737373");
        labelEl.setText(label);
        Div valueEl = new Div();
        valueEl.getStyle().set("font-size", "13px").set("color", "#fafafa").set("font-weight", "600");
        valueEl.setText(value);
        row.add(labelEl, valueEl);
        return row;
    }

    private Div buildPaymentForm() {
        Div card = new Div();
        card.addClassName("payment-card");

        Div header = new Div();
        header.addClassName("section-header");
        header.setText("Payment Details");

        cardholderField = new TextField("Cardholder Name");
        cardholderField.setPlaceholder("Full name on card");
        cardholderField.setWidthFull();

        cardNumberField = new TextField("Card Number");
        cardNumberField.setPlaceholder("16-digit card number");
        cardNumberField.setWidthFull();
        cardNumberField.setMaxLength(19);

        expirationField = new TextField("Expiration (MM/YY)");
        expirationField.setPlaceholder("MM/YY");
        expirationField.setWidthFull();
        expirationField.setMaxLength(5);

        cvvField = new TextField("CVV");
        cvvField.setPlaceholder("3 digits");
        cvvField.setWidthFull();
        cvvField.setMaxLength(3);

        Div expiryRow = new Div();
        expiryRow.addClassName("payment-row-inline");
        expiryRow.add(expirationField, cvvField);

        purchaseBtn = new NativeButton("Purchase");
        purchaseBtn.addClassName("btn-primary");
        purchaseBtn.setEnabled(false);
        purchaseBtn.getStyle().set("margin-top", "16px");

        cardholderField.setValueChangeMode(ValueChangeMode.EAGER);
        cardNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        expirationField.setValueChangeMode(ValueChangeMode.EAGER);
        cvvField.setValueChangeMode(ValueChangeMode.EAGER);

        cardholderField.addValueChangeListener(e -> validateForm());
        cardNumberField.addValueChangeListener(e -> validateForm());
        expirationField.addValueChangeListener(e -> validateForm());
        cvvField.addValueChangeListener(e -> validateForm());

        purchaseBtn.addClickListener(e -> submitPurchase());

        card.add(header, cardholderField, cardNumberField, expiryRow, purchaseBtn);
        return card;
    }

    private void validateForm() {
        boolean valid = !cardholderField.getValue().trim().isEmpty()
                && cardNumberField.getValue().replaceAll("\\s", "").matches("\\d{16}")
                && expirationField.getValue().matches("(0[1-9]|1[0-2])/\\d{2}")
                && cvvField.getValue().matches("\\d{3}");
        purchaseBtn.setEnabled(valid);
    }

    private void submitPurchase() {
        String rawCard = cardNumberField.getValue().replaceAll("\\s", "");
        String lastFour = rawCard.substring(rawCard.length() - 4);
        PurchaseOrder order = purchaseService.createOrder(ticket, quantity, lastFour);
        UI.getCurrent().navigate("confirmation/" + order.getConfirmationCode().toString());
    }
}
