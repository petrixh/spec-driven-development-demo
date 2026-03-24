package com.example.specdriven.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.function.Consumer;

public class AddVisitDialog extends Dialog {

    private final DatePicker date = new DatePicker("Date");
    private final TextField reason = new TextField("Reason");
    private final TextField doctorName = new TextField("Doctor Name");
    private final TextArea notes = new TextArea("Notes");

    public AddVisitDialog(Consumer<Visit> onSave) {
        setHeaderTitle("Add Visit");
        setWidth("500px");

        // Default date to today
        date.setValue(LocalDate.now());
        date.setRequired(true);
        date.setRequiredIndicatorVisible(true);

        reason.setRequired(true);
        reason.setRequiredIndicatorVisible(true);
        reason.setWidthFull();

        doctorName.setWidthFull();
        notes.setWidthFull();
        notes.setMaxHeight("150px");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.add(date, reason, doctorName, notes);

        add(formLayout);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        saveButton.addClickListener(e -> {
            if (validate()) {
                Visit visit = new Visit();
                visit.setDate(date.getValue());
                visit.setReason(reason.getValue().trim());
                visit.setDoctorName(doctorName.getValue() != null ? doctorName.getValue().trim() : null);
                visit.setNotes(notes.getValue() != null ? notes.getValue().trim() : null);
                onSave.accept(visit);
                close();
            }
        });

        Button cancelButton = new Button("Cancel", e -> close());
        cancelButton.addThemeVariants(ButtonVariant.TERTIARY);

        getFooter().add(cancelButton, saveButton);
    }

    private boolean validate() {
        boolean valid = true;

        if (date.getValue() == null) {
            date.setInvalid(true);
            date.setErrorMessage("Date is required");
            valid = false;
        } else if (date.getValue().isAfter(LocalDate.now())) {
            date.setInvalid(true);
            date.setErrorMessage("Visit date cannot be in the future");
            valid = false;
        } else {
            date.setInvalid(false);
        }

        if (reason.getValue() == null || reason.getValue().isBlank()) {
            reason.setInvalid(true);
            reason.setErrorMessage("Reason is required");
            valid = false;
        } else {
            reason.setInvalid(false);
        }

        if (!valid) {
            Notifications.showError("Please fill in the required fields");
        }

        return valid;
    }
}
