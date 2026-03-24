package com.example.specdriven.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@Layout
@PermitAll
public class MainLayout extends AppLayout {

    private final AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        setPrimarySection(Section.DRAWER);

        addToDrawer(createDrawerHeader(), new Scroller(createSideNav()), createDrawerFooter());

        H1 viewTitle = new H1();
        viewTitle.getStyle().set("font-size", "var(--aura-font-size-l)").set("margin", "0");
        addToNavbar(new DrawerToggle(), viewTitle);
    }

    private Header createDrawerHeader() {
        Image logo = new Image("images/logo.svg", "Stash.log");
        logo.setWidth("160px");

        Header header = new Header(logo);
        header.getStyle()
                .set("padding", "var(--vaadin-padding-m)")
                .set("display", "flex")
                .set("justify-content", "center");
        return header;
    }

    private SideNav createSideNav() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Dashboard", "/", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Inventory", "/inventory", VaadinIcon.STORAGE.create()));
        nav.addItem(new SideNavItem("Receive Stock", "/receive", VaadinIcon.PACKAGE.create()));

        if (authContext.hasRole("ADMIN")) {
            nav.addItem(new SideNavItem("Products", "/products", VaadinIcon.CUBE.create()));
            nav.addItem(new SideNavItem("Adjust Stock", "/adjust", VaadinIcon.WRENCH.create()));
        }

        return nav;
    }

    private Footer createDrawerFooter() {
        Button logout = new Button("Log out", VaadinIcon.SIGN_OUT.create(), e -> authContext.logout());
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logout.setWidthFull();

        Footer footer = new Footer(logout);
        footer.getStyle().set("padding", "var(--vaadin-padding-s)");
        return footer;
    }
}
