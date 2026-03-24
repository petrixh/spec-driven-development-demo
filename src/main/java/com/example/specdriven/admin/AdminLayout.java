package com.example.specdriven.admin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import jakarta.annotation.security.RolesAllowed;

@Layout
@RolesAllowed("ADMIN")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        Image logo = new Image("icons/icon.svg", "re:solve");
        logo.setHeight("32px");

        H1 title = new H1("re:solve");
        title.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("margin", "0");

        Anchor logout = new Anchor("/logout", "Log out");
        logout.getStyle()
                .set("margin-left", "auto")
                .set("color", "inherit");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, title, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setWidthFull();
        header.getStyle()
                .set("padding", "0 var(--vaadin-space-m)");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/admin/dashboard"));
        nav.addItem(new SideNavItem("Ticket Queue", "/admin/queue"));
        addToDrawer(nav);
    }
}
