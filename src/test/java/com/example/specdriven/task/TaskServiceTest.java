package com.example.specdriven.task;

import com.example.specdriven.project.Project;
import com.example.specdriven.project.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project("Test Project", null,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 6, 30));
        project = projectRepository.save(project);
    }

    @Test
    void createTaskWithValidData() {
        Task task = new Task("Test Task", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 15), project);
        Task saved = taskService.save(task);

        assertNotNull(saved.getId());
        assertEquals("Test Task", saved.getName());
        assertEquals(TaskStatus.TODO, saved.getStatus());
        assertEquals(TaskPriority.MEDIUM, saved.getPriority());
    }

    @Test
    void createTaskRequiresName() {
        Task task = new Task(null, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 15), project);

        assertThrows(IllegalArgumentException.class, () -> taskService.save(task));
    }

    @Test
    void taskEndDateMustBeOnOrAfterStartDate() {
        Task task = new Task("Bad dates", LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 5), project);

        assertThrows(IllegalArgumentException.class, () -> taskService.save(task));
    }

    @Test
    void taskSameDayIsValid() {
        LocalDate date = LocalDate.of(2026, 4, 10);
        Task task = new Task("Same day", date, date, project);
        Task saved = taskService.save(task);
        assertNotNull(saved.getId());
    }

    @Test
    void taskStartDateMustNotBeBeforeProjectStart() {
        Task task = new Task("Too early", LocalDate.of(2026, 3, 15), LocalDate.of(2026, 4, 15), project);

        assertThrows(IllegalArgumentException.class, () -> taskService.save(task));
    }

    @Test
    void taskEndDateMustNotBeAfterProjectEnd() {
        Task task = new Task("Too late", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 7, 15), project);

        assertThrows(IllegalArgumentException.class, () -> taskService.save(task));
    }

    @Test
    void findByProjectIdReturnsTasks() {
        taskService.save(new Task("T1", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 15), project));
        taskService.save(new Task("T2", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), project));

        List<Task> tasks = taskService.findByProjectId(project.getId());
        assertTrue(tasks.size() >= 2);
    }

    @Test
    void deleteTaskRemovesIt() {
        Task task = taskService.save(
                new Task("ToDelete", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 15), project));
        Long id = task.getId();

        taskService.delete(id);

        assertThrows(IllegalArgumentException.class, () -> taskService.findById(id));
    }

    @Test
    void updateTaskDates() {
        Task task = taskService.save(
                new Task("Movable", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 15), project));

        Task updated = taskService.updateDates(task.getId(),
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10));

        assertEquals(LocalDate.of(2026, 5, 1), updated.getStartDate());
        assertEquals(LocalDate.of(2026, 5, 10), updated.getEndDate());
    }
}
