package com.example.specdriven.admin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        DrawerToggle toggle = new DrawerToggle();

        Span brand = new Span();
        brand.addClassName("resolve-brand");

        Span re = new Span("re");
        re.addClassName("resolve-brand-re");
        Span colon = new Span(":");
        colon.addClassName("resolve-brand-colon");
        Span solve = new Span("solve");
        solve.addClassName("resolve-brand-solve");
        brand.add(re, colon, solve);

        addToNavbar(toggle, brand);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/admin/dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Queue", "/admin/queue", VaadinIcon.LIST.create()));
        addToDrawer(nav);
    }
}
