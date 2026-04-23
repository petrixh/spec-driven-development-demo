package com.example.specdriven.tickets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.component.UI;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Route("ticket")
public class TicketDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 5;

    private final TicketService ticketService;
    private Ticket ticket;
    private int quantity = 1;
    private final Span quantityDisplay = new Span();
    private final Span subtotalValue = new Span();
    private final Button checkoutBtn = new Button("Continue to Checkout");

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;
        addStyleNames();
        buildLayout();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter == null) {
            UI.getCurrent().navigate("");
            return;
        }
        ticket = ticketService.getTicketById(parameter);
        if (ticket == null) {
            UI.getCurrent().navigate("");
            return;
        }
        quantity = 1;
        populateDetail();
    }

    private void addStyleNames() {
        addClassName("detail-page");
        getStyle().set("background-color", "#0f0f0f");
        getStyle().set("color", "#fafafa");
        getStyle().set("min-height", "100vh");
        getStyle().set("font-family", "Inter, system-ui, -apple-system, sans-serif");
        checkoutBtn.getElement().getStyle().set("background", "linear-gradient(135deg, #f59e0b, #d97706)");
        checkoutBtn.getElement().getStyle().set("color", "#000000");
        checkoutBtn.getElement().getStyle().set("font-weight", "700");
        checkoutBtn.getElement().getStyle().set("border-radius", "12px");
        checkoutBtn.getElement().getStyle().set("min-height", "48px");
        checkoutBtn.getElement().getStyle().set("font-size", "15px");
        checkoutBtn.getElement().getStyle().set("width", "100%");
        checkoutBtn.getElement().getStyle().set("border", "none");
        checkoutBtn.setEnabled(true);
    }

    private void buildLayout() {
        Div mainContainer = new Div();
        mainContainer.addClassName("page-container");

        Span backLink = new Span("\u2190 Back to Browse");
        backLink.addClassName("back-link");
        backLink.addClickListener(e -> UI.getCurrent().navigate(""));

        Div infoCard = new Div();
        infoCard.addClassName("detail-card");
        infoCard.addClassName("detail-info");

        Div purchaseCard = new Div();
        purchaseCard.addClassName("detail-card");
        purchaseCard.addClassName("detail-quantity");

        mainContainer.add(backLink, infoCard, purchaseCard);
        add(mainContainer);
        setSizeFull();
    }

    private void populateDetail() {
        if (ticket == null) return;

        Div mainContainer = getChildren()
                .filter(el -> el instanceof Div)
                .findFirst()
                .map(el -> (Div) el)
                .orElse(null);
        if (mainContainer == null) return;

        mainContainer.removeAll();

        // Back link
        Span backLink = new Span("\u2190 Back to Browse");
        backLink.addClassName("back-link");
        backLink.addClickListener(e -> UI.getCurrent().navigate(""));

        // Ticket info card
        Div infoCard = new Div();
        infoCard.addClassName("detail-card");
        infoCard.addClassName("detail-info");

        // Emoji
        Span emoji = new Span(getModeEmoji(ticket.getTransitMode()));
        emoji.getStyle().set("font-size", "48px");
        emoji.getStyle().set("margin-bottom", "8px");
        emoji.getStyle().set("display", "block");

        // Title
        H2 title = new H2(ticket.getName());
        title.getStyle().set("font-size", "22px");
        title.getStyle().set("font-weight", "800");
        title.getStyle().set("color", "#fafafa");
        title.getStyle().set("margin", "0 0 8px");

        // Badges
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

        // Description
        Span description = new Span(ticket.getDescription());
        description.getStyle().set("color", "#a3a3a3");
        description.getStyle().set("font-size", "14px");
        description.getStyle().set("margin-bottom", "16px");
        description.getStyle().set("display", "block");

        // Price row
        Span price = new Span(formatPrice(ticket.getPrice()));
        price.getStyle().set("font-size", "28px");
        price.getStyle().set("font-weight", "800");
        price.getStyle().set("color", getModeColor(ticket.getTransitMode()));

        Span priceUnit = new Span("per ticket");
        priceUnit.getStyle().set("font-size", "14px");
        priceUnit.getStyle().set("color", "#525252");
        priceUnit.getStyle().set("margin-left", "8px");

        HorizontalLayout priceRow = new HorizontalLayout(price, priceUnit);
        priceRow.setAlignItems(Alignment.BASELINE);
        priceRow.setSpacing(true);
        priceRow.setPadding(false);

        infoCard.add(emoji, title, badges, description, priceRow);

        // Quantity card
        Div purchaseCard = new Div();
        purchaseCard.addClassName("detail-card");
        purchaseCard.addClassName("detail-quantity");

        // Quantity label
        Span quantityLabel = new Span("Quantity");
        quantityLabel.getStyle().set("font-size", "12px");
        quantityLabel.getStyle().set("font-weight", "600");
        quantityLabel.getStyle().set("color", "#737373");
        quantityLabel.getStyle().set("margin-bottom", "8px");
        quantityLabel.getStyle().set("display", "block");

        // Custom stepper layout
        HorizontalLayout stepper = buildStepper();

        // Subtotal box
        Div subtotalBox = new Div();
        subtotalBox.addClassName("subtotal-box");

        Span subtotalLabel = new Span("SUBTOTAL");
        subtotalLabel.getStyle().set("font-size", "12px");
        subtotalLabel.getStyle().set("font-weight", "600");
        subtotalLabel.getStyle().set("color", "#737373");
        subtotalLabel.getStyle().set("text-transform", "uppercase");
        subtotalLabel.getStyle().set("letter-spacing", "1px");
        subtotalLabel.getStyle().set("display", "block");
        subtotalLabel.getStyle().set("margin-bottom", "4px");

        subtotalValue.getStyle().set("font-size", "32px");
        subtotalValue.getStyle().set("font-weight", "800");
        subtotalValue.getStyle().set("color", getModeColor(ticket.getTransitMode()));

        HorizontalLayout subtotalRow = new HorizontalLayout(subtotalLabel, subtotalValue);
        subtotalRow.setAlignItems(Alignment.BASELINE);
        subtotalRow.setSpacing(true);
        subtotalRow.setPadding(false);
        subtotalBox.add(subtotalRow);

        checkoutBtn.addClickListener(e -> navigateToCheckout());

        purchaseCard.add(quantityLabel, stepper, subtotalBox, checkoutBtn);

        mainContainer.add(backLink, infoCard, purchaseCard);
        updateSubtotal();
    }

    private HorizontalLayout buildStepper() {
        HorizontalLayout stepper = new HorizontalLayout();
        stepper.getStyle().set("display", "inline-flex");
        stepper.getStyle().set("align-items", "center");
        stepper.getStyle().set("background", "#0f0f0f");
        stepper.getStyle().set("border-radius", "12px");
        stepper.getStyle().set("border", "1px solid #2a2a2a");
        stepper.getStyle().set("height", "48px");

        // Minus button
        Span minusBtn = new Span("\u2212");
        minusBtn.getStyle().set("width", "48px");
        minusBtn.getStyle().set("height", "48px");
        minusBtn.getStyle().set("background", "transparent");
        minusBtn.getStyle().set("color", "#f59e0b");
        minusBtn.getStyle().set("font-size", "20px");
        minusBtn.getStyle().set("font-weight", "700");
        minusBtn.getStyle().set("border", "none");
        minusBtn.getStyle().set("cursor", "pointer");
        minusBtn.getStyle().set("display", "flex");
        minusBtn.getStyle().set("align-items", "center");
        minusBtn.getStyle().set("justify-content", "center");
        minusBtn.getStyle().set("padding", "0");
        minusBtn.addClickListener(e -> {
            if (quantity > MIN_QUANTITY) {
                quantity--;
                quantityDisplay.setText(String.valueOf(quantity));
                updateSubtotal();
            }
        });

        // Value display
        quantityDisplay.getStyle().set("width", "48px");
        quantityDisplay.getStyle().set("text-align", "center");
        quantityDisplay.getStyle().set("font-size", "20px");
        quantityDisplay.getStyle().set("font-weight", "800");
        quantityDisplay.getStyle().set("color", "#fafafa");
        quantityDisplay.getStyle().set("user-select", "none");
        quantityDisplay.setText(String.valueOf(quantity));

        // Plus button
        Span plusBtn = new Span("+");
        plusBtn.getStyle().set("width", "48px");
        plusBtn.getStyle().set("height", "48px");
        plusBtn.getStyle().set("background", "transparent");
        plusBtn.getStyle().set("color", "#f59e0b");
        plusBtn.getStyle().set("font-size", "20px");
        plusBtn.getStyle().set("font-weight", "700");
        plusBtn.getStyle().set("border", "none");
        plusBtn.getStyle().set("cursor", "pointer");
        plusBtn.getStyle().set("display", "flex");
        plusBtn.getStyle().set("align-items", "center");
        plusBtn.getStyle().set("justify-content", "center");
        plusBtn.getStyle().set("padding", "0");
        plusBtn.addClickListener(e -> {
            if (quantity < MAX_QUANTITY) {
                quantity++;
                quantityDisplay.setText(String.valueOf(quantity));
                updateSubtotal();
            }
        });

        stepper.add(minusBtn, quantityDisplay, plusBtn);
        return stepper;
    }

    private void updateSubtotal() {
        BigDecimal subtotal = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));
        subtotalValue.setText(formatPrice(subtotal));
    }

    private void navigateToCheckout() {
        if (ticket != null && ticket.getId() != null) {
            UI.getCurrent().getPage().executeJs(
                "window.location.href = $0",
                "checkout/" + ticket.getId() + "-" + quantity
            );
        }
    }

    private String getModeColor(Ticket.TransitMode mode) {
        return switch (mode) {
            case BUS -> "#f59e0b";
            case TRAIN -> "#fb923c";
            case METRO -> "#fbbf24";
            case FERRY -> "#a3e635";
        };
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
