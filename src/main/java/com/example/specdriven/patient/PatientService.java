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
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientService(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @Transactional(readOnly = true)
    public Page<Patient> findPatients(String filter, Pageable pageable) {
        if (filter == null || filter.isBlank()) {
            return patientRepository.findAll(pageable);
        }
        return patientRepository.findByFilter(filter.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(String firstName, String lastName, LocalDate dateOfBirth) {
        return !patientRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(firstName, lastName, dateOfBirth)
                .isEmpty();
    }

    @Transactional(readOnly = true)
    public boolean isDuplicateExcluding(String firstName, String lastName, LocalDate dateOfBirth, Long excludeId) {
        return patientRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(firstName, lastName, dateOfBirth)
                .stream()
                .anyMatch(p -> !p.getId().equals(excludeId));
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return patientRepository.count();
    }

    @Transactional(readOnly = true)
    public long countRegisteredThisMonth() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return patientRepository.countByCreatedAtAfter(startOfMonth);
    }

    @Transactional(readOnly = true)
    public List<Patient> findRecentPatients() {
        return patientRepository.findTop5ByOrderByCreatedAtDesc();
    }

    // Visit operations

    public Visit saveVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    @Transactional(readOnly = true)
    public List<Visit> findVisitsByPatientId(Long patientId) {
        return visitRepository.findByPatientIdOrderByDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public long countVisitsToday() {
        return visitRepository.countByDate(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Visit> findRecentVisits() {
        return visitRepository.findTop10WithPatient();
    }
}
