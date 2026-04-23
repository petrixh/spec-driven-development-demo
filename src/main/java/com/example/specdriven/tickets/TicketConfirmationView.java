package com.example.specdriven.tickets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Route("confirmation")
public class TicketConfirmationView extends VerticalLayout implements HasUrlParameter<String> {

    private final PurchaseService purchaseService;
    private PurchaseOrder order;

    public TicketConfirmationView(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        addStyleNames();
        buildLayout();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            UI.getCurrent().navigate("");
            return;
        }
        UUID code;
        try {
            code = UUID.fromString(parameter);
        } catch (IllegalArgumentException e) {
            UI.getCurrent().navigate("");
            return;
        }
        Optional<PurchaseOrder> orderOpt = purchaseService.findByConfirmationCode(code);
        if (orderOpt.isEmpty()) {
            UI.getCurrent().navigate("");
            return;
        }
        order = orderOpt.get();
        populateConfirmation();
    }

    private void addStyleNames() {
        addClassName("confirmation-page");
        getStyle().set("background-color", "#0f0f0f");
        getStyle().set("color", "#fafafa");
        getStyle().set("min-height", "100vh");
        getStyle().set("font-family", "Inter, system-ui, -apple-system, sans-serif");
    }

    private void buildLayout() {
        Div mainContainer = new Div();
        mainContainer.addClassName("confirmation-container");
        add(mainContainer);
        setSizeFull();
    }

    private void populateConfirmation() {
        Div mainContainer = getChildren()
                .filter(el -> el instanceof Div)
                .findFirst()
                .map(el -> (Div) el)
                .orElse(null);
        if (mainContainer == null) return;
        mainContainer.removeAll();

        if (order == null) return;

        // Success header
        Div iconCircle = new Div();
        iconCircle.addClassName("success-icon");
        iconCircle.setText("\u2713");
        iconCircle.getStyle().set("display", "flex");
        iconCircle.getStyle().set("align-items", "center");
        iconCircle.getStyle().set("justify-content", "center");
        iconCircle.getStyle().set("margin", "0 auto");

        Span heading = new Span("Purchase Successful!");
        heading.addClassName("success-heading");
        heading.getStyle().set("text-align", "center");

        Span subtitle = new Span("Show this code when boarding");
        subtitle.addClassName("success-subtitle");
        subtitle.getStyle().set("text-align", "center");

        mainContainer.add(iconCircle, heading, subtitle);

        // Confirmation code box
        Div codeBox = new Div();
        codeBox.addClassName("confirmation-code-box");

        Span codeLabel = new Span("CONFIRMATION CODE");
        codeLabel.addClassName("confirmation-code-label");
        codeLabel.getStyle().set("display", "block");
        codeLabel.getStyle().set("margin-bottom", "8px");

        Span codeValue = new Span(order.getConfirmationCode().toString());
        codeValue.addClassName("confirmation-code-value");

        codeBox.add(codeLabel, codeValue);
        mainContainer.add(codeBox);

        // Details list
        Div detailsList = new Div();
        detailsList.addClassName("details-list");

        String modeName = order.getTicket().getTransitMode().name().charAt(0) + order.getTicket().getTransitMode().name().substring(1).toLowerCase();
        String typeName = order.getTicket().getTicketType().name().replaceAll("_", " ");
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);

        addDetailRow(detailsList, "Ticket", order.getTicket().getName());
        addDetailRow(detailsList, "Mode", modeName + " \u00b7 " + typeName);
        addDetailRow(detailsList, "Quantity", String.valueOf(order.getQuantity()));
        addDetailRow(detailsList, "Total", formatPrice(order.getTotalPrice()));
        addDetailRow(detailsList, "Card", "**** " + order.getCardLastFour());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, hh:mm a");
        addDetailRow(detailsList, "Purchased", order.getPurchasedAt().format(formatter));

        mainContainer.add(detailsList);

        // Buy another ticket button
        Button buyBtn = new Button("Buy Another Ticket");
        buyBtn.addClassName("secondary");
        buyBtn.getStyle().set("width", "100%");
        buyBtn.addClickListener(e -> UI.getCurrent().getPage().executeJs("window.location.href = $0", ""));
        mainContainer.add(buyBtn);
    }

    private void addDetailRow(Div container, String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("details-row");
        row.getStyle().set("justify-content", "space-between");
        row.setSpacing(false);
        row.setPadding(false);

        Span labelSpan = new Span(label);
        labelSpan.addClassName("details-row-label");

        Span valueSpan = new Span(value);
        valueSpan.addClassName("details-row-value");

        row.add(labelSpan, valueSpan);
        container.add(row);
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
