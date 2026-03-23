package com.example.specdriven;

import com.example.specdriven.project.ProjectDashboardView;
import com.example.specdriven.team.TeamWorkloadView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import jakarta.annotation.security.PermitAll;

@Layout
@PermitAll
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        Image logo = new Image("icons/logo-horizontal.svg", "TickTask");
        logo.setHeight("36px");

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Projects", ProjectDashboardView.class,
                VaadinIcon.FOLDER_OPEN.create()));
        nav.addItem(new SideNavItem("Team Workload", TeamWorkloadView.class,
                VaadinIcon.USERS.create()));
        nav.getStyle().set("margin", "var(--vaadin-padding-s)");

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, logo);

        setPrimarySection(Section.DRAWER);
    }
}
