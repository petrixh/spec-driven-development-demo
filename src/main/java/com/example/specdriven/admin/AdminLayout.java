package com.example.specdriven.admin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class AdminLayout extends AppLayout {

    public AdminLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("re:solve");
        title.getStyle().set("font-size", "var(--aura-font-size-l)");
        title.getStyle().set("margin", "0");

        addToNavbar(toggle, title);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/admin/dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Queue", "/admin/queue", VaadinIcon.LIST.create()));
        addToDrawer(nav);
    }
}
