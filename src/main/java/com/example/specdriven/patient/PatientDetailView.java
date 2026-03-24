package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("patients/:patientId")
@PageTitle("Patient Details — Triage")
@RolesAllowed("ADMIN")
public class PatientDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final PatientService patientService;

    private Patient patient;

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private final ComboBox<String> gender = new ComboBox<>("Gender");
    private final TextField phone = new TextField("Phone");
    private final TextField email = new TextField("Email");
    private final TextArea address = new TextArea("Address");

    private final Button editButton = new Button("Edit", VaadinIcon.EDIT.create());
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button addVisitButton = new Button("Add Visit", VaadinIcon.PLUS.create());

    private final Grid<Visit> visitGrid = new Grid<>(Visit.class, false);

    private final VerticalLayout contentLayout = new VerticalLayout();
    private final Paragraph notFoundMessage = new Paragraph("Patient not found");

    public PatientDetailView(PatientService patientService) {
        this.patientService = patientService;
        addClassName("view-content");
        setPadding(true);
        setSpacing(true);
        setSizeFull();

        notFoundMessage.addClassName("not-found-message");
        notFoundMessage.setVisible(false);

        gender.setItems("Male", "Female", "Other");

        buildLayout();
        setReadOnly(true);

        add(contentLayout, notFoundMessage);
    }

    private void buildLayout() {
        contentLayout.setPadding(false);
        contentLayout.setSpacing(true);
        contentLayout.setSizeFull();

        // Header
        H2 title = new H2("Patient Details");
        title.getStyle().set("color", "#2C3E50").set("margin", "0");

        editButton.addThemeVariants(ButtonVariant.PRIMARY);
        editButton.addClickListener(e -> setReadOnly(false));

        HorizontalLayout header = new HorizontalLayout(title, editButton);
        header.setAlignItems(Alignment.CENTER);
        header.expand(title);
        header.setWidthFull();

        // Form
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("640px", 2)
        );
        formLayout.add(firstName, lastName, dateOfBirth, gender, phone, email, address);
        formLayout.setColspan(address, 2);

        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        saveButton.addClickListener(e -> save());
        cancelButton.addThemeVariants(ButtonVariant.TERTIARY);
        cancelButton.addClickListener(e -> cancelEdit());

        HorizontalLayout formButtons = new HorizontalLayout(saveButton, cancelButton);
        formButtons.setVisible(false);

        VerticalLayout formCard = new VerticalLayout(formLayout, formButtons);
        formCard.addClassName("section-card");
        formCard.setSpacing(true);
        formCard.setPadding(true);

        // Visit history
        H3 visitTitle = new H3("Visit History");
        visitTitle.getStyle().set("color", "#2C3E50").set("margin", "0");

        addVisitButton.addThemeVariants(ButtonVariant.PRIMARY);
        addVisitButton.addClickListener(e -> openAddVisitDialog());

        HorizontalLayout visitHeader = new HorizontalLayout(visitTitle, addVisitButton);
        visitHeader.setAlignItems(Alignment.CENTER);
        visitHeader.expand(visitTitle);
        visitHeader.setWidthFull();

        visitGrid.addColumn(v -> v.getDate() != null
                ? v.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "")
                .setHeader("Date").setSortable(true);
        visitGrid.addColumn(Visit::getReason).setHeader("Reason");
        visitGrid.addColumn(Visit::getDoctorName).setHeader("Doctor");
        visitGrid.addColumn(Visit::getNotes).setHeader("Notes");
        visitGrid.setWidthFull();

        VerticalLayout visitCard = new VerticalLayout(visitHeader, visitGrid);
        visitCard.addClassName("section-card");
        visitCard.setSpacing(true);
        visitCard.setPadding(true);

        contentLayout.add(header, formCard, visitCard);
        contentLayout.setFlexGrow(1, visitCard);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String paramStr = event.getRouteParameters().get("patientId").orElse("");
        try {
            Long patientId = Long.valueOf(paramStr);
            var found = patientService.findById(patientId);
            if (found.isPresent()) {
                this.patient = found.get();
                populateForm();
                refreshVisits();
                contentLayout.setVisible(true);
                notFoundMessage.setVisible(false);
            } else {
                contentLayout.setVisible(false);
                notFoundMessage.setVisible(true);
            }
        } catch (NumberFormatException e) {
            contentLayout.setVisible(false);
            notFoundMessage.setVisible(true);
        }
    }

    private void populateForm() {
        firstName.setValue(patient.getFirstName() != null ? patient.getFirstName() : "");
        lastName.setValue(patient.getLastName() != null ? patient.getLastName() : "");
        dateOfBirth.setValue(patient.getDateOfBirth());
        gender.setValue(patient.getGender());
        phone.setValue(patient.getPhone() != null ? patient.getPhone() : "");
        email.setValue(patient.getEmail() != null ? patient.getEmail() : "");
        address.setValue(patient.getAddress() != null ? patient.getAddress() : "");
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
        // Show/hide save+cancel buttons
        saveButton.getParent().ifPresent(p -> ((HorizontalLayout) p).setVisible(!readOnly));
    }

    private void save() {
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

        // Duplicate detection (excluding this patient)
        if (patientService.isDuplicateExcluding(
                firstName.getValue().trim(), lastName.getValue().trim(),
                dateOfBirth.getValue(), patient.getId())) {
            Notifications.showWarning("A patient with the same name and date of birth already exists");
        }

        patient.setFirstName(firstName.getValue().trim());
        patient.setLastName(lastName.getValue().trim());
        patient.setDateOfBirth(dateOfBirth.getValue());
        patient.setGender(gender.getValue());
        patient.setPhone(phoneValue != null ? phoneValue.trim() : null);
        patient.setEmail(emailValue != null ? emailValue.trim() : null);
        patient.setAddress(address.getValue() != null ? address.getValue().trim() : null);

        patient = patientService.save(patient);
        Notifications.showSuccess("Patient saved successfully");
        setReadOnly(true);
    }

    private void cancelEdit() {
        populateForm();
        setReadOnly(true);
    }

    private void openAddVisitDialog() {
        AddVisitDialog dialog = new AddVisitDialog(visit -> {
            visit.setPatient(patient);
            patientService.saveVisit(visit);
            refreshVisits();
            Notifications.showSuccess("Visit recorded successfully");
        });
        dialog.open();
    }

    private void refreshVisits() {
        List<Visit> visits = patientService.findVisitsByPatientId(patient.getId());
        visitGrid.setItems(visits);
    }
}
