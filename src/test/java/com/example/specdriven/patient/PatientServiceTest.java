package com.example.specdriven.patient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Test
    void saveAndFindPatient() {
        Patient p = new Patient();
        p.setFirstName("Service");
        p.setLastName("Test");
        p.setDateOfBirth(LocalDate.of(1990, 5, 15));
        Patient saved = patientService.save(p);

        assertNotNull(saved.getId());
        assertTrue(patientService.findById(saved.getId()).isPresent());
    }

    @Test
    void findPatientsWithFilter() {
        Patient p = new Patient();
        p.setFirstName("Filterable");
        p.setLastName("Query");
        p.setDateOfBirth(LocalDate.of(1985, 3, 10));
        patientService.save(p);

        Page<Patient> results = patientService.findPatients("Filterable",
                PageRequest.of(0, 10, Sort.by("lastName")));
        assertTrue(results.getTotalElements() > 0);
    }

    @Test
    void duplicateDetection() {
        Patient p = new Patient();
        p.setFirstName("Dup");
        p.setLastName("Check");
        p.setDateOfBirth(LocalDate.of(1975, 12, 25));
        patientService.save(p);

        assertTrue(patientService.isDuplicate("Dup", "Check", LocalDate.of(1975, 12, 25)));
        assertFalse(patientService.isDuplicate("Dup", "Check", LocalDate.of(1975, 12, 26)));
    }

    @Test
    void saveAndFindVisit() {
        Patient p = new Patient();
        p.setFirstName("VisitService");
        p.setLastName("Test");
        p.setDateOfBirth(LocalDate.of(2000, 1, 1));
        p = patientService.save(p);

        Visit v = new Visit();
        v.setDate(LocalDate.now());
        v.setReason("Test visit");
        v.setDoctorName("Dr. Test");
        v.setPatient(p);
        patientService.saveVisit(v);

        List<Visit> visits = patientService.findVisitsByPatientId(p.getId());
        assertEquals(1, visits.size());
        assertEquals("Test visit", visits.get(0).getReason());
    }

    @Test
    void countOperations() {
        long total = patientService.countAll();
        assertTrue(total >= 0);

        long visitsToday = patientService.countVisitsToday();
        assertTrue(visitsToday >= 0);

        long thisMonth = patientService.countRegisteredThisMonth();
        assertTrue(thisMonth >= 0);
    }

    @Test
    void recentPatientsAndVisits() {
        List<Patient> recentPatients = patientService.findRecentPatients();
        assertNotNull(recentPatients);
        assertTrue(recentPatients.size() <= 5);

        List<Visit> recentVisits = patientService.findRecentVisits();
        assertNotNull(recentVisits);
        assertTrue(recentVisits.size() <= 10);
    }
}
