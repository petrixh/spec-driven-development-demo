package com.example.specdriven.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientService(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    public Page<Patient> findAll(String filter, Pageable pageable) {
        if (filter == null || filter.isBlank()) {
            return patientRepository.findAll(pageable);
        }
        return patientRepository.findByFilter(filter.trim(), pageable);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Transactional
    public Patient save(Patient patient) {
        if (patient.getCreatedAt() == null) {
            patient.setCreatedAt(LocalDateTime.now());
        }
        return patientRepository.save(patient);
    }

    public boolean hasDuplicate(String firstName, String lastName, LocalDate dateOfBirth, Long excludeId) {
        List<Patient> matches = patientRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(firstName, lastName, dateOfBirth);
        if (excludeId != null) {
            matches.removeIf(p -> p.getId().equals(excludeId));
        }
        return !matches.isEmpty();
    }

    public List<Visit> findVisitsByPatientId(Long patientId) {
        return visitRepository.findByPatientIdOrderByDateDesc(patientId);
    }

    @Transactional
    public Visit saveVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public long countPatients() {
        return patientRepository.count();
    }

    public long countVisitsToday() {
        return visitRepository.countByDate(LocalDate.now());
    }

    public long countPatientsThisMonth() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return patientRepository.countByCreatedAtAfter(startOfMonth);
    }

    public List<Visit> findRecentVisits() {
        return visitRepository.findTop10ByOrderByDateDesc();
    }

    public List<Patient> findRecentPatients() {
        return patientRepository.findTop5ByOrderByCreatedAtDesc();
    }
}
