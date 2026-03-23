package com.example.specdriven.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByPatientIdOrderByDateDesc(Long patientId);

    long countByDate(LocalDate date);

    List<Visit> findTop10ByOrderByDateDesc();
}
