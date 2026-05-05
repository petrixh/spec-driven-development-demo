package com.example.specdriven.view;

import com.example.specdriven.model.Book;
import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.model.Patron;
import com.example.specdriven.repository.BookRepository;
import com.example.specdriven.repository.LendingRecordRepository;
import com.example.specdriven.repository.PatronRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReturnViewTest extends SpringBrowserlessTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private LendingRecordRepository lendingRecordRepository;

    @BeforeEach
    void setUp() {
        lendingRecordRepository.deleteAll();
        bookRepository.deleteAll();
        patronRepository.deleteAll();
    }

    @Test
    void landingViewRendersWithInputFieldAndLookUpButton() {
        navigate(BookReturnView.class);

        TextField isbnField = $(TextField.class).first();
        assertNotNull(isbnField);
        assertTrue(isbnField.getPlaceholder().contains("978"));

        List<Button> buttons = $(Button.class).all();
        assertTrue(buttons.stream().anyMatch(b -> "Look up".equals(b.getText())));
    }

    @Test
    void validIsbnLookupShowsBookDetails() {
        Book book = bookRepository.save(new Book("Clean Code", "Robert Martin",
                "978-0-13-235088-4", "QA76.76", new BigDecimal("0.15")));
        Patron patron = patronRepository.save(new Patron("Jane Doe", "LIB-001", BigDecimal.ZERO));
        lendingRecordRepository.save(new LendingRecord(book, patron,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));

        navigate(BookReturnView.class);

        TextField isbnField = $(TextField.class).first();
        test(isbnField).setValue("978-0-13-235088-4");

        Button lookupButton = $(Button.class).all().stream()
                .filter(b -> "Look up".equals(b.getText()))
                .findFirst().orElseThrow();
        test(lookupButton).click();

        // Verify book title is displayed
        List<H2> headings = $(H2.class).all();
        assertTrue(headings.stream().anyMatch(h -> h.getText().contains("Clean Code")),
                "Book title should be displayed");

        // Verify patron info is displayed
        List<Span> spans = $(Span.class).all();
        assertTrue(spans.stream().anyMatch(s -> "Jane Doe".equals(s.getText())),
                "Patron name should be displayed");
    }

    @Test
    void invalidIsbnShowsError() {
        navigate(BookReturnView.class);

        TextField isbnField = $(TextField.class).first();
        test(isbnField).setValue("INVALID-ISBN");

        Button lookupButton = $(Button.class).all().stream()
                .filter(b -> "Look up".equals(b.getText()))
                .findFirst().orElseThrow();
        test(lookupButton).click();

        // View should still show the landing layout (isbn field visible)
        assertTrue(isbnField.isVisible(), "Should remain on landing view after invalid ISBN");
    }

    @Test
    void overdueBookShowsOverdueBadgeAndFee() {
        Book book = bookRepository.save(new Book("Late Book", "Author",
                "978-1-111-11111-1", "QA99", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("John Smith", "LIB-002", BigDecimal.ZERO));
        lendingRecordRepository.save(new LendingRecord(book, patron,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(10)));

        navigate(BookReturnView.class);

        TextField isbnField = $(TextField.class).first();
        test(isbnField).setValue("978-1-111-11111-1");

        Button lookupButton = $(Button.class).all().stream()
                .filter(b -> "Look up".equals(b.getText()))
                .findFirst().orElseThrow();
        test(lookupButton).click();

        // Verify "Overdue" badge is present
        List<Span> spans = $(Span.class).all();
        assertTrue(spans.stream().anyMatch(s -> "Overdue".equals(s.getText())),
                "Overdue badge should be displayed");

        // Verify overdue banner has fee info
        List<Div> divs = $(Div.class).all();
        assertTrue(divs.stream().anyMatch(d -> d.getClassName() != null
                        && d.getClassName().contains("overdue-banner")),
                "Overdue banner should be displayed");
    }

    @Test
    void returnOkProcessesReturnAndResetsView() {
        Book book = bookRepository.save(new Book("Test Book", "Author",
                "978-2-222-22222-2", "QA50", new BigDecimal("0.15")));
        Patron patron = patronRepository.save(new Patron("Alice", "LIB-003", BigDecimal.ZERO));
        lendingRecordRepository.save(new LendingRecord(book, patron,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));

        navigate(BookReturnView.class);

        // Look up the book
        TextField isbnField = $(TextField.class).first();
        test(isbnField).setValue("978-2-222-22222-2");
        Button lookupButton = $(Button.class).all().stream()
                .filter(b -> "Look up".equals(b.getText()))
                .findFirst().orElseThrow();
        test(lookupButton).click();

        // Click Return OK
        Button returnOk = $(Button.class).all().stream()
                .filter(b -> "Return OK".equals(b.getText()))
                .findFirst().orElseThrow();
        test(returnOk).click();

        // Should be back on landing view — Look up button visible, Return OK gone
        List<Button> buttons = $(Button.class).all();
        assertTrue(buttons.stream().anyMatch(b -> "Look up".equals(b.getText())),
                "Should show landing view with Look up button");
        assertFalse(buttons.stream().anyMatch(b -> "Return OK".equals(b.getText())),
                "Return OK button should not be visible after return");
    }
}
