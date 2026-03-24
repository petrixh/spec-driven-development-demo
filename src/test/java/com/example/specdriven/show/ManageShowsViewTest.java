package com.example.specdriven.show;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManageShowsViewTest extends SpringBrowserlessTest {

    @Test
    @WithUserDetails("admin")
    void viewShowsGrid() {
        navigate(ManageShowsView.class);
        Grid<?> grid = $(Grid.class).first();
        assertNotNull(grid);
    }

    @Test
    @WithUserDetails("admin")
    void gridHasFourColumns() {
        navigate(ManageShowsView.class);
        Grid<?> grid = $(Grid.class).first();
        assertEquals(4, grid.getColumns().size());
    }
}
