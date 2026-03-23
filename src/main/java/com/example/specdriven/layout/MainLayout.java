package com.example.specdriven.layout;

import com.example.specdriven.dashboard.DashboardView;
import com.example.specdriven.inventory.BrowseInventoryView;
import com.example.specdriven.product.ManageProductsView;
import com.example.specdriven.stock.AdjustStockView;
import com.example.specdriven.stock.ReceiveStockView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.PermitAll;

@PermitAll
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Stash.log");
        title.getStyle()
                .set("font-size", "var(--aura-font-size-l)")
                .set("font-weight", "var(--aura-font-weight-semibold)")
                .set("margin", "0");

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Inventory", BrowseInventoryView.class, VaadinIcon.STOCK.create()));
        nav.addItem(new SideNavItem("Products", ManageProductsView.class, VaadinIcon.PACKAGE.create()));
        nav.addItem(new SideNavItem("Receive Stock", ReceiveStockView.class, VaadinIcon.DOWNLOAD.create()));
        nav.addItem(new SideNavItem("Adjust Stock", AdjustStockView.class, VaadinIcon.EDIT.create()));

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
        setPrimarySection(Section.DRAWER);
    }
}
