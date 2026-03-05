package com.example.specdriven.admin;

import com.example.specdriven.data.Booking;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;

@Route(value = "admin/bookings", layout = AdminLayout.class)
@PageTitle("View Bookings")
@RolesAllowed("ADMIN")
public class BookingsAdminView extends VerticalLayout {

    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final BookingAdminService service;
    private final Grid<Booking> grid = new Grid<>(Booking.class, false);
    private final TextField search = new TextField();

    public BookingsAdminView(BookingAdminService service) {
        this.service = service;
        setSizeFull();

        configureSearch();
        configureGrid();

        add(new H2("Bookings"), search, grid);
        refreshGrid();
    }

    private void configureSearch() {
        search.setPlaceholder("Search by confirmation code, name, or email...");
        search.setClearButtonVisible(true);
        search.setWidthFull();
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(event -> refreshGrid());
    }

    private void configureGrid() {
        grid.addColumn(Booking::getConfirmationCode).setHeader("Confirmation Code").setAutoWidth(true);
        grid.addColumn(Booking::getCustomerName).setHeader("Customer Name").setAutoWidth(true);
        grid.addColumn(Booking::getCustomerEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(b -> b.getScreening().getMovie().getTitle()).setHeader("Movie").setAutoWidth(true);
        grid.addColumn(b -> b.getScreening().getStartTime().format(DATETIME_FMT)).setHeader("Show Time").setAutoWidth(true);
        grid.addColumn(Booking::getTicketCount).setHeader("Tickets").setAutoWidth(true);
    }

    private void refreshGrid() {
        grid.setItems(service.search(search.getValue()));
    }
}
