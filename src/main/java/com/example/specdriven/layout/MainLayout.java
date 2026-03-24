package com.example.specdriven.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 appName = new H1("Forge");
        appName.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("margin", "0")
                .set("color", "var(--vaadin-primary-color)");

        Span tagline = new Span("Project Management");
        tagline.getStyle()
                .set("font-size", "var(--aura-font-size-xs)")
                .set("color", "var(--vaadin-text-color-secondary)");

        HorizontalLayout titleLayout = new HorizontalLayout(appName, tagline);
        titleLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        titleLayout.setSpacing(true);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/projects",
                VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Team Workload", "/team",
                VaadinIcon.GROUP.create()));
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, titleLayout);
        setPrimarySection(Section.DRAWER);
    }
}
