package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class RecordVisitTest extends SpringBrowserlessTest {

    @Autowired
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        Patient p = new Patient();
        p.setFirstName("Visit");
        p.setLastName("TestPatient");
        p.setDateOfBirth(LocalDate.of(1975, 8, 12));
        testPatient = patientService.save(p);
    }

    @Test
    void addVisitOpensDialog() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button addVisitButton = $(Button.class).all().stream()
                .filter(b -> "Add Visit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(addVisitButton).click();

        List<Dialog> dialogs = $(Dialog.class).all();
        assertFalse(dialogs.isEmpty());
    }

    @Test
    void dateDefaultsToToday() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button addVisitButton = $(Button.class).all().stream()
                .filter(b -> "Add Visit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(addVisitButton).click();

        Dialog dialog = $(Dialog.class).single();
        DatePicker datePicker = test(dialog).find(DatePicker.class).single();
        assertEquals(LocalDate.now(), datePicker.getValue());
    }

    @Test
    void requiredFieldValidation() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button addVisitButton = $(Button.class).all().stream()
                .filter(b -> "Add Visit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(addVisitButton).click();

        Dialog dialog = $(Dialog.class).single();

        // Clear date using component API directly (tester doesn't accept null)
        DatePicker datePicker = test(dialog).find(DatePicker.class).single();
        datePicker.setValue(null);

        // Click save in dialog
        Button dialogSave = test(dialog).find(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(dialogSave).click();

        assertTrue(datePicker.isInvalid());
    }

    @Test
    void futureDateIsRejected() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button addVisitButton = $(Button.class).all().stream()
                .filter(b -> "Add Visit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(addVisitButton).click();

        Dialog dialog = $(Dialog.class).single();

        DatePicker datePicker = test(dialog).find(DatePicker.class).single();
        test(datePicker).setValue(LocalDate.now().plusDays(5));

        TextField reason = test(dialog).find(TextField.class).all().stream()
                .filter(f -> "Reason".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(reason).setValue("Test reason");

        Button dialogSave = test(dialog).find(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(dialogSave).click();

        assertTrue(datePicker.isInvalid());
    }

    @Test
    void successfulSaveAddsVisitToHistory() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        long visitsBefore = patientService.findVisitsByPatientId(testPatient.getId()).size();

        Button addVisitButton = $(Button.class).all().stream()
                .filter(b -> "Add Visit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(addVisitButton).click();

        Dialog dialog = $(Dialog.class).single();

        TextField reason = test(dialog).find(TextField.class).all().stream()
                .filter(f -> "Reason".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(reason).setValue("Annual checkup");

        Button dialogSave = test(dialog).find(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(dialogSave).click();

        long visitsAfter = patientService.findVisitsByPatientId(testPatient.getId()).size();
        assertEquals(visitsBefore + 1, visitsAfter);
    }

    @Test
    void visitListSortedByDateDescending() {
        // Add visits with different dates
        Visit v1 = new Visit();
        v1.setDate(LocalDate.of(2025, 1, 1));
        v1.setReason("First visit");
        v1.setPatient(testPatient);
        patientService.saveVisit(v1);

        Visit v2 = new Visit();
        v2.setDate(LocalDate.of(2025, 6, 15));
        v2.setReason("Second visit");
        v2.setPatient(testPatient);
        patientService.saveVisit(v2);

        List<Visit> visits = patientService.findVisitsByPatientId(testPatient.getId());
        assertTrue(visits.size() >= 2);
        assertTrue(visits.get(0).getDate().isAfter(visits.get(1).getDate()) ||
                   visits.get(0).getDate().isEqual(visits.get(1).getDate()));
    }
}
