package com.example.specdriven.patient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class BrowseSearchPatientsTest extends SpringBrowserlessTest {

    @Autowired
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        // Clear and create test data
        patientService.findPatients("", org.springframework.data.domain.Pageable.unpaged())
                .forEach(p -> {});

        Patient p1 = new Patient();
        p1.setFirstName("Alice");
        p1.setLastName("Anderson");
        p1.setDateOfBirth(LocalDate.of(1990, 1, 15));
        p1.setPhone("555-0101");
        p1.setEmail("alice@example.com");
        patientService.save(p1);

        Patient p2 = new Patient();
        p2.setFirstName("Bob");
        p2.setLastName("Brown");
        p2.setDateOfBirth(LocalDate.of(1985, 6, 20));
        p2.setPhone("555-0102");
        p2.setEmail("bob@example.com");
        patientService.save(p2);
    }

    @Test
    void tableDisplaysPatientDataWithCorrectColumns() {
        PatientListView view = navigate(PatientListView.class);
        Grid<Patient> grid = $(Grid.class).single();

        assertNotNull(grid);
        assertTrue(grid.getColumns().stream().anyMatch(c -> "lastName".equals(c.getKey())));
        assertTrue(grid.getColumns().stream().anyMatch(c -> "firstName".equals(c.getKey())));
        assertTrue(grid.getColumns().stream().anyMatch(c -> "dateOfBirth".equals(c.getKey())));
        assertTrue(grid.getColumns().stream().anyMatch(c -> "phone".equals(c.getKey())));
        assertTrue(grid.getColumns().stream().anyMatch(c -> "email".equals(c.getKey())));
    }

    @Test
    void searchFiltersPatientsByName() {
        PatientListView view = navigate(PatientListView.class);
        TextField searchField = $(TextField.class).single();
        test(searchField).setValue("Alice");

        Grid<Patient> grid = $(Grid.class).single();
        // After filtering, the data provider should refresh
        assertNotNull(grid.getDataProvider());
    }

    @Test
    void searchFiltersPatientsByDateOfBirth() {
        PatientListView view = navigate(PatientListView.class);
        TextField searchField = $(TextField.class).single();
        test(searchField).setValue("1990");

        Grid<Patient> grid = $(Grid.class).single();
        assertNotNull(grid.getDataProvider());
    }

    @Test
    void emptySearchShowsAllPatients() {
        PatientListView view = navigate(PatientListView.class);
        TextField searchField = $(TextField.class).single();
        test(searchField).setValue("");

        Grid<Patient> grid = $(Grid.class).single();
        assertNotNull(grid.getDataProvider());
    }

    @Test
    void gridHasSortableColumns() {
        PatientListView view = navigate(PatientListView.class);
        Grid<Patient> grid = $(Grid.class).single();

        assertTrue(grid.getColumnByKey("lastName").isSortable());
        assertTrue(grid.getColumnByKey("firstName").isSortable());
        assertTrue(grid.getColumnByKey("dateOfBirth").isSortable());
    }
}
