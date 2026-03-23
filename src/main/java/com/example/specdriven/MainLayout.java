package com.example.specdriven;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Triage");
        logo.addClassName("app-logo");
        logo.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("margin", "0");

        var header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setPadding(true);
        header.setSpacing(true);
        header.setWidthFull();
        header.addClassName("app-header");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "dashboard"));
        nav.addItem(new SideNavItem("Patients", "patients"));
        addToDrawer(nav);
    }
}
