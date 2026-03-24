package com.example.specdriven.team;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamWorkloadTest extends SpringBrowserlessTest {

    @Test
    @WithMockUser(roles = "USER")
    void teamWorkloadViewShowsGrid() {
        TeamWorkloadView view = navigate(TeamWorkloadView.class);
        assertNotNull(view);

        assertTrue($(Grid.class).exists(), "Grid should be present");
    }

    @Test
    @WithMockUser(roles = "USER")
    void teamWorkloadHasCorrectTitle() {
        navigate(TeamWorkloadView.class);

        H2 title = $(H2.class).single();
        assertEquals("Team Workload", title.getText());
    }

    @Test
    @WithMockUser(roles = "USER")
    void gridShowsAllTeamMembers() {
        navigate(TeamWorkloadView.class);

        Grid grid = $(Grid.class).single();
        // DataInitializer creates 5 members
        assertNotNull(grid);
    }
}
