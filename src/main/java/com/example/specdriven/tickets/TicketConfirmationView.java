package com.example.specdriven.tickets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Route("confirmation/:confirmationCode")
@PageTitle("Purchase Confirmation")
public class TicketConfirmationView extends VerticalLayout implements BeforeEnterObserver {
    private final PurchaseService purchaseService;

    public TicketConfirmationView(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        setPadding(false);
        setSpacing(false);
        setWidth("100%");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String codeStr = event.getRouteParameters().get("confirmationCode").orElse("");
        PurchaseOrder order = null;
        try {
            UUID code = UUID.fromString(codeStr);
            order = purchaseService.findByConfirmationCode(code).orElse(null);
        } catch (IllegalArgumentException e) {
            // invalid UUID
        }
        if (order == null) {
            event.forwardTo(TicketBrowseView.class);
            return;
        }
        removeAll();
        add(buildNavBar(), buildContent(order));
    }

    private Component buildNavBar() {
        Div nav = new Div();
        nav.addClassName("desktop-nav");
        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("nav-brand");
        nav.add(brand);
        return nav;
    }

    private Component buildContent(PurchaseOrder order) {
        Div outer = new Div();
        outer.addClassName("page-container");

        Div container = new Div();
        container.addClassName("confirmation-container");
        container.add(
            buildSuccessHeader(),
            buildCodeBlock(order),
            buildDetailsList(order),
            buildBuyAnotherBtn()
        );

        outer.add(container);
        return outer;
    }

    private Component buildSuccessHeader() {
        Div wrapper = new Div();
        wrapper.getStyle().set("text-align", "center").set("margin-bottom", "24px");

        Div iconCircle = new Div();
        iconCircle.addClassName("success-icon");
        iconCircle.setText("✓");

        H1 heading = new H1("Purchase Successful!");
        heading.addClassName("success-heading");

        Paragraph subtitle = new Paragraph("Show this code when boarding");
        subtitle.addClassName("success-subtitle");

        wrapper.add(iconCircle, heading, subtitle);
        return wrapper;
    }

    private Component buildCodeBlock(PurchaseOrder order) {
        Div box = new Div();
        box.addClassName("confirmation-code-box");

        Div label = new Div();
        label.addClassName("confirmation-code-label");
        label.setText("Confirmation Code");

        Div code = new Div();
        code.addClassName("confirmation-code-value");
        code.setText(order.getConfirmationCode().toString());

        box.add(label, code);
        return box;
    }

    private Component buildDetailsList(PurchaseOrder order) {
        Div list = new Div();
        list.addClassName("details-list");

        Ticket t = order.getTicket();
        String modeType = t.getTransitMode().displayName() + " · " + t.getTicketType().displayName();
        String purchased = order.getPurchasedAt()
                .format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a"));

        list.add(
            detailRow("Ticket", t.getName()),
            detailRow("Mode", modeType),
            detailRow("Quantity", String.valueOf(order.getQuantity())),
            detailRow("Total", String.format("$%.2f", order.getTotalPrice())),
            detailRow("Card", "**** **** **** " + order.getCardLastFour()),
            detailRow("Purchased", purchased)
        );
        return list;
    }

    private Div detailRow(String label, String value) {
        Div row = new Div();
        row.addClassName("details-row");
        Span labelEl = new Span(label);
        labelEl.addClassName("details-row-label");
        Span valueEl = new Span(value);
        valueEl.addClassName("details-row-value");
        row.add(labelEl, valueEl);
        return row;
    }

    private Component buildBuyAnotherBtn() {
        NativeButton btn = new NativeButton("Buy Another Ticket");
        btn.addClassName("btn-secondary");
        btn.getStyle().set("margin-top", "16px");
        btn.addClickListener(e -> UI.getCurrent().navigate(TicketBrowseView.class));
        return btn;
    }
}
