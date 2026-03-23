package com.example.specdriven;

import com.example.specdriven.expense.DashboardView;
import com.example.specdriven.expense.MyExpensesView;
import com.example.specdriven.expense.ReviewExpensesView;
import com.example.specdriven.expense.SubmitExpenseView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;

        DrawerToggle toggle = new DrawerToggle();

        Image logo = new Image("icons/logo.svg", "GreenLedger");
        logo.setWidth("32px");
        logo.setHeight("32px");

        H1 appTitle = new H1("GreenLedger");
        appTitle.addClassName("app-title");
        appTitle.getStyle().set("font-size", "var(--aura-font-size-l)").set("margin", "0");

        HorizontalLayout titleLayout = new HorizontalLayout(logo, appTitle);
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setSpacing(true);

        addToNavbar(toggle, titleLayout);

        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Submit Expense", SubmitExpenseView.class,
                VaadinIcon.PLUS_CIRCLE.create()));
        nav.addItem(new SideNavItem("My Expenses", MyExpensesView.class,
                VaadinIcon.LIST.create()));

        if (authContext.hasRole("MANAGER")) {
            nav.addItem(new SideNavItem("Review Expenses", ReviewExpensesView.class,
                    VaadinIcon.CHECK_CIRCLE.create()));
            nav.addItem(new SideNavItem("Dashboard", DashboardView.class,
                    VaadinIcon.DASHBOARD.create()));
        }

        Scroller scroller = new Scroller(nav);
        scroller.getStyle().set("flex", "1");

        Button logoutButton = new Button("Log out", VaadinIcon.SIGN_OUT.create(),
                e -> authContext.logout());
        logoutButton.setWidthFull();

        Span username = new Span();
        authContext.getAuthenticatedUser(org.springframework.security.core.userdetails.UserDetails.class)
                .ifPresent(user -> username.setText(user.getUsername()));
        username.addClassName("drawer-username");

        VerticalLayout drawerContent = new VerticalLayout(scroller, username, logoutButton);
        drawerContent.setSizeFull();
        drawerContent.setPadding(true);
        drawerContent.setSpacing(false);
        drawerContent.expand(scroller);

        addToDrawer(drawerContent);
        setPrimarySection(Section.DRAWER);
    }
}
