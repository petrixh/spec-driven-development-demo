package com.example.specdriven.show;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;

@Route("admin/shows")
@RolesAllowed("ADMIN")
public class ManageShowsView extends VerticalLayout {

    private static final int CLEANUP_BUFFER_MINUTES = 30;

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRoomRepository screeningRoomRepository;
    private final TicketRepository ticketRepository;

    private final Grid<Show> grid = new Grid<>(Show.class, false);
    private final ComboBox<Movie> movieCombo = new ComboBox<>("Movie");
    private final ComboBox<ScreeningRoom> roomCombo = new ComboBox<>("Screening Room");
    private final DateTimePicker dateTimePicker = new DateTimePicker("Date & Time");
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");

    private Show currentShow;

    public ManageShowsView(ShowRepository showRepository,
                           MovieRepository movieRepository,
                           ScreeningRoomRepository screeningRoomRepository,
                           TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.screeningRoomRepository = screeningRoomRepository;
        this.ticketRepository = ticketRepository;

        addClassName("admin-view");
        setSizeFull();
        add(new H2("Manage Shows"));

        // Grid
        grid.addColumn(show -> show.getMovie().getTitle()).setHeader("Movie").setSortable(true);
        grid.addColumn(show -> show.getDateTime().toString()).setHeader("Date/Time").setSortable(true);
        grid.addColumn(show -> show.getScreeningRoom().getName()).setHeader("Room").setSortable(true);
        grid.addColumn(show -> {
            long sold = ticketRepository.countByShowId(show.getId());
            int total = show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow();
            return sold + " / " + total;
        }).setHeader("Tickets");
        grid.asSingleSelect().addValueChangeListener(e -> editShow(e.getValue()));

        // Form
        movieCombo.setItems(movieRepository.findAll());
        movieCombo.setItemLabelGenerator(Movie::getTitle);
        movieCombo.setRequired(true);
        movieCombo.setWidthFull();

        roomCombo.setItems(screeningRoomRepository.findAll());
        roomCombo.setItemLabelGenerator(ScreeningRoom::getName);
        roomCombo.setRequired(true);
        roomCombo.setWidthFull();

        dateTimePicker.setWidthFull();

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveShow());

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> deleteShow());

        cancelButton.addClickListener(e -> clearForm());

        FormLayout formLayout = new FormLayout();
        formLayout.add(movieCombo, roomCombo, dateTimePicker);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);

        VerticalLayout formContainer = new VerticalLayout(formLayout, buttonLayout);
        formContainer.setWidth("400px");
        formContainer.setPadding(false);

        Button addButton = new Button("Add Show");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            grid.asSingleSelect().clear();
            clearForm();
            currentShow = null; // will be created on save
            movieCombo.setEnabled(true);
            saveButton.setEnabled(true);
            cancelButton.setEnabled(true);
            deleteButton.setVisible(false);
        });

        HorizontalLayout content = new HorizontalLayout();
        content.addClassName("admin-content");
        content.setSizeFull();

        VerticalLayout gridContainer = new VerticalLayout(addButton, grid);
        gridContainer.setSizeFull();
        gridContainer.setPadding(false);

        content.add(gridContainer, formContainer);
        content.setFlexGrow(1, gridContainer);
        add(content);

        clearForm();
        refreshGrid();
    }

    private void editShow(Show show) {
        if (show == null) {
            clearForm();
            return;
        }
        currentShow = show;
        movieCombo.setValue(show.getMovie());
        movieCombo.setEnabled(false); // BR-04: movie cannot be changed
        roomCombo.setValue(show.getScreeningRoom());
        dateTimePicker.setValue(show.getDateTime());
        deleteButton.setVisible(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

    private void saveShow() {
        Movie movie = movieCombo.getValue();
        ScreeningRoom room = roomCombo.getValue();
        LocalDateTime dateTime = dateTimePicker.getValue();

        if (movie == null) {
            showError("Movie is required");
            return;
        }
        if (room == null) {
            showError("Screening room is required");
            return;
        }
        if (dateTime == null) {
            showError("Date and time are required");
            return;
        }
        if (!dateTime.isAfter(LocalDateTime.now())) {
            showError("Show must be scheduled in the future");
            return;
        }

        // Check overlap: new show's time slot vs existing shows in same room
        int durationWithBuffer = movie.getDurationMinutes() + CLEANUP_BUFFER_MINUTES;
        LocalDateTime newEnd = dateTime.plusMinutes(durationWithBuffer);

        List<Show> roomShows = showRepository.findByScreeningRoomId(room.getId());
        for (Show existing : roomShows) {
            if (currentShow != null && existing.getId().equals(currentShow.getId())) {
                continue; // skip self when editing
            }
            int existingDuration = existing.getMovie().getDurationMinutes() + CLEANUP_BUFFER_MINUTES;
            LocalDateTime existingStart = existing.getDateTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existingDuration);

            // Overlap: newStart < existingEnd AND newEnd > existingStart
            if (dateTime.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                showError("This room has an overlapping show at " + existingStart);
                return;
            }
        }

        if (currentShow == null) {
            currentShow = new Show(dateTime, movie, room);
        } else {
            currentShow.setDateTime(dateTime);
            currentShow.setScreeningRoom(room);
        }
        showRepository.save(currentShow);

        Notification.show("Show saved", 3000, Notification.Position.TOP_CENTER);
        clearForm();
        refreshGrid();
    }

    private void deleteShow() {
        if (currentShow == null || currentShow.getId() == null) {
            return;
        }

        long soldTickets = ticketRepository.countByShowId(currentShow.getId());
        if (soldTickets > 0) {
            showError("Cannot delete: this show has " + soldTickets + " sold ticket(s)");
            return;
        }

        showRepository.delete(currentShow);
        Notification.show("Show deleted", 3000, Notification.Position.TOP_CENTER);
        clearForm();
        refreshGrid();
    }

    private void clearForm() {
        currentShow = null;
        movieCombo.clear();
        movieCombo.setEnabled(true);
        roomCombo.clear();
        dateTimePicker.clear();
        deleteButton.setVisible(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(showRepository.findAll());
    }

    private void showError(String message) {
        Notification.show(message, 5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.ERROR);
    }
}
