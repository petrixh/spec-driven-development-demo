package com.example.specdriven.project;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectDashboardTest extends SpringBrowserlessTest {

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardShowsPageTitle() {
        navigate(ProjectDashboardView.class);

        H2 title = $(H2.class).single();
        assertEquals("Projects", title.getText());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminSeesNewProjectButton() {
        navigate(ProjectDashboardView.class);

        assertTrue($(Button.class).withText("New Project").exists(),
                "Admin should see New Project button");
    }

    @Test
    @WithMockUser(roles = "USER")
    void nonAdminDoesNotSeeNewProjectButton() {
        navigate(ProjectDashboardView.class);

        assertFalse($(Button.class).withText("New Project").exists(),
                "Non-admin should not see New Project button");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardShowsProjectCardsFromDemoData() {
        navigate(ProjectDashboardView.class);

        var cards = $(Div.class).withClassName("project-card").all();
        assertTrue(cards.size() >= 3, "Should have at least 3 demo projects");
    }
}
