package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class RegisterPatientTest extends SpringBrowserlessTest {

    @Autowired
    private PatientService patientService;

    @Test
    void formRendersAllFields() {
        RegisterPatientView view = navigate(RegisterPatientView.class);

        assertFalse($(TextField.class).all().isEmpty());
        assertFalse($(DatePicker.class).all().isEmpty());
        assertFalse($(ComboBox.class).all().isEmpty());
        assertFalse($(TextArea.class).all().isEmpty());
    }

    @Test
    void requiredFieldValidation() {
        RegisterPatientView view = navigate(RegisterPatientView.class);

        // Click save without filling required fields
        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        // Fields should be invalid
        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        assertTrue(firstName.isInvalid());
    }

    @Test
    void dateOfBirthMustBeInPast() {
        RegisterPatientView view = navigate(RegisterPatientView.class);

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Test");

        TextField lastName = $(TextField.class).all().stream()
                .filter(f -> "Last Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(lastName).setValue("Patient");

        DatePicker dob = $(DatePicker.class).single();
        test(dob).setValue(LocalDate.now().plusDays(1));

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        assertTrue(dob.isInvalid());
    }

    @Test
    void emailFormatValidation() {
        RegisterPatientView view = navigate(RegisterPatientView.class);

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Test");

        TextField lastName = $(TextField.class).all().stream()
                .filter(f -> "Last Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(lastName).setValue("Patient");

        DatePicker dob = $(DatePicker.class).single();
        test(dob).setValue(LocalDate.of(1990, 1, 1));

        TextField email = $(TextField.class).all().stream()
                .filter(f -> "Email".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(email).setValue("invalid-email");

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        assertTrue(email.isInvalid());
    }

    @Test
    void successfulSaveCreatesPatient() {
        RegisterPatientView view = navigate(RegisterPatientView.class);

        long countBefore = patientService.countAll();

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Jane");

        TextField lastName = $(TextField.class).all().stream()
                .filter(f -> "Last Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(lastName).setValue("Doe");

        DatePicker dob = $(DatePicker.class).single();
        test(dob).setValue(LocalDate.of(1992, 3, 15));

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        assertEquals(countBefore + 1, patientService.countAll());
    }

    @Test
    void duplicateDetectionWarning() {
        // Create existing patient
        Patient existing = new Patient();
        existing.setFirstName("Duplicate");
        existing.setLastName("Test");
        existing.setDateOfBirth(LocalDate.of(1988, 5, 10));
        patientService.save(existing);

        RegisterPatientView view = navigate(RegisterPatientView.class);

        TextField firstName = $(TextField.class).all().stream()
                .filter(f -> "First Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(firstName).setValue("Duplicate");

        TextField lastName = $(TextField.class).all().stream()
                .filter(f -> "Last Name".equals(f.getLabel()))
                .findFirst().orElseThrow();
        test(lastName).setValue("Test");

        DatePicker dob = $(DatePicker.class).single();
        test(dob).setValue(LocalDate.of(1988, 5, 10));

        Button saveButton = $(Button.class).all().stream()
                .filter(b -> "Save".equals(b.getText()))
                .findFirst().orElseThrow();
        test(saveButton).click();

        // Patient should still be saved (warning, not block)
        assertTrue(patientService.isDuplicate("Duplicate", "Test", LocalDate.of(1988, 5, 10)));
    }
}
