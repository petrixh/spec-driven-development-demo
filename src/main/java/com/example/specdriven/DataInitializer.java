package com.example.specdriven;

import com.example.specdriven.model.Book;
import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.model.Patron;
import com.example.specdriven.repository.BookRepository;
import com.example.specdriven.repository.LendingRecordRepository;
import com.example.specdriven.repository.PatronRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LendingRecordRepository lendingRecordRepository;

    public DataInitializer(BookRepository bookRepository, PatronRepository patronRepository,
                           LendingRecordRepository lendingRecordRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.lendingRecordRepository = lendingRecordRepository;
    }

    @Override
    public void run(String... args) {
        // Books with different late fee rates
        Book book1 = bookRepository.save(new Book(
                "Designing Data-Intensive Applications", "Martin Kleppmann",
                "978-1-449-37332-0", "QA76.9 .D32", new BigDecimal("0.25")));

        Book book2 = bookRepository.save(new Book(
                "Clean Code", "Robert C. Martin",
                "978-0-13-235088-4", "QA76.76 .C54", new BigDecimal("0.15")));

        Book book3 = bookRepository.save(new Book(
                "The Pragmatic Programmer", "David Thomas & Andrew Hunt",
                "978-0-13-595705-9", "QA76.6 .T48", new BigDecimal("0.50")));

        // Patrons
        Patron patron1 = patronRepository.save(new Patron(
                "Sarah Chen", "LIB-2024-00847", BigDecimal.ZERO));

        Patron patron2 = patronRepository.save(new Patron(
                "James Wilson", "LIB-2024-01234", new BigDecimal("2.50")));

        // Lending records — one overdue, one on time, one not checked out
        lendingRecordRepository.save(new LendingRecord(
                book1, patron1,
                LocalDate.now().minusDays(42), LocalDate.now().minusDays(12)));

        lendingRecordRepository.save(new LendingRecord(
                book2, patron2,
                LocalDate.now().minusDays(7), LocalDate.now().plusDays(14)));

        lendingRecordRepository.save(new LendingRecord(
                book3, patron1,
                LocalDate.now().minusDays(3), LocalDate.now().plusDays(25)));
    }
}
