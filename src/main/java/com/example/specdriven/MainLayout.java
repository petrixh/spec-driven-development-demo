package com.example.specdriven;

import com.example.specdriven.patient.DashboardView;
import com.example.specdriven.patient.PatientListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@Layout
@RolesAllowed("ADMIN")
public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);

        // Navbar
        DrawerToggle toggle = new DrawerToggle();
        H1 appName = new H1();
        Span tri = new Span("Tri");
        tri.getStyle().set("color", "#2C3E50");
        Span age = new Span("age");
        age.getStyle().set("color", "#4A90B8");
        appName.add(tri, age);
        appName.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("margin", "0")
                .set("font-family", "system-ui, -apple-system, 'Segoe UI', sans-serif")
                .set("font-weight", "600");

        Button logout = new Button("Logout", VaadinIcon.SIGN_OUT.create(), e -> {
            getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        });
        logout.addThemeVariants(ButtonVariant.TERTIARY);

        HorizontalLayout navbar = new HorizontalLayout(toggle, appName, logout);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.expand(appName);
        navbar.setWidthFull();
        navbar.getStyle().set("padding-right", "var(--vaadin-padding-m)");

        addToNavbar(navbar);

        // Drawer
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()));
        SideNavItem patientsItem = new SideNavItem("Patients", PatientListView.class, VaadinIcon.GROUP.create());
        patientsItem.setMatchNested(true);
        nav.addItem(patientsItem);
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");

        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller);
    }
}
