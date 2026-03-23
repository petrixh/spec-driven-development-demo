package com.example.specdriven.dashboard;

import com.example.specdriven.MainLayout;
import com.example.specdriven.patient.Patient;
import com.example.specdriven.patient.PatientService;
import com.example.specdriven.patient.Visit;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard — Triage")
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {

    private final PatientService patientService;

    public DashboardView(PatientService patientService) {
        this.patientService = patientService;

        setPadding(true);
        setSpacing(true);

        add(new H2("Dashboard"));
        add(createStatCards());
        add(createRecentSections());
    }

    private HorizontalLayout createStatCards() {
        long totalPatients = patientService.countPatients();
        long visitsToday = patientService.countVisitsToday();
        long patientsThisMonth = patientService.countPatientsThisMonth();

        var stats = new HorizontalLayout(
                createStatCard(String.valueOf(totalPatients), "Total Patients"),
                createStatCard(String.valueOf(visitsToday), "Visits Today"),
                createStatCard(String.valueOf(patientsThisMonth), "New This Month")
        );
        stats.setWidthFull();
        stats.setSpacing(true);
        return stats;
    }

    private VerticalLayout createStatCard(String value, String label) {
        Span valueSpan = new Span(value);
        valueSpan.addClassName("stat-value");

        Span labelSpan = new Span(label);
        labelSpan.addClassName("stat-label");

        var card = new VerticalLayout(valueSpan, labelSpan);
        card.addClassName("stat-card");
        card.setAlignItems(Alignment.CENTER);
        card.setPadding(true);
        card.setFlexGrow(1);
        card.setWidth("33%");
        return card;
    }

    private HorizontalLayout createRecentSections() {
        var layout = new HorizontalLayout(createRecentVisits(), createRecentPatients());
        layout.setWidthFull();
        layout.setSpacing(true);
        return layout;
    }

    private VerticalLayout createRecentVisits() {
        var section = new VerticalLayout();
        section.add(new H3("Recent Visits"));

        List<Visit> visits = patientService.findRecentVisits();

        Grid<Visit> grid = new Grid<>(Visit.class, false);
        grid.addColumn(v -> v.getPatient().getLastName() + ", " + v.getPatient().getFirstName())
                .setHeader("Patient");
        grid.addColumn(Visit::getDate).setHeader("Date");
        grid.addColumn(Visit::getReason).setHeader("Reason");
        grid.setItems(visits);
        grid.setHeight("350px");
        grid.setWidthFull();
        grid.addItemClickListener(e ->
                UI.getCurrent().navigate("patients/" + e.getItem().getPatient().getId()));

        section.add(grid);
        section.setWidth("60%");
        return section;
    }

    private VerticalLayout createRecentPatients() {
        var section = new VerticalLayout();
        section.add(new H3("Recently Registered"));

        List<Patient> patients = patientService.findRecentPatients();

        Grid<Patient> grid = new Grid<>(Patient.class, false);
        grid.addColumn(p -> p.getLastName() + ", " + p.getFirstName())
                .setHeader("Name");
        grid.addColumn(p -> p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .setHeader("Registered");
        grid.setItems(patients);
        grid.setHeight("350px");
        grid.setWidthFull();
        grid.addItemClickListener(e ->
                UI.getCurrent().navigate("patients/" + e.getItem().getId()));

        section.add(grid);
        section.setWidth("40%");
        return section;
    }
}
