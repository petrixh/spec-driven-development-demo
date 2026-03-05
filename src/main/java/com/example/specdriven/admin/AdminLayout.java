package com.example.specdriven.admin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        var toggle = new DrawerToggle();

        var title = new H1("Cinema Admin");
        title.getStyle().set("font-size", "1.125rem").set("margin", "0");

        var nav = new SideNav();
        nav.addItem(new SideNavItem("Movies", MoviesAdminView.class));
        nav.addItem(new SideNavItem("Shows", ShowsAdminView.class));
        nav.addItem(new SideNavItem("Bookings", BookingsAdminView.class));

        addToDrawer(nav);
        addToNavbar(toggle, title);
    }
}
