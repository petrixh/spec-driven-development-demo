package com.example.specdriven.patient;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class DashboardTest extends SpringBrowserlessTest {

    @Autowired
    private PatientService patientService;

    @Test
    void statisticsAreDisplayed() {
        DashboardView view = navigate(DashboardView.class);

        // Check that stat cards exist with values
        var paragraphs = $(Paragraph.class).all();
        // Should have stat values and labels
        assertTrue(paragraphs.stream().anyMatch(p -> "Total Patients".equals(p.getText())));
        assertTrue(paragraphs.stream().anyMatch(p -> "Visits Today".equals(p.getText())));
        assertTrue(paragraphs.stream().anyMatch(p -> "New This Month".equals(p.getText())));
    }

    @Test
    void dashboardWorksWithEmptyData() {
        DashboardView view = navigate(DashboardView.class);
        // Should not throw, should display "0" for stats and empty messages for lists
        assertNotNull(view);
        H2 title = $(H2.class).single();
        assertEquals("Dashboard", title.getText());
    }

    @Test
    void recentVisitsListShown() {
        // Create patient and visit
        Patient p = new Patient();
        p.setFirstName("Dashboard");
        p.setLastName("Test");
        p.setDateOfBirth(LocalDate.of(1995, 4, 20));
        p = patientService.save(p);

        Visit v = new Visit();
        v.setDate(LocalDate.now());
        v.setReason("Dashboard visit test");
        v.setPatient(p);
        patientService.saveVisit(v);

        DashboardView view = navigate(DashboardView.class);

        // Should have at least one Anchor for the patient link
        var anchors = $(Anchor.class).all();
        assertTrue(anchors.stream().anyMatch(a -> a.getText().contains("Test")));
    }

    @Test
    void recentPatientsListShown() {
        Patient p = new Patient();
        p.setFirstName("Recent");
        p.setLastName("Registration");
        p.setDateOfBirth(LocalDate.of(2000, 1, 1));
        patientService.save(p);

        DashboardView view = navigate(DashboardView.class);

        var anchors = $(Anchor.class).all();
        assertTrue(anchors.stream().anyMatch(a -> a.getText().contains("Registration")));
    }

    @Test
    void clickingPatientNavigatesToDetailView() {
        Patient p = new Patient();
        p.setFirstName("Clickable");
        p.setLastName("Patient");
        p.setDateOfBirth(LocalDate.of(1988, 7, 4));
        p = patientService.save(p);

        DashboardView view = navigate(DashboardView.class);

        var anchors = $(Anchor.class).all();
        var patientLink = anchors.stream()
                .filter(a -> a.getText().contains("Patient"))
                .findFirst().orElse(null);
        assertNotNull(patientLink);
        assertTrue(patientLink.getHref().contains("patients/"));
    }
}
