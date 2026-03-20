package com.example.specdriven.admin.ui;

import java.time.LocalDateTime;

import jakarta.annotation.security.RolesAllowed;

import com.example.specdriven.catalog.application.MovieOptionDto;
import com.example.specdriven.catalog.application.ScreeningRoomOptionDto;
import com.example.specdriven.catalog.application.ShowAdminFormData;
import com.example.specdriven.catalog.application.ShowAdminRowDto;
import com.example.specdriven.catalog.application.ShowAdminService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/shows", layout = AdminLayout.class)
@PageTitle("Manage Shows")
@RolesAllowed("ADMIN")
public class ShowAdminView extends VerticalLayout {

    private final ShowAdminService showAdminService;
    private final Grid<ShowAdminRowDto> grid = new Grid<>(ShowAdminRowDto.class, false);
    private final ComboBox<MovieOptionDto> movie = new ComboBox<>("Movie");
    private final ComboBox<ScreeningRoomOptionDto> screeningRoom = new ComboBox<>("Screening Room");
    private final DateTimePicker dateTime = new DateTimePicker("Show Date & Time");
    private final Button addShow = new Button("Add Show");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private Long editingShowId;

    public ShowAdminView(ShowAdminService showAdminService) {
        this.showAdminService = showAdminService;

        setSizeFull();

        H1 heading = new H1("Manage Shows");
        configureGrid();
        configureForm();
        VerticalLayout formPanel = createFormPanel();

        HorizontalLayout toolbar = new HorizontalLayout(addShow);
        HorizontalLayout body = new HorizontalLayout(grid, formPanel);
        body.setSizeFull();
        body.setFlexGrow(1.4, grid);
        body.setFlexGrow(1, formPanel);

        add(heading, toolbar, body);
        refreshGrid();
        editShow(null);
    }

    private void configureGrid() {
        grid.addColumn(ShowAdminRowDto::movieTitle).setHeader("Movie").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(ShowAdminRowDto::dateTime).setHeader("Date/Time").setAutoWidth(true);
        grid.addColumn(ShowAdminRowDto::screeningRoomName).setHeader("Room").setAutoWidth(true);
        grid.addColumn(show -> show.ticketsSold() + " / " + show.totalCapacity()).setHeader("Tickets Sold / Capacity")
                .setAutoWidth(true);
        grid.setHeightFull();
        grid.addItemClickListener(event -> editShow(event.getItem()));
        grid.asSingleSelect().addValueChangeListener(event -> editShow(event.getValue()));
    }

    private void configureForm() {
        movie.setItems(showAdminService.listMovieOptions());
        movie.setItemLabelGenerator(MovieOptionDto::title);
        movie.setRequired(true);

        screeningRoom.setItems(showAdminService.listScreeningRooms());
        screeningRoom.setItemLabelGenerator(ScreeningRoomOptionDto::name);
        screeningRoom.setRequired(true);

        dateTime.setRequiredIndicatorVisible(true);
        dateTime.setStep(java.time.Duration.ofMinutes(15));

        addShow.addClickListener(event -> {
            grid.deselectAll();
            editShow(null);
        });
        save.addClickListener(event -> saveShow());
        delete.addClickListener(event -> deleteShow());
        cancel.addClickListener(event -> {
            grid.deselectAll();
            editShow(null);
        });

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    private VerticalLayout createFormPanel() {
        FormLayout form = new FormLayout();
        form.add(movie, screeningRoom, dateTime);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("40rem", 2));
        form.setColspan(dateTime, 2);

        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        VerticalLayout panel = new VerticalLayout(new Paragraph("Create or update a show."), form, actions);
        panel.setWidth("460px");
        panel.setPadding(false);
        return panel;
    }

    private void saveShow() {
        try {
            ShowAdminRowDto savedShow = showAdminService.saveShow(new ShowAdminFormData(
                    editingShowId,
                    movie.getValue() == null ? null : movie.getValue().id(),
                    screeningRoom.getValue() == null ? null : screeningRoom.getValue().id(),
                    dateTime.getValue()));
            refreshGrid();
            grid.select(savedShow);
            editShow(savedShow);
            Notification.show("Show saved.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteShow() {
        if (editingShowId == null) {
            return;
        }
        try {
            showAdminService.deleteShow(editingShowId);
            Notification.show("Show deleted.");
            refreshGrid();
            grid.deselectAll();
            editShow(null);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception.getMessage());
        }
    }

    private void refreshGrid() {
        grid.setItems(showAdminService.listShows());
    }

    private void editShow(ShowAdminRowDto row) {
        if (row == null) {
            editingShowId = null;
            movie.clear();
            screeningRoom.clear();
            dateTime.clear();
            movie.setReadOnly(false);
            delete.setEnabled(false);
            return;
        }

        ShowAdminFormData show = showAdminService.getShow(row.id());
        editingShowId = show.id();
        movie.setValue(showAdminService.listMovieOptions().stream()
                .filter(option -> option.id().equals(show.movieId()))
                .findFirst()
                .orElseThrow());
        screeningRoom.setValue(showAdminService.listScreeningRooms().stream()
                .filter(option -> option.id().equals(show.screeningRoomId()))
                .findFirst()
                .orElseThrow());
        dateTime.setValue(show.dateTime());
        movie.setReadOnly(true);
        delete.setEnabled(true);
    }

    private void showError(String message) {
        Notification notification = Notification.show(message, 4000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
