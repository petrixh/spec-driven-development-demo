package com.example.specdriven.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "CAST(p.dateOfBirth AS string) LIKE CONCAT('%', :filter, '%')")
    Page<Patient> findByFilter(@Param("filter") String filter, Pageable pageable);

    List<Patient> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
            String firstName, String lastName, LocalDate dateOfBirth);

    long countByCreatedAtAfter(LocalDateTime dateTime);

    List<Patient> findTop5ByOrderByCreatedAtDesc();
}
