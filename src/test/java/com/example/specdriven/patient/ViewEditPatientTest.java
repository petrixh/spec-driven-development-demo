package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class ViewEditPatientTest extends SpringBrowserlessTest {

    @Autowired
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        Patient p = new Patient();
        p.setFirstName("John");
        p.setLastName("Smith");
        p.setDateOfBirth(LocalDate.of(1980, 3, 25));
        p.setPhone("555-1234");
        p.setEmail("john@example.com");
        testPatient = patientService.save(p);

        // Add a visit
        Visit v = new Visit();
        v.setDate(LocalDate.now());
        v.setReason("Checkup");
        v.setDoctorName("Dr. Jones");
        v.setPatient(testPatient);
        patientService.saveVisit(v);
    }

    @Test
    void patientDetailsDisplayInReadOnlyMode() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        assertEquals("John", firstName.getValue());
        assertTrue(firstName.isReadOnly());
    }

    @Test
    void editModeEnablesFormFields() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button editButton = $(Button.class).all().stream()
                .filter(b -> "Edit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(editButton).click();

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        assertFalse(firstName.isReadOnly());
    }

    @Test
    void savingValidChangesUpdatesPatient() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button editButton = $(Button.class).all().stream()
                .filter(b -> "Edit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(editButton).click();

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Jonathan");

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        Patient updated = patientService.findById(testPatient.getId()).orElseThrow();
        assertEquals("Jonathan", updated.getFirstName());
    }

    @Test
    void validationErrorsForInvalidInput() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button editButton = $(Button.class).all().stream()
                .filter(b -> "Edit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(editButton).click();

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("");

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        assertTrue(firstName.isInvalid());
    }

    @Test
    void cancelDiscardsUnsavedChanges() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        Button editButton = $(Button.class).all().stream()
                .filter(b -> "Edit".equals(b.getText()))
                .findFirst().orElseThrow();
        test(editButton).click();

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Changed");

        Button cancelButton = $(Button.class).all().stream()
                .filter(b -> "Cancel".equals(b.getText()))
                .findFirst().orElseThrow();
        test(cancelButton).click();

        assertEquals("John", firstName.getValue());
        assertTrue(firstName.isReadOnly());
    }

    @Test
    void visitHistoryIsDisplayed() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", String.valueOf(testPatient.getId())));

        // Should have at least one Grid for visit history
        var grids = $(Grid.class).all();
        assertFalse(grids.isEmpty());
    }

    @Test
    void notFoundMessageForInvalidPatientId() {
        PatientDetailView view = navigate(PatientDetailView.class, java.util.Map.of("patientId", "99999"));

        Paragraph notFound = $(Paragraph.class).all().stream()
                .filter(p -> "Patient not found".equals(p.getText()))
                .findFirst().orElse(null);
        assertNotNull(notFound);
        assertTrue(notFound.isVisible());
    }
}
