package com.example.specdriven.patient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

@Route("patients")
@PageTitle("Patients — Triage")
@RolesAllowed("ADMIN")
public class PatientListView extends VerticalLayout {

    private final PatientService patientService;
    private final Grid<Patient> grid;
    private final TextField searchField;

    public PatientListView(PatientService patientService) {
        this.patientService = patientService;
        addClassName("view-content");
        setPadding(true);
        setSpacing(true);

        // Header row
        H2 title = new H2("Patients");
        title.getStyle().set("color", "#2C3E50").set("margin", "0");

        Button newPatientButton = new Button("New Patient", VaadinIcon.PLUS.create());
        newPatientButton.addThemeVariants(ButtonVariant.PRIMARY);
        newPatientButton.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("patients/new")));

        HorizontalLayout header = new HorizontalLayout(title, newPatientButton);
        header.setAlignItems(Alignment.CENTER);
        header.expand(title);
        header.setWidthFull();

        // Grid
        grid = new Grid<>(Patient.class, false);

        // Search
        searchField = new TextField();
        searchField.setPlaceholder("Search by name or date of birth...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidthFull();
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> grid.getDataProvider().refreshAll());
        grid.addColumn(Patient::getLastName).setHeader("Last Name").setSortable(true).setKey("lastName");
        grid.addColumn(Patient::getFirstName).setHeader("First Name").setSortable(true).setKey("firstName");
        grid.addColumn(p -> p.getDateOfBirth() != null
                ? p.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "")
                .setHeader("Date of Birth").setSortable(true).setKey("dateOfBirth");
        grid.addColumn(Patient::getPhone).setHeader("Phone").setKey("phone");
        grid.addColumn(Patient::getEmail).setHeader("Email").setKey("email");

        grid.setItems(query -> {
            Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
            if (!query.getSortOrders().isEmpty()) {
                var order = query.getSortOrders().get(0);
                Sort.Direction dir = order.getDirection() == com.vaadin.flow.data.provider.SortDirection.ASCENDING
                        ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = Sort.by(dir, order.getSorted());
            }
            return patientService.findPatients(
                    searchField.getValue(),
                    PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit(), sort)
            ).stream();
        });

        grid.addItemClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("patients/" + e.getItem().getId())));

        grid.setWidthFull();
        grid.setHeight("100%");

        add(header, searchField, grid);
        setFlexGrow(1, grid);
        setSizeFull();
    }
}
