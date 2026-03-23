package com.example.specdriven.patient;

import com.example.specdriven.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;

@Route(value = "patients/new", layout = MainLayout.class)
@PageTitle("New Patient — Triage")
@RolesAllowed("ADMIN")
public class PatientFormView extends VerticalLayout {

    private final PatientService patientService;
    private final Binder<Patient> binder = new Binder<>(Patient.class);

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private final ComboBox<String> gender = new ComboBox<>("Gender");
    private final TextField phone = new TextField("Phone");
    private final TextField email = new TextField("Email");
    private final TextArea address = new TextArea("Address");

    public PatientFormView(PatientService patientService) {
        this.patientService = patientService;

        addClassName("content-view");
        setPadding(true);

        H2 title = new H2("New Patient");
        title.addClassName("page-title");

        configureForm();
        configureBinder();

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("640px", 2));
        formLayout.add(firstName, lastName, dateOfBirth, gender, phone, email);
        formLayout.setColspan(email, 2);

        address.setWidthFull();

        var formCard = new VerticalLayout(formLayout, address);
        formCard.addClassName("surface-card");
        formCard.setPadding(true);
        formCard.setSpacing(true);

        add(title, formCard, createButtons());
    }

    private void configureForm() {
        firstName.setRequired(true);
        lastName.setRequired(true);
        dateOfBirth.setRequired(true);
        dateOfBirth.setMax(LocalDate.now());

        gender.setItems("Male", "Female", "Other");
        gender.setPlaceholder("Select gender");

        phone.setHelperText("Digits, spaces, dashes, or leading +");
        email.setHelperText("e.g. jane@example.com");
        address.setPlaceholder("Street, city, zip");
        address.setMaxLength(500);
    }

    private void configureBinder() {
        binder.forField(firstName)
                .asRequired("First name is required")
                .bind(Patient::getFirstName, Patient::setFirstName);

        binder.forField(lastName)
                .asRequired("Last name is required")
                .bind(Patient::getLastName, Patient::setLastName);

        binder.forField(dateOfBirth)
                .asRequired("Date of birth is required")
                .withValidator(dob -> dob.isBefore(LocalDate.now()),
                        "Date of birth must be in the past")
                .bind(Patient::getDateOfBirth, Patient::setDateOfBirth);

        binder.forField(gender)
                .bind(Patient::getGender, Patient::setGender);

        binder.forField(phone)
                .withValidator(new RegexpValidator(
                        "Phone must contain only digits, spaces, dashes, or a leading +",
                        "^$|^\\+?[\\d\\s-]+$"))
                .bind(Patient::getPhone, Patient::setPhone);

        binder.forField(email)
                .withValidator(value -> value == null || value.isBlank() || new EmailValidator("").apply(value, null).isError() == false,
                        "Please enter a valid email address")
                .bind(Patient::getEmail, Patient::setEmail);

        binder.forField(address)
                .bind(Patient::getAddress, Patient::setAddress);
    }

    private HorizontalLayout createButtons() {
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> savePatient());

        Button cancel = new Button("Cancel");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> UI.getCurrent().navigate("patients"));

        var layout = new HorizontalLayout(save, cancel);
        layout.setSpacing(true);
        return layout;
    }

    private void savePatient() {
        Patient patient = new Patient();
        try {
            binder.writeBean(patient);
        } catch (ValidationException e) {
            return;
        }

        if (patientService.hasDuplicate(patient.getFirstName(), patient.getLastName(),
                patient.getDateOfBirth(), null)) {
            Notification.show("Warning: A patient with the same name and date of birth already exists.",
                            5000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING);
        }

        Patient saved = patientService.save(patient);
        Notification.show("Patient saved successfully", 3000, Notification.Position.BOTTOM_STRETCH)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().navigate("patients/" + saved.getId());
    }
}
