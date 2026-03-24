package com.example.specdriven.task;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GanttChartTest extends SpringBrowserlessTest {

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttViewRendersForProject() {
        GanttChartView view = navigate("projects/1/gantt", GanttChartView.class);
        assertNotNull(view);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttHasZoomControls() {
        navigate("projects/1/gantt", GanttChartView.class);

        assertTrue($(Button.class).withText("Day").exists(), "Day zoom button should exist");
        assertTrue($(Button.class).withText("Week").exists(), "Week zoom button should exist");
        assertTrue($(Button.class).withText("Month").exists(), "Month zoom button should exist");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttHasBackToTasksButton() {
        navigate("projects/1/gantt", GanttChartView.class);

        assertTrue($(Button.class).withText("Back to Tasks").exists(),
                "Back to Tasks button should be present");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttShowsTaskBars() {
        navigate("projects/1/gantt", GanttChartView.class);

        // Should have gantt bars for the demo tasks
        var bars = $(Div.class).withClassName("gantt-bar").all();
        assertTrue(bars.size() >= 3, "Should have task bars for demo project");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttShowsTodayMarker() {
        navigate("projects/1/gantt", GanttChartView.class);

        assertTrue($(Div.class).withClassName("gantt-today-marker").exists(),
                "Today marker should be visible");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ganttBarsAreColorCodedByStatus() {
        navigate("projects/1/gantt", GanttChartView.class);

        // Project 1 has DONE, IN_PROGRESS, and TODO tasks
        assertTrue($(Div.class).withClassName("gantt-bar").withClassName("done").exists(),
                "Should have done bars");
        assertTrue($(Div.class).withClassName("gantt-bar").withClassName("in-progress").exists(),
                "Should have in-progress bars");
        assertTrue($(Div.class).withClassName("gantt-bar").withClassName("todo").exists(),
                "Should have todo bars");
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminBarsAreNotDraggable() {
        navigate("projects/1/gantt", GanttChartView.class);

        var draggableBars = $(Div.class).withClassName("gantt-bar").withClassName("draggable").all();
        assertTrue(draggableBars.isEmpty(), "Non-admin bars should not be draggable");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminBarsAreDraggable() {
        navigate("projects/1/gantt", GanttChartView.class);

        var draggableBars = $(Div.class).withClassName("gantt-bar").withClassName("draggable").all();
        assertFalse(draggableBars.isEmpty(), "Admin bars should be draggable");
    }
}
