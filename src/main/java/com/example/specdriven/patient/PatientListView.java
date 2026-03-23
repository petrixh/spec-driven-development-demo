package com.example.specdriven.patient;

import com.example.specdriven.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Route(value = "patients", layout = MainLayout.class)
@PageTitle("Patients — Triage")
@RolesAllowed("ADMIN")
public class PatientListView extends VerticalLayout {

    private final PatientService patientService;
    private final Grid<Patient> grid = new Grid<>(Patient.class, false);
    private final TextField searchField = new TextField();

    public PatientListView(PatientService patientService) {
        this.patientService = patientService;

        setSizeFull();
        setPadding(true);

        configureGrid();
        add(createToolbar(), grid);
        updateList();
    }

    private HorizontalLayout createToolbar() {
        searchField.setPlaceholder("Search by name or date of birth...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> updateList());
        searchField.setWidthFull();

        Button addButton = new Button("New Patient");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> UI.getCurrent().navigate("patients/new"));

        var toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setWidthFull();
        toolbar.setFlexGrow(1, searchField);
        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }

    private void configureGrid() {
        grid.addColumn(Patient::getLastName).setHeader("Last Name").setSortable(true).setKey("lastName");
        grid.addColumn(Patient::getFirstName).setHeader("First Name").setSortable(true).setKey("firstName");
        grid.addColumn(Patient::getDateOfBirth).setHeader("Date of Birth").setSortable(true).setKey("dateOfBirth");
        grid.addColumn(Patient::getPhone).setHeader("Phone").setKey("phone");
        grid.addColumn(Patient::getEmail).setHeader("Email").setKey("email");

        grid.setSizeFull();
        grid.addItemClickListener(e ->
                UI.getCurrent().navigate("patients/" + e.getItem().getId()));
    }

    private void updateList() {
        String filter = searchField.getValue();
        var page = patientService.findAll(filter,
                PageRequest.of(0, 200, Sort.by("lastName").ascending()));
        if (page.isEmpty()) {
            grid.setItems(java.util.List.of());
        } else {
            grid.setItems(page.getContent());
        }
    }
}
