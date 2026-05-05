package com.example.specdriven.view;

import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.service.BookReturnService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Route("")
@PageTitle("Book Return")
public class BookReturnView extends VerticalLayout {

    private final BookReturnService bookReturnService;

    private final VerticalLayout landingLayout = new VerticalLayout();
    private final VerticalLayout processingLayout = new VerticalLayout();

    private TextField isbnField;
    private LendingRecord currentRecord;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public BookReturnView(BookReturnService bookReturnService) {
        this.bookReturnService = bookReturnService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        buildLandingLayout();
        buildProcessingLayout();

        add(landingLayout, processingLayout);
        showLanding();
    }

    private void buildLandingLayout() {
        landingLayout.setAlignItems(Alignment.CENTER);
        landingLayout.setMaxWidth("500px");
        landingLayout.setWidth("100%");
        landingLayout.addClassName("landing-layout");

        Span barcodeIcon = new Span("\u2758\u2758\u2758\u2758\u2758");
        barcodeIcon.addClassName("barcode-icon");

        H2 heading = new H2("Scan or enter barcode");

        Paragraph helper = new Paragraph("Place the book under the scanner, or type the ISBN manually");
        helper.addClassName("helper-text");

        isbnField = new TextField();
        isbnField.setPlaceholder("e.g. 978-0-13-468599-1");
        isbnField.setWidthFull();
        isbnField.setClearButtonVisible(true);

        Button lookupButton = new Button("Look up", event -> lookupBook());
        lookupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        lookupButton.addClickShortcut(Key.ENTER);

        HorizontalLayout inputRow = new HorizontalLayout(isbnField, lookupButton);
        inputRow.setWidthFull();
        inputRow.setFlexGrow(1, isbnField);
        inputRow.setAlignItems(Alignment.BASELINE);

        Paragraph status = new Paragraph("Ready \u2014 waiting for input");
        status.addClassName("status-text");

        landingLayout.add(barcodeIcon, heading, helper, inputRow, status);
    }

    private void buildProcessingLayout() {
        processingLayout.setMaxWidth("700px");
        processingLayout.setWidth("100%");
        processingLayout.addClassName("processing-layout");
    }

    private void showLanding() {
        landingLayout.setVisible(true);
        processingLayout.setVisible(false);
        currentRecord = null;
        isbnField.clear();
        isbnField.focus();
    }

    private void showProcessing(LendingRecord record) {
        this.currentRecord = record;
        processingLayout.removeAll();

        boolean overdue = record.isOverdue();

        // Header row
        Span title = new Span("Return processing");
        title.addClassName("processing-title");

        Span badge = createStatusBadge(overdue);

        HorizontalLayout headerLeft = new HorizontalLayout(title, badge);
        headerLeft.setAlignItems(FlexComponent.Alignment.CENTER);

        Button cancelButton = new Button("Cancel", event -> showLanding());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout header = new HorizontalLayout(headerLeft, cancelButton);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        // Book details card
        Div bookCard = new Div();
        bookCard.addClassName("info-card");

        H2 bookTitle = new H2(record.getBook().getTitle());
        bookTitle.addClassName("book-title");

        Paragraph authorLine = new Paragraph(record.getBook().getAuthor());

        Span isbnLabel = new Span("ISBN ");
        Span isbnValue = new Span(record.getBook().getIsbn());
        isbnValue.addClassName("mono-value");
        Span callLabel = new Span("  Call no. ");
        Span callValue = new Span(record.getBook().getCallNumber());
        callValue.addClassName("mono-value");
        Paragraph bookMeta = new Paragraph(isbnLabel, isbnValue, callLabel, callValue);

        bookCard.add(bookTitle, authorLine, bookMeta);

        // Patron and account cards side by side
        Div patronCard = new Div();
        patronCard.addClassName("info-card");
        Span patronLabel = new Span("PATRON");
        patronLabel.addClassName("card-label");
        Div patronName = new Div(new Span(record.getPatron().getName()));
        patronName.addClassName("card-value");
        Div patronId = new Div(new Span(record.getPatron().getPatronId()));
        patronId.addClassName("mono-value");
        patronCard.add(patronLabel, patronName, patronId);

        Div accountCard = new Div();
        accountCard.addClassName("info-card");
        Span accountLabel = new Span("ACCOUNT BALANCE");
        accountLabel.addClassName("card-label");
        Div accountValue = new Div(new Span(CURRENCY_FORMAT.format(record.getPatron().getAccountBalance())));
        accountValue.addClassName("card-value");
        String balanceDescription = record.getPatron().getAccountBalance().compareTo(BigDecimal.ZERO) == 0
                ? "No outstanding fees" : "Outstanding fees";
        Div accountDesc = new Div(new Span(balanceDescription));
        accountCard.add(accountLabel, accountValue, accountDesc);

        Div patronRow = new Div(patronCard, accountCard);
        patronRow.addClassName("patron-row");

        // Lending info
        Div lendingInfo = new Div();
        if (overdue) {
            lendingInfo.addClassName("overdue-banner");
            long days = record.daysOverdue();
            BigDecimal fee = bookReturnService.calculateLateFee(record);

            Div overdueText = new Div();
            overdueText.addClassName("overdue-text");
            overdueText.add(new Span(days + " days overdue"));

            Div dueLine = new Div(new Span(
                    "Due: " + record.getDueDate().format(DATE_FORMAT)
                    + " \u2014 Checked out: " + record.getCheckoutDate().format(DATE_FORMAT)));

            Div feeDisplay = new Div();
            feeDisplay.addClassName("fee-display");
            Div feeAmount = new Div(new Span(CURRENCY_FORMAT.format(fee)));
            feeAmount.addClassName("fee-amount");
            Div feeRate = new Div(new Span(CURRENCY_FORMAT.format(record.getBook().getLateFeePerDay()) + "/day"));
            feeRate.addClassName("fee-rate");
            feeDisplay.add(feeAmount, feeRate);

            HorizontalLayout overdueLine = new HorizontalLayout();
            overdueLine.setWidthFull();
            overdueLine.setJustifyContentMode(JustifyContentMode.BETWEEN);
            overdueLine.setAlignItems(FlexComponent.Alignment.CENTER);

            VerticalLayout overdueLeft = new VerticalLayout(overdueText, dueLine);
            overdueLeft.setPadding(false);
            overdueLeft.setSpacing(false);

            overdueLine.add(overdueLeft, feeDisplay);
            lendingInfo.add(overdueLine);
        } else {
            lendingInfo.addClassName("ontime-banner");
            lendingInfo.add(new Span("On time"));
            lendingInfo.add(new Div(new Span(
                    "Due: " + record.getDueDate().format(DATE_FORMAT)
                    + " \u2014 Checked out: " + record.getCheckoutDate().format(DATE_FORMAT))));
        }

        // Return OK button
        Button returnOkButton = new Button("Return OK", event -> processReturn());
        returnOkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        returnOkButton.setWidthFull();
        returnOkButton.addClassName("return-ok-button");

        processingLayout.add(header, bookCard, patronRow, lendingInfo, returnOkButton);

        landingLayout.setVisible(false);
        processingLayout.setVisible(true);
    }

    private void lookupBook() {
        String isbn = isbnField.getValue().trim();
        if (isbn.isEmpty()) {
            isbnField.setInvalid(true);
            isbnField.setErrorMessage("Please enter an ISBN");
            return;
        }

        try {
            LendingRecord record = bookReturnService.lookupActiveRecord(isbn);
            showProcessing(record);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void processReturn() {
        if (currentRecord == null) {
            return;
        }

        try {
            bookReturnService.processReturn(currentRecord.getId());
            Notification.show("Book returned successfully: " + currentRecord.getBook().getTitle(),
                            5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            showLanding();
        } catch (Exception e) {
            Notification.show("Error processing return: " + e.getMessage(),
                            5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private Span createStatusBadge(boolean overdue) {
        Span badge = new Span(overdue ? "Overdue" : "On time");
        badge.addClassName(overdue ? "badge-overdue" : "badge-ontime");
        return badge;
    }
}
