package com.example.specdriven.tickets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.math.BigDecimal;

@Route("ticket/:ticketId")
@PageTitle("Ticket Detail")
public class TicketDetailView extends VerticalLayout implements BeforeEnterObserver {
    private final TicketService ticketService;

    private Ticket ticket;
    private int quantity = 1;
    private Span qtyDisplay;
    private Span subtotalValue;
    private NativeButton minusBtn;
    private NativeButton plusBtn;

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;
        setPadding(false);
        setSpacing(false);
        setWidth("100%");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("ticketId").orElse("");
        try {
            ticket = ticketService.findById(Long.parseLong(idStr)).orElse(null);
        } catch (NumberFormatException e) {
            ticket = null;
        }
        if (ticket == null) {
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
        nav.add(buildNavLinks("detail"));
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
        container.add(buildBackLink(), buildDetailLayout());
        return container;
    }

    private Component buildBackLink() {
        NativeButton back = new NativeButton("← Back to Browse");
        back.addClassName("back-link");
        back.addClickListener(e -> UI.getCurrent().navigate(TicketBrowseView.class));
        return back;
    }

    private Component buildDetailLayout() {
        Div layout = new Div();
        layout.addClassName("detail-layout");
        layout.add(buildTicketInfoCard(), buildPurchasePanel());
        return layout;
    }

    private Component buildTicketInfoCard() {
        Div card = new Div();
        card.addClassName("detail-card");

        Span icon = new Span(ticket.getTransitMode().emoji());
        icon.getStyle().set("font-size", "48px").set("display", "block").set("margin-bottom", "16px");

        H2 title = new H2(ticket.getName());
        title.addClassName("detail-title");

        Div badges = new Div();
        badges.getStyle().set("display", "flex").set("gap", "6px").set("margin-bottom", "12px");
        Span modeBadge = new Span(ticket.getTransitMode().displayName());
        modeBadge.addClassNames("badge", "badge-mode", ticket.getTransitMode().cssClass());
        Span typeBadge = new Span(ticket.getTicketType().displayName());
        typeBadge.addClassNames("badge", "badge-type");
        badges.add(modeBadge, typeBadge);

        Paragraph description = new Paragraph(ticket.getDescription());
        description.getStyle().set("font-size", "14px").set("color", "#a3a3a3").set("margin-bottom", "16px");

        Span priceAmount = new Span(String.format("$%.2f", ticket.getPrice()));
        priceAmount.addClassNames("detail-price", "mode-" + ticket.getTransitMode().cssClass());
        Span priceUnit = new Span(" per ticket");
        priceUnit.addClassName("detail-price-unit");
        Div priceRow = new Div(priceAmount, priceUnit);

        card.add(icon, title, badges, description, priceRow);
        return card;
    }

    private Component buildPurchasePanel() {
        Div panel = new Div();
        panel.addClassName("detail-card");
        panel.getStyle().set("margin-top", "16px");

        Div qtySection = new Div();

        Div qtyLabel = new Div();
        qtyLabel.addClassName("quantity-label");
        qtyLabel.setText("Quantity");

        Div stepper = buildStepper();
        qtySection.add(qtyLabel, stepper);

        Div subtotalBox = buildSubtotalBox();

        NativeButton checkoutBtn = new NativeButton("Continue to Checkout");
        checkoutBtn.addClassName("btn-primary");
        checkoutBtn.addClickListener(e ->
            UI.getCurrent().navigate("checkout/" + ticket.getId() + "/" + quantity));

        panel.add(qtySection, subtotalBox, checkoutBtn);
        return panel;
    }

    private Div buildStepper() {
        Div stepper = new Div();
        stepper.addClassName("stepper");
        stepper.getStyle().set("margin-bottom", "16px");

        minusBtn = new NativeButton("−");
        minusBtn.addClassName("stepper-btn");

        qtyDisplay = new Span("1");
        qtyDisplay.addClassName("stepper-value");

        plusBtn = new NativeButton("+");
        plusBtn.addClassName("stepper-btn");

        minusBtn.addClickListener(e -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });
        plusBtn.addClickListener(e -> {
            if (quantity < 5) {
                quantity++;
                updateQuantityDisplay();
            }
        });

        stepper.add(minusBtn, qtyDisplay, plusBtn);
        return stepper;
    }

    private Div buildSubtotalBox() {
        Div box = new Div();
        box.addClassName("subtotal-box");

        Div label = new Div();
        label.addClassName("subtotal-label");
        label.setText("Subtotal");

        subtotalValue = new Span(String.format("$%.2f", ticket.getPrice()));
        subtotalValue.addClassName("subtotal-value");

        box.add(label, subtotalValue);
        return box;
    }

    private void updateQuantityDisplay() {
        qtyDisplay.setText(String.valueOf(quantity));
        BigDecimal subtotal = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));
        subtotalValue.setText(String.format("$%.2f", subtotal));
    }
}
