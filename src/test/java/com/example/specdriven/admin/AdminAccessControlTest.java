package com.example.specdriven.admin;

import com.example.specdriven.security.LoginView;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AdminAccessControlTest extends SpringBrowserlessTest {

    @Test
    @WithAnonymousUser
    void unauthenticatedUserIsRedirectedToLoginFromProducts() {
        navigate("admin/products", LoginView.class);
    }

    @Test
    @WithAnonymousUser
    void unauthenticatedUserIsRedirectedToLoginFromCustomers() {
        navigate("admin/customers", LoginView.class);
    }

    @Test
    @WithAnonymousUser
    void unauthenticatedUserIsRedirectedToLoginFromAdmin() {
        navigate("admin", LoginView.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void logoutButtonExistsInAdminLayout() {
        navigate(ProductAdminView.class);
        Button logoutButton = $(Button.class).withText("Logout").single();
        assertTrue(test(logoutButton).isUsable());
    }
}
