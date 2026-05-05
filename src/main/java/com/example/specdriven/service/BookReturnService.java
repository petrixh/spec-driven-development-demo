package com.example.specdriven.service;

import com.example.specdriven.model.Book;
import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.model.LendingStatus;
import com.example.specdriven.repository.BookRepository;
import com.example.specdriven.repository.LendingRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookReturnService {

    private final BookRepository bookRepository;
    private final LendingRecordRepository lendingRecordRepository;

    public BookReturnService(BookRepository bookRepository, LendingRecordRepository lendingRecordRepository) {
        this.bookRepository = bookRepository;
        this.lendingRecordRepository = lendingRecordRepository;
    }

    public LendingRecord lookupActiveRecord(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No book found with ISBN: " + isbn));

        return lendingRecordRepository.findByBookAndStatus(book, LendingStatus.CHECKED_OUT)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active lending record found for: " + book.getTitle()));
    }

    @Transactional
    public LendingRecord processReturn(Long lendingRecordId) {
        LendingRecord record = lendingRecordRepository.findById(lendingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Lending record not found"));

        record.setReturnDate(LocalDate.now());

        if (LocalDate.now().isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            BigDecimal fee = record.getBook().getLateFeePerDay().multiply(BigDecimal.valueOf(daysLate));
            record.setLateFee(fee);
            record.setStatus(LendingStatus.RETURNED_LATE);
        } else {
            record.setLateFee(BigDecimal.ZERO);
            record.setStatus(LendingStatus.RETURNED);
        }

        return lendingRecordRepository.save(record);
    }

    public BigDecimal calculateLateFee(LendingRecord record) {
        if (!record.isOverdue()) {
            return BigDecimal.ZERO;
        }
        long daysLate = record.daysOverdue();
        return record.getBook().getLateFeePerDay().multiply(BigDecimal.valueOf(daysLate));
    }
}
