package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.time.LocalDate;

public class VisitDialog extends Dialog {

    private final Patient patient;
    private final PatientService patientService;
    private final Runnable onSave;

    private final DatePicker date = new DatePicker("Date");
    private final TextField reason = new TextField("Reason");
    private final TextField doctorName = new TextField("Doctor Name");
    private final TextArea notes = new TextArea("Notes");

    private final Binder<Visit> binder = new Binder<>(Visit.class);

    public VisitDialog(Patient patient, PatientService patientService, Runnable onSave) {
        this.patient = patient;
        this.patientService = patientService;
        this.onSave = onSave;

        setHeaderTitle("Add Visit");
        setWidth("500px");

        configureBinder();
        buildLayout();

        date.setValue(LocalDate.now());
    }

    private void configureBinder() {
        date.setMax(LocalDate.now());
        date.setRequired(true);
        reason.setRequired(true);

        binder.forField(date)
                .asRequired("Date is required")
                .withValidator(d -> !d.isAfter(LocalDate.now()),
                        "Visit date cannot be in the future")
                .bind(Visit::getDate, Visit::setDate);

        binder.forField(reason)
                .asRequired("Reason is required")
                .bind(Visit::getReason, Visit::setReason);

        binder.forField(doctorName)
                .bind(Visit::getDoctorName, Visit::setDoctorName);

        binder.forField(notes)
                .bind(Visit::getNotes, Visit::setNotes);
    }

    private void buildLayout() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.add(date, reason, doctorName, notes);
        notes.setWidthFull();
        notes.setMaxLength(2000);

        add(form);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> saveVisit());

        Button cancel = new Button("Cancel");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> close());

        getFooter().add(cancel, save);
    }

    private void saveVisit() {
        Visit visit = new Visit();
        try {
            binder.writeBean(visit);
        } catch (ValidationException e) {
            return;
        }

        visit.setPatient(patient);
        patientService.saveVisit(visit);
        Notification.show("Visit saved successfully", 3000, Notification.Position.BOTTOM_STRETCH)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        onSave.run();
        close();
    }
}
