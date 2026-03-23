package com.example.specdriven;

import com.example.specdriven.patient.Patient;
import com.example.specdriven.patient.PatientRepository;
import com.example.specdriven.patient.Visit;
import com.example.specdriven.patient.VisitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class SampleDataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public SampleDataInitializer(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) {
            return;
        }

        Patient p1 = createPatient("Alice", "Johnson", LocalDate.of(1985, 3, 15), "Female", "555-0101", "alice.johnson@email.com", "123 Oak Street");
        Patient p2 = createPatient("Bob", "Smith", LocalDate.of(1972, 7, 22), "Male", "555-0102", "bob.smith@email.com", "456 Elm Avenue");
        Patient p3 = createPatient("Carol", "Williams", LocalDate.of(1990, 11, 8), "Female", "555-0103", "carol.w@email.com", "789 Pine Road");
        Patient p4 = createPatient("David", "Brown", LocalDate.of(1968, 1, 30), "Male", "555-0104", "d.brown@email.com", "321 Maple Drive");
        Patient p5 = createPatient("Eva", "Martinez", LocalDate.of(1995, 6, 12), "Female", "555-0105", "eva.m@email.com", "654 Cedar Lane");

        createVisit(p1, LocalDate.now(), "Annual checkup", "Dr. Thompson", "Routine examination, all vitals normal");
        createVisit(p1, LocalDate.now().minusDays(90), "Flu symptoms", "Dr. Chen", "Prescribed rest and fluids");
        createVisit(p2, LocalDate.now().minusDays(7), "Back pain", "Dr. Thompson", "Referred to physiotherapy");
        createVisit(p3, LocalDate.now(), "Vaccination", "Dr. Patel", "Flu shot administered");
        createVisit(p4, LocalDate.now().minusDays(30), "Blood work", "Dr. Chen", "Cholesterol levels slightly elevated");
    }

    private Patient createPatient(String firstName, String lastName, LocalDate dob,
                                   String gender, String phone, String email, String address) {
        Patient p = new Patient();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setDateOfBirth(dob);
        p.setGender(gender);
        p.setPhone(phone);
        p.setEmail(email);
        p.setAddress(address);
        p.setCreatedAt(LocalDateTime.now());
        return patientRepository.save(p);
    }

    private void createVisit(Patient patient, LocalDate date, String reason, String doctor, String notes) {
        Visit v = new Visit();
        v.setPatient(patient);
        v.setDate(date);
        v.setReason(reason);
        v.setDoctorName(doctor);
        v.setNotes(notes);
        visitRepository.save(v);
    }
}
