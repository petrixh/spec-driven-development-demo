package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;

@Route("patients/new")
@PageTitle("New Patient — Triage")
@RolesAllowed("ADMIN")
public class RegisterPatientView extends VerticalLayout {

    private final PatientService patientService;

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private final ComboBox<String> gender = new ComboBox<>("Gender");
    private final TextField phone = new TextField("Phone");
    private final TextField email = new TextField("Email");
    private final TextArea address = new TextArea("Address");

    public RegisterPatientView(PatientService patientService) {
        this.patientService = patientService;
        addClassName("view-content");
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("New Patient");
        title.getStyle().set("color", "#2C3E50").set("margin", "0");

        firstName.setRequired(true);
        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequired(true);
        lastName.setRequiredIndicatorVisible(true);
        dateOfBirth.setRequired(true);
        dateOfBirth.setRequiredIndicatorVisible(true);

        gender.setItems("Male", "Female", "Other");
        gender.setPlaceholder("Select gender");

        phone.setHelperText("Digits, spaces, dashes, or leading +");

        address.setMaxHeight("150px");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("640px", 2)
        );
        formLayout.add(firstName, lastName, dateOfBirth, gender, phone, email, address);
        formLayout.setColspan(address, 2);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        saveButton.addClickListener(e -> save());

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.TERTIARY);
        cancelButton.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("patients")));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout formCard = new VerticalLayout(formLayout, buttons);
        formCard.addClassName("section-card");
        formCard.setSpacing(true);
        formCard.setPadding(true);

        add(title, formCard);
    }

    private void save() {
        // Validate required fields
        boolean valid = true;

        if (firstName.getValue() == null || firstName.getValue().isBlank()) {
            firstName.setInvalid(true);
            firstName.setErrorMessage("First name is required");
            valid = false;
        } else {
            firstName.setInvalid(false);
        }

        if (lastName.getValue() == null || lastName.getValue().isBlank()) {
            lastName.setInvalid(true);
            lastName.setErrorMessage("Last name is required");
            valid = false;
        } else {
            lastName.setInvalid(false);
        }

        if (dateOfBirth.getValue() == null) {
            dateOfBirth.setInvalid(true);
            dateOfBirth.setErrorMessage("Date of birth is required");
            valid = false;
        } else if (!dateOfBirth.getValue().isBefore(LocalDate.now())) {
            dateOfBirth.setInvalid(true);
            dateOfBirth.setErrorMessage("Date of birth must be in the past");
            valid = false;
        } else {
            dateOfBirth.setInvalid(false);
        }

        // Email validation
        String emailValue = email.getValue();
        if (emailValue != null && !emailValue.isBlank()) {
            if (!emailValue.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                email.setInvalid(true);
                email.setErrorMessage("Invalid email format");
                valid = false;
            } else {
                email.setInvalid(false);
            }
        } else {
            email.setInvalid(false);
        }

        // Phone validation
        String phoneValue = phone.getValue();
        if (phoneValue != null && !phoneValue.isBlank()) {
            if (!phoneValue.matches("^\\+?[\\d\\s\\-]+$")) {
                phone.setInvalid(true);
                phone.setErrorMessage("Phone must contain only digits, spaces, dashes, or a leading +");
                valid = false;
            } else {
                phone.setInvalid(false);
            }
        } else {
            phone.setInvalid(false);
        }

        if (!valid) {
            Notifications.showError("Please fill in the required fields");
            return;
        }

        // Duplicate detection
        if (patientService.isDuplicate(firstName.getValue().trim(), lastName.getValue().trim(), dateOfBirth.getValue())) {
            Notifications.showWarning("A patient with the same name and date of birth already exists");
        }

        Patient patient = new Patient();
        patient.setFirstName(firstName.getValue().trim());
        patient.setLastName(lastName.getValue().trim());
        patient.setDateOfBirth(dateOfBirth.getValue());
        patient.setGender(gender.getValue());
        patient.setPhone(phoneValue != null ? phoneValue.trim() : null);
        patient.setEmail(emailValue != null ? emailValue.trim() : null);
        patient.setAddress(address.getValue() != null ? address.getValue().trim() : null);

        Patient saved = patientService.save(patient);
        Notifications.showSuccess("Patient saved successfully");
        getUI().ifPresent(ui -> ui.navigate("patients/" + saved.getId()));
    }
}
