package com.example.specdriven.show;

import com.example.specdriven.admin.AdminLayout;
import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.example.specdriven.screeningroom.ScreeningRoom;
import com.example.specdriven.screeningroom.ScreeningRoomRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;

@Route(value = "admin/shows", layout = AdminLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Manage Shows")
public class ManageShowsView extends VerticalLayout {

    private final ShowService showService;
    private final Grid<Show> grid = new Grid<>(Show.class, false);
    private final ComboBox<Movie> movieComboBox = new ComboBox<>("Movie");
    private final ComboBox<ScreeningRoom> roomComboBox = new ComboBox<>("Screening Room");
    private final DateTimePicker dateTimePicker = new DateTimePicker("Date & Time");
    private final VerticalLayout formContainer = new VerticalLayout();
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");

    private Show currentShow;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ManageShowsView(ShowService showService,
                           MovieRepository movieRepository,
                           ScreeningRoomRepository screeningRoomRepository) {
        this.showService = showService;
        setSizeFull();

        configureGrid();
        configureForm(movieRepository, screeningRoomRepository);

        Button addButton = new Button("Add Show");
        addButton.addThemeVariants(ButtonVariant.PRIMARY);
        addButton.addClickListener(e -> addShow());

        HorizontalLayout content = new HorizontalLayout(grid, formContainer);
        content.setSizeFull();
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, formContainer);

        add(addButton, content);
        formContainer.setVisible(false);
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(show -> show.getMovie().getTitle()).setHeader("Movie").setSortable(true);
        grid.addColumn(show -> show.getDateTime().format(DT_FORMAT)).setHeader("Date/Time").setSortable(true);
        grid.addColumn(show -> show.getScreeningRoom().getName()).setHeader("Room").setSortable(true);
        grid.addColumn(show -> showService.getTicketCount(show) + " / " + showService.getTotalCapacity(show))
                .setHeader("Tickets");
        grid.asSingleSelect().addValueChangeListener(e -> editShow(e.getValue()));
        grid.setSizeFull();
    }

    private void configureForm(MovieRepository movieRepository,
                               ScreeningRoomRepository screeningRoomRepository) {
        movieComboBox.setItems(movieRepository.findAll());
        movieComboBox.setItemLabelGenerator(Movie::getTitle);
        movieComboBox.setRequired(true);

        roomComboBox.setItems(screeningRoomRepository.findAll());
        roomComboBox.setItemLabelGenerator(ScreeningRoom::getName);
        roomComboBox.setRequired(true);

        dateTimePicker.setRequiredIndicatorVisible(true);

        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        saveButton.addClickListener(e -> save());

        deleteButton.addThemeVariants(ButtonVariant.ERROR);
        deleteButton.addClickListener(e -> delete());

        cancelButton.addClickListener(e -> clearForm());

        FormLayout formLayout = new FormLayout(movieComboBox, roomComboBox, dateTimePicker);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);

        formContainer.add(new H3("Show Details"), formLayout, buttons);
        formContainer.setPadding(true);
    }

    private void addShow() {
        grid.asSingleSelect().clear();
        currentShow = new Show();
        movieComboBox.clear();
        roomComboBox.clear();
        dateTimePicker.clear();
        movieComboBox.setReadOnly(false);
        deleteButton.setVisible(false);
        formContainer.setVisible(true);
    }

    private void editShow(Show show) {
        if (show == null) {
            clearForm();
            return;
        }
        currentShow = show;
        movieComboBox.setValue(show.getMovie());
        movieComboBox.setReadOnly(true); // BR-04: movie cannot be changed
        roomComboBox.setValue(show.getScreeningRoom());
        dateTimePicker.setValue(show.getDateTime());
        deleteButton.setVisible(true);
        formContainer.setVisible(true);
    }

    private void save() {
        try {
            currentShow.setMovie(movieComboBox.getValue());
            currentShow.setScreeningRoom(roomComboBox.getValue());
            currentShow.setDateTime(dateTimePicker.getValue());
            showService.save(currentShow);
            Notification.show("Show saved", 3000, Notification.Position.TOP_CENTER);
            clearForm();
            refreshGrid();
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }

    private void delete() {
        try {
            showService.delete(currentShow);
            Notification.show("Show deleted", 3000, Notification.Position.TOP_CENTER);
            clearForm();
            refreshGrid();
        } catch (IllegalStateException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }

    private void clearForm() {
        currentShow = null;
        movieComboBox.clear();
        roomComboBox.clear();
        dateTimePicker.clear();
        formContainer.setVisible(false);
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(showService.findAll());
    }
}
