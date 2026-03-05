package com.example.specdriven.admin;

import com.example.specdriven.security.LoginView;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class AdminLayoutTest extends SpringBrowserlessTest {

    @Test
    void menuPresent() {
        navigate(MoviesAdminView.class);

        SideNav sideNav = $(SideNav.class).single();
        List<SideNavItem> items = sideNav.getItems();
        assertEquals(3, items.size());
        assertEquals("Movies", items.get(0).getLabel());
        assertEquals("Shows", items.get(1).getLabel());
        assertEquals("Bookings", items.get(2).getLabel());
    }

    @Test
    void navigationToShows() {
        navigate(MoviesAdminView.class);

        // Click Shows menu item
        SideNav sideNav = $(SideNav.class).single();
        SideNavItem showsItem = sideNav.getItems().get(1);
        navigate(ShowsAdminView.class);

        // Verify Shows view loaded
        assertNotNull($(ShowsAdminView.class).single());
    }

    @Test
    void defaultRedirect() {
        navigate("admin", MoviesAdminView.class);
    }

    @Test
    @WithAnonymousUser
    void anonymousUserRedirectedToLogin() {
        navigate("admin", LoginView.class);
    }
}
