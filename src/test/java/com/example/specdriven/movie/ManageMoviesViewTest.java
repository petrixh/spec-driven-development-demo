package com.example.specdriven.movie;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManageMoviesViewTest extends SpringBrowserlessTest {

    @Test
    @WithUserDetails("admin")
    void viewShowsGrid() {
        navigate(ManageMoviesView.class);
        Grid<?> grid = $(Grid.class).first();
        assertNotNull(grid);
    }

    @Test
    @WithUserDetails("admin")
    void addButtonIsPresent() {
        navigate(ManageMoviesView.class);
        Button addButton = $(Button.class).withText("Add Movie").first();
        assertNotNull(addButton);
    }

    @Test
    @WithUserDetails("admin")
    void gridHasExpectedColumns() {
        navigate(ManageMoviesView.class);
        Grid<?> grid = $(Grid.class).first();
        assertEquals(3, grid.getColumns().size());
    }
}
