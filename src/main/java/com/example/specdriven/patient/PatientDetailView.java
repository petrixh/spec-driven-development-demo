package com.example.specdriven.patient;

import com.example.specdriven.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.List;

@Route(value = "patients", layout = MainLayout.class)
@PageTitle("Patient Details — Triage")
@RolesAllowed("ADMIN")
public class PatientDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final PatientService patientService;
    private final Binder<Patient> binder = new Binder<>(Patient.class);
    private Patient patient;

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private final ComboBox<String> gender = new ComboBox<>("Gender");
    private final TextField phone = new TextField("Phone");
    private final TextField email = new TextField("Email");
    private final TextArea address = new TextArea("Address");

    private final Button editButton = new Button("Edit");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button addVisitButton = new Button("Add Visit");

    private final Grid<Visit> visitGrid = new Grid<>(Visit.class, false);
    private final H2 title = new H2();

    public PatientDetailView(PatientService patientService) {
        this.patientService = patientService;

        addClassName("content-view");
        setPadding(true);

        configureBinder();
        configureVisitGrid();
    }

    @Override
    public void setParameter(BeforeEvent event, Long patientId) {
        var optional = patientService.findById(patientId);
        if (optional.isEmpty()) {
            removeAll();
            Span notFound = new Span("Patient not found");
            notFound.addClassName("empty-state");
            add(notFound);
            return;
        }
        this.patient = optional.get();
        buildLayout();
        populateForm();
        loadVisits();
        setReadOnly(true);
    }

    private void buildLayout() {
        removeAll();

        title.setText(patient.getFirstName() + " " + patient.getLastName());
        title.addClassName("page-title");

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

        configureButtons();

        var buttonBar = new HorizontalLayout(editButton, saveButton, cancelButton);
        buttonBar.setSpacing(true);

        add(title, formCard, buttonBar);

        // Visit section
        H3 visitTitle = new H3("Visit History");
        visitTitle.addClassName("section-title");
        var visitHeader = new HorizontalLayout(visitTitle, addVisitButton);
        visitHeader.setAlignItems(Alignment.BASELINE);
        visitHeader.setSpacing(true);
        add(visitHeader, visitGrid);
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

        gender.setItems("Male", "Female", "Other");
        dateOfBirth.setMax(LocalDate.now());
        phone.setHelperText("Digits, spaces, dashes, or leading +");
        email.setHelperText("e.g. jane@example.com");
    }

    private void configureButtons() {
        editButton.addClickListener(e -> setReadOnly(false));

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> savePatient());

        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(e -> {
            populateForm();
            setReadOnly(true);
        });

        addVisitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addVisitButton.addClickListener(e -> openVisitDialog());
    }

    private void configureVisitGrid() {
        visitGrid.addColumn(Visit::getDate).setHeader("Date").setSortable(true);
        visitGrid.addColumn(Visit::getReason).setHeader("Reason");
        visitGrid.addColumn(Visit::getDoctorName).setHeader("Doctor");
        visitGrid.addColumn(Visit::getNotes).setHeader("Notes").setFlexGrow(2);
        visitGrid.setWidthFull();
        visitGrid.setHeight("300px");
    }

    private void populateForm() {
        binder.readBean(patient);
    }

    private void setReadOnly(boolean readOnly) {
        firstName.setReadOnly(readOnly);
        lastName.setReadOnly(readOnly);
        dateOfBirth.setReadOnly(readOnly);
        gender.setReadOnly(readOnly);
        phone.setReadOnly(readOnly);
        email.setReadOnly(readOnly);
        address.setReadOnly(readOnly);

        editButton.setVisible(readOnly);
        saveButton.setVisible(!readOnly);
        cancelButton.setVisible(!readOnly);
    }

    private void savePatient() {
        try {
            binder.writeBean(patient);
        } catch (ValidationException e) {
            return;
        }

        patientService.save(patient);
        title.setText(patient.getFirstName() + " " + patient.getLastName());
        Notification.show("Patient updated successfully", 3000, Notification.Position.BOTTOM_STRETCH)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        setReadOnly(true);
    }

    private void loadVisits() {
        List<Visit> visits = patientService.findVisitsByPatientId(patient.getId());
        visitGrid.setItems(visits);
    }

    void openVisitDialog() {
        VisitDialog dialog = new VisitDialog(patient, patientService, this::loadVisits);
        dialog.open();
    }
}
