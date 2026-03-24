package com.example.specdriven.admin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        H2 title = new H2("Cinema Admin");
        title.getStyle()
                .set("margin", "var(--vaadin-padding-s)")
                .set("font-size", "var(--aura-font-size-l)");
        addToNavbar(title);

        SideNav sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("Movies", "/admin/movies"));
        sideNav.addItem(new SideNavItem("Shows", "/admin/shows"));
        sideNav.getStyle().set("padding", "var(--vaadin-padding-s)");

        addToDrawer(sideNav);
    }
}
