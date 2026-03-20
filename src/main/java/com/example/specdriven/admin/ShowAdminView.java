package com.example.specdriven.admin;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ScreeningRoom;
import com.example.specdriven.catalog.domain.ScreeningRoomRepository;
import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.TicketRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import jakarta.annotation.security.RolesAllowed;

@Route("admin/shows")
@RolesAllowed("ADMIN")
public class ShowAdminView extends VerticalLayout {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int CLEANUP_BUFFER_MINUTES = 30;

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRoomRepository roomRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    private final Grid<Show> grid = new Grid<>(Show.class, false);
    private final ComboBox<Movie> movieCombo = new ComboBox<>("Movie");
    private final ComboBox<ScreeningRoom> roomCombo = new ComboBox<>("Screening Room");
    private final DateTimePicker dateTimePicker = new DateTimePicker("Date & Time");

    private Show editingShow = null;

    public ShowAdminView(ShowRepository showRepository, MovieRepository movieRepository,
            ScreeningRoomRepository roomRepository, TicketRepository ticketRepository, Clock clock) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        // Reset text color — inherited white from .app-shell dark cinema theme
        getStyle().set("color", "#1a1a1a").set("background", "#f5f5f5");

        add(new RouterLink("← Admin Index", AdminIndexView.class));
        add(new H2("Manage Shows"));

        configureGrid();

        HorizontalLayout mainLayout = new HorizontalLayout(buildGridSection(), buildFormSection());
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(2, mainLayout.getComponentAt(0));
        mainLayout.setFlexGrow(1, mainLayout.getComponentAt(1));

        add(mainLayout);
        refreshGrid();
    }

    private VerticalLayout buildGridSection() {
        Button addBtn = new Button("Add Show", e -> openNew());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VerticalLayout section = new VerticalLayout(addBtn, grid);
        section.setPadding(false);
        section.setSizeFull();
        return section;
    }

    private VerticalLayout buildFormSection() {
        List<Movie> movies = movieRepository.findAll();
        movieCombo.setItems(movies);
        movieCombo.setItemLabelGenerator(Movie::getTitle);
        movieCombo.setWidth("100%");

        List<ScreeningRoom> rooms = roomRepository.findAll();
        roomCombo.setItems(rooms);
        roomCombo.setItemLabelGenerator(ScreeningRoom::getName);
        roomCombo.setWidth("100%");

        dateTimePicker.setWidth("100%");

        FormLayout form = new FormLayout(movieCombo, roomCombo, dateTimePicker);
        form.setWidth("100%");

        Button saveBtn = new Button("Save", e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelBtn = new Button("Cancel", e -> clearForm());

        HorizontalLayout actions = new HorizontalLayout(saveBtn, cancelBtn);

        VerticalLayout formSection = new VerticalLayout(new H2("Show Details"), form, actions);
        formSection.setWidth("380px");
        formSection.setPadding(true);
        formSection.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        return formSection;
    }

    private void configureGrid() {
        grid.addColumn(s -> s.getMovie().getTitle()).setHeader("Movie").setSortable(true).setFlexGrow(1);
        grid.addColumn(s -> s.getDateTime().format(DISPLAY_FORMAT)).setHeader("Date & Time").setWidth("150px");
        grid.addColumn(s -> s.getScreeningRoom().getName()).setHeader("Room").setWidth("100px");
        grid.addColumn(s -> {
            int total = s.getScreeningRoom().getRows() * s.getScreeningRoom().getSeatsPerRow();
            long sold = ticketRepository.countByShowId(s.getId());
            return sold + " / " + total;
        }).setHeader("Tickets Sold").setWidth("120px");
        grid.addComponentColumn(show -> {
            Button editBtn = new Button("Edit", e -> openEdit(show));
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button deleteBtn = new Button("Delete", e -> confirmDelete(show));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setWidth("160px");
        grid.setHeight("400px");
    }

    private void openNew() {
        editingShow = null;
        movieCombo.setReadOnly(false);
        movieCombo.clear();
        roomCombo.clear();
        dateTimePicker.clear();
    }

    private void openEdit(Show show) {
        editingShow = show;
        movieCombo.setValue(show.getMovie());
        movieCombo.setReadOnly(true);
        roomCombo.setValue(show.getScreeningRoom());
        dateTimePicker.setValue(show.getDateTime());
    }

    private void save() {
        Movie movie = movieCombo.getValue();
        ScreeningRoom room = roomCombo.getValue();
        LocalDateTime dateTime = dateTimePicker.getValue();

        if (movie == null) { showError("Please select a movie."); return; }
        if (room == null) { showError("Please select a screening room."); return; }
        if (dateTime == null) { showError("Please set a date and time."); return; }
        if (!dateTime.isAfter(LocalDateTime.now(clock))) {
            showError("Show must be scheduled in the future.");
            return;
        }

        long excludeId = editingShow != null ? editingShow.getId() : -1L;
        LocalDateTime showEnd = dateTime.plusMinutes(movie.getDurationMinutes() + CLEANUP_BUFFER_MINUTES);
        LocalDateTime windowStart = dateTime.minusMinutes(movie.getDurationMinutes() + CLEANUP_BUFFER_MINUTES);

        if (showRepository.existsOverlappingShow(room.getId(), excludeId, windowStart, showEnd)) {
            showError("This time slot overlaps with another show in " + room.getName() + ".");
            return;
        }

        try {
            if (editingShow == null) {
                showRepository.save(new Show(dateTime, movie, room));
            } else {
                editingShow.setDateTime(dateTime);
                editingShow.setScreeningRoom(room);
                showRepository.save(editingShow);
            }
            Notification.show("Show saved.", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            clearForm();
            refreshGrid();
        } catch (Exception ex) {
            showError("Save failed: " + ex.getMessage());
        }
    }

    private void confirmDelete(Show show) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete show?");
        dialog.setText(show.getMovie().getTitle() + " — " + show.getDateTime().format(DISPLAY_FORMAT));
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> deleteShow(show));
        dialog.open();
    }

    private void deleteShow(Show show) {
        if (ticketRepository.existsByShowId(show.getId())) {
            showError("Cannot delete a show that has sold tickets.");
            return;
        }
        showRepository.delete(show);
        Notification.show("Show deleted.", 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        clearForm();
        refreshGrid();
    }

    private void clearForm() {
        editingShow = null;
        movieCombo.setReadOnly(false);
        movieCombo.clear();
        roomCombo.clear();
        dateTimePicker.clear();
    }

    private void refreshGrid() {
        grid.setItems(showRepository.findAllByOrderByDateTimeAsc());
    }

    private void showError(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
