package com.example.specdriven.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void createProjectWithValidData() {
        Project project = new Project("Test Project", "Description",
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 1));
        Project saved = projectService.save(project);

        assertNotNull(saved.getId());
        assertEquals("Test Project", saved.getName());
    }

    @Test
    void createProjectRequiresName() {
        Project project = new Project(null, null,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 1));

        assertThrows(IllegalArgumentException.class, () -> projectService.save(project));
    }

    @Test
    void createProjectRequiresStartDate() {
        Project project = new Project("Test", null, null, LocalDate.of(2026, 5, 1));

        assertThrows(IllegalArgumentException.class, () -> projectService.save(project));
    }

    @Test
    void createProjectRequiresEndDate() {
        Project project = new Project("Test", null, LocalDate.of(2026, 4, 1), null);

        assertThrows(IllegalArgumentException.class, () -> projectService.save(project));
    }

    @Test
    void endDateMustBeAfterStartDate() {
        Project project = new Project("Test", null,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 4, 1));

        assertThrows(IllegalArgumentException.class, () -> projectService.save(project));
    }

    @Test
    void endDateSameAsStartDateIsInvalid() {
        LocalDate date = LocalDate.of(2026, 4, 1);
        Project project = new Project("Test", null, date, date);

        assertThrows(IllegalArgumentException.class, () -> projectService.save(project));
    }

    @Test
    void findAllReturnsProjects() {
        projectService.save(new Project("P1", null,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 1)));
        projectService.save(new Project("P2", null,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 7, 1)));

        List<Project> projects = projectService.findAll();
        // Includes demo data + our 2 new projects
        assertTrue(projects.size() >= 2);
    }

    @Test
    void deleteProjectRemovesIt() {
        Project project = projectService.save(new Project("ToDelete", null,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 1)));
        Long id = project.getId();

        projectService.delete(id);

        assertFalse(projectRepository.findById(id).isPresent());
    }

    @Test
    void projectWithNoTasksIsNotStarted() {
        Project project = new Project("Empty", null,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 1));

        assertEquals(ProjectStatus.NOT_STARTED, project.getStatus());
        assertEquals(0, project.getProgressPercent());
    }
}
