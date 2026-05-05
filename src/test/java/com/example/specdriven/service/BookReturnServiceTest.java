package com.example.specdriven.service;

import com.example.specdriven.model.Book;
import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.model.LendingStatus;
import com.example.specdriven.model.Patron;
import com.example.specdriven.repository.BookRepository;
import com.example.specdriven.repository.LendingRecordRepository;
import com.example.specdriven.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReturnServiceTest {

    @Autowired
    private BookReturnService bookReturnService;

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
    void lookupByIsbnReturnsActiveRecord() {
        Book book = bookRepository.save(new Book("Test Book", "Author", "111-222", "QA1", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = lendingRecordRepository.save(
                new LendingRecord(book, patron, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));

        LendingRecord found = bookReturnService.lookupActiveRecord("111-222");

        assertEquals(record.getId(), found.getId());
        assertEquals("Test Book", found.getBook().getTitle());
        assertEquals("Test Patron", found.getPatron().getName());
        assertEquals(LendingStatus.CHECKED_OUT, found.getStatus());
    }

    @Test
    void lookupNonexistentIsbnThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookReturnService.lookupActiveRecord("999-999"));

        assertTrue(ex.getMessage().contains("No book found"));
    }

    @Test
    void lookupBookWithNoActiveRecordThrowsException() {
        Book book = bookRepository.save(new Book("Test Book", "Author", "111-222", "QA1", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = new LendingRecord(book, patron, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
        record.setStatus(LendingStatus.RETURNED);
        record.setReturnDate(LocalDate.now());
        lendingRecordRepository.save(record);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookReturnService.lookupActiveRecord("111-222"));

        assertTrue(ex.getMessage().contains("No active lending record"));
    }

    @Test
    void processReturnOnTimeSetStatusReturned() {
        Book book = bookRepository.save(new Book("Test Book", "Author", "111-222", "QA1", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = lendingRecordRepository.save(
                new LendingRecord(book, patron, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));

        LendingRecord returned = bookReturnService.processReturn(record.getId());

        assertEquals(LendingStatus.RETURNED, returned.getStatus());
        assertEquals(LocalDate.now(), returned.getReturnDate());
        assertEquals(0, BigDecimal.ZERO.compareTo(returned.getLateFee()));
    }

    @Test
    void processReturnOverdueSetsStatusReturnedLateWithFee() {
        Book book = bookRepository.save(new Book("Test Book", "Author", "111-222", "QA1", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = lendingRecordRepository.save(
                new LendingRecord(book, patron, LocalDate.now().minusDays(20), LocalDate.now().minusDays(10)));

        LendingRecord returned = bookReturnService.processReturn(record.getId());

        assertEquals(LendingStatus.RETURNED_LATE, returned.getStatus());
        assertEquals(LocalDate.now(), returned.getReturnDate());
        // 10 days overdue at $0.25/day = $2.50
        assertEquals(0, new BigDecimal("2.50").compareTo(returned.getLateFee()));
    }

    @Test
    void lateFeeCalculationUsesBookSpecificRate() {
        Book expensiveBook = bookRepository.save(
                new Book("Rare Book", "Author", "333-444", "QA2", new BigDecimal("0.50")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = lendingRecordRepository.save(
                new LendingRecord(expensiveBook, patron, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5)));

        BigDecimal fee = bookReturnService.calculateLateFee(record);

        // 5 days overdue at $0.50/day = $2.50
        assertEquals(0, new BigDecimal("2.50").compareTo(fee));
    }

    @Test
    void lateFeeIsZeroForNonOverdueBooks() {
        Book book = bookRepository.save(new Book("Test Book", "Author", "111-222", "QA1", new BigDecimal("0.25")));
        Patron patron = patronRepository.save(new Patron("Test Patron", "P-001", BigDecimal.ZERO));
        LendingRecord record = lendingRecordRepository.save(
                new LendingRecord(book, patron, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));

        BigDecimal fee = bookReturnService.calculateLateFee(record);

        assertEquals(0, BigDecimal.ZERO.compareTo(fee));
    }
}
