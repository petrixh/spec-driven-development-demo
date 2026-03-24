package com.example.specdriven.patient;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("dashboard")
@PageTitle("Dashboard — Triage")
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {

    private final PatientService patientService;

    public DashboardView(PatientService patientService) {
        this.patientService = patientService;
        addClassName("view-content");
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("Dashboard");
        title.getStyle().set("color", "#2C3E50").set("margin", "0");

        add(title, buildStatsRow(), buildListsRow());
    }

    private HorizontalLayout buildStatsRow() {
        long totalPatients = patientService.countAll();
        long visitsToday = patientService.countVisitsToday();
        long patientsThisMonth = patientService.countRegisteredThisMonth();

        HorizontalLayout stats = new HorizontalLayout(
                buildStatCard(String.valueOf(totalPatients), "Total Patients"),
                buildStatCard(String.valueOf(visitsToday), "Visits Today"),
                buildStatCard(String.valueOf(patientsThisMonth), "New This Month")
        );
        stats.setWidthFull();
        stats.setSpacing(true);
        return stats;
    }

    private Div buildStatCard(String value, String label) {
        Div card = new Div();
        card.addClassName("stat-card");

        Paragraph valuePara = new Paragraph(value);
        valuePara.addClassName("stat-value");

        Paragraph labelPara = new Paragraph(label);
        labelPara.addClassName("stat-label");

        card.add(valuePara, labelPara);
        return card;
    }

    private HorizontalLayout buildListsRow() {
        HorizontalLayout lists = new HorizontalLayout(buildRecentVisits(), buildRecentPatients());
        lists.setWidthFull();
        lists.setSpacing(true);
        lists.setFlexGrow(1, lists.getComponentAt(0));
        lists.setFlexGrow(1, lists.getComponentAt(1));
        return lists;
    }

    private VerticalLayout buildRecentVisits() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("section-card");
        section.setSpacing(true);
        section.setPadding(true);

        H3 heading = new H3("Recent Visits");
        heading.getStyle().set("color", "#2C3E50").set("margin", "0");
        section.add(heading);

        List<Visit> visits = patientService.findRecentVisits();
        if (visits.isEmpty()) {
            section.add(new Paragraph("No visits recorded yet"));
        } else {
            for (Visit visit : visits) {
                Patient p = visit.getPatient();
                String patientName = p.getLastName() + ", " + p.getFirstName();
                String dateStr = visit.getDate() != null
                        ? visit.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";

                Div row = new Div();
                row.getStyle()
                        .set("display", "flex")
                        .set("justify-content", "space-between")
                        .set("align-items", "center")
                        .set("padding", "var(--vaadin-padding-xs) 0")
                        .set("border-bottom", "1px solid #D6E0E8");

                Anchor link = new Anchor("patients/" + p.getId(), patientName);
                link.getStyle().set("color", "#3A7CA5").set("text-decoration", "none");

                Span info = new Span(dateStr + " — " + visit.getReason());
                info.getStyle().set("color", "#6B7D8D").set("font-size", "var(--aura-font-size-s)");

                row.add(link, info);
                section.add(row);
            }
        }

        return section;
    }

    private VerticalLayout buildRecentPatients() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("section-card");
        section.setSpacing(true);
        section.setPadding(true);

        H3 heading = new H3("Recently Registered");
        heading.getStyle().set("color", "#2C3E50").set("margin", "0");
        section.add(heading);

        List<Patient> patients = patientService.findRecentPatients();
        if (patients.isEmpty()) {
            section.add(new Paragraph("No patients registered yet"));
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Patient p : patients) {
                String name = p.getLastName() + ", " + p.getFirstName();
                String dateStr = p.getCreatedAt() != null ? p.getCreatedAt().format(dtf) : "";

                Div row = new Div();
                row.getStyle()
                        .set("display", "flex")
                        .set("justify-content", "space-between")
                        .set("align-items", "center")
                        .set("padding", "var(--vaadin-padding-xs) 0")
                        .set("border-bottom", "1px solid #D6E0E8");

                Anchor link = new Anchor("patients/" + p.getId(), name);
                link.getStyle().set("color", "#3A7CA5").set("text-decoration", "none");

                Span date = new Span(dateStr);
                date.getStyle().set("color", "#6B7D8D").set("font-size", "var(--aura-font-size-s)");

                row.add(link, date);
                section.add(row);
            }
        }

        return section;
    }
}
