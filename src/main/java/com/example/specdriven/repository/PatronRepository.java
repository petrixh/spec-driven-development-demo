package com.example.specdriven.repository;

import com.example.specdriven.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
