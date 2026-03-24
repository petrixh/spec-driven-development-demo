package com.example.specdriven;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("GreenLedger");
        title.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("margin", "0")
                .set("color", "#2E7D32");

        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Submit Expense",
                "submit-expense", VaadinIcon.PLUS_CIRCLE.create()));
        nav.addItem(new SideNavItem("My Expenses",
                "my-expenses", VaadinIcon.LIST.create()));

        if (hasRole("MANAGER")) {
            nav.addItem(new SideNavItem("Review Expenses",
                    "review-expenses", VaadinIcon.CHECK_CIRCLE.create()));
            nav.addItem(new SideNavItem("Dashboard",
                    "dashboard", VaadinIcon.DASHBOARD.create()));
        }

        Span logoutLink = new Span("Logout");
        SideNavItem logoutItem = new SideNavItem("Logout", "logout",
                VaadinIcon.SIGN_OUT.create());
        nav.addItem(logoutItem);

        nav.getStyle().set("margin", "var(--vaadin-gap-s)");
        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, title);

        setPrimarySection(Section.DRAWER);
    }

    private boolean hasRole(String role) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
