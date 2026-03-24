package com.example.specdriven.task;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskManagementTest extends SpringBrowserlessTest {

    @Test
    @WithMockUser(roles = "ADMIN")
    void taskViewShowsGridForProject() {
        // Navigate to first demo project's tasks (project id 1 from DataInitializer)
        TaskManagementView view = navigate("projects/1/tasks", TaskManagementView.class);
        assertNotNull(view);

        // Should have a grid
        assertTrue($(Grid.class).exists(), "Task grid should be present");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminSeesAddTaskButton() {
        navigate("projects/1/tasks", TaskManagementView.class);

        assertTrue($(Button.class).withText("Add Task").exists(),
                "Admin should see Add Task button");
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminDoesNotSeeAddTaskButton() {
        navigate("projects/1/tasks", TaskManagementView.class);

        assertFalse($(Button.class).withText("Add Task").exists(),
                "Non-admin should not see Add Task button");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttChartButtonIsPresent() {
        navigate("projects/1/tasks", TaskManagementView.class);

        assertTrue($(Button.class).withText("Gantt Chart").exists(),
                "Gantt Chart button should be present");
    }
}
