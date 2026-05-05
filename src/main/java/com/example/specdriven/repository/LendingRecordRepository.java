package com.example.specdriven.repository;

import com.example.specdriven.model.Book;
import com.example.specdriven.model.LendingRecord;
import com.example.specdriven.model.LendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LendingRecordRepository extends JpaRepository<LendingRecord, Long> {
    Optional<LendingRecord> findByBookAndStatus(Book book, LendingStatus status);
}
