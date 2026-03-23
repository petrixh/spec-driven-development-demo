package com.example.specdriven;

import com.example.specdriven.project.ProjectDashboardView;
import com.example.specdriven.team.TeamWorkloadView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

        H1 title = new H1("TickTask");
        title.getStyle()
                .set("font-size", "1.125rem")
                .set("margin", "0")
                .set("color", "#F57C00");

        Span tagline = new Span("Project Management");
        tagline.getStyle()
                .set("font-size", "var(--aura-font-size-s)")
                .set("color", "#757575")
                .set("margin-left", "var(--vaadin-space-s)");

        HorizontalLayout header = new HorizontalLayout(title, tagline);
        header.setAlignItems(HorizontalLayout.Alignment.BASELINE);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Projects", ProjectDashboardView.class,
                VaadinIcon.FOLDER_OPEN.create()));
        nav.addItem(new SideNavItem("Team Workload", TeamWorkloadView.class,
                VaadinIcon.USERS.create()));
        nav.getStyle().set("margin", "var(--vaadin-space-s)");

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, header);

        setPrimarySection(Section.DRAWER);
    }
}
