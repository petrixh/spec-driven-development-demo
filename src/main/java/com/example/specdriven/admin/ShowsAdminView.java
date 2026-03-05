package com.example.specdriven.admin;

import com.example.specdriven.data.Movie;
import com.example.specdriven.data.Screening;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "admin/shows", layout = AdminLayout.class)
@PageTitle("Manage Shows")
@RolesAllowed("ADMIN")
public class ShowsAdminView extends VerticalLayout {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final ShowAdminService service;

    private final Grid<Screening> grid = new Grid<>(Screening.class, false);
    private final ComboBox<Movie> movie = new ComboBox<>("Movie");
    private final DatePicker date = new DatePicker("Date");
    private final TimePicker time = new TimePicker("Time");
    private final IntegerField availableSeats = new IntegerField("Available Seats");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private Screening currentScreening;

    public ShowsAdminView(ShowAdminService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();
        configureButtons();

        var formLayout = createFormLayout();
        var editorLayout = createEditorLayout(formLayout);

        var splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(editorLayout);
        splitLayout.setSplitterPosition(65);

        add(splitLayout);
        refreshGrid();
        clearForm();
    }

    private void configureGrid() {
        grid.addColumn(s -> s.getMovie().getTitle()).setHeader("Movie").setAutoWidth(true);
        grid.addColumn(s -> s.getStartTime().format(DATE_FMT)).setHeader("Date").setAutoWidth(true);
        grid.addColumn(s -> s.getStartTime().format(TIME_FMT)).setHeader("Time").setAutoWidth(true);
        grid.addColumn(Screening::getAvailableSeats).setHeader("Available Seats").setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue());
            } else {
                clearForm();
            }
        });
    }

    private void configureForm() {
        movie.setItemLabelGenerator(Movie::getTitle);
        movie.setItems(service.findAllMovies());
        movie.setRequired(true);
        movie.setRequiredIndicatorVisible(true);

        date.setRequired(true);
        date.setRequiredIndicatorVisible(true);

        time.setRequired(true);
        time.setRequiredIndicatorVisible(true);

        availableSeats.setRequired(true);
        availableSeats.setRequiredIndicatorVisible(true);
        availableSeats.setMin(1);
    }

    private void configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (!validateForm()) {
                Notification.show("Please fill in all fields correctly.", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (currentScreening == null) {
                currentScreening = new Screening(movie.getValue(),
                        LocalDateTime.of(date.getValue(), time.getValue()),
                        availableSeats.getValue());
            } else {
                currentScreening.setMovie(movie.getValue());
                currentScreening.setStartTime(LocalDateTime.of(date.getValue(), time.getValue()));
                currentScreening.setAvailableSeats(availableSeats.getValue());
            }
            service.save(currentScreening);
            Notification.show("Show saved.", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            clearForm();
        });

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> {
            if (currentScreening == null || currentScreening.getId() == null) {
                return;
            }
            var dialog = new ConfirmDialog();
            dialog.setHeader("Delete show?");
            dialog.setText("Are you sure you want to permanently delete this show for \""
                    + currentScreening.getMovie().getTitle() + "\"?");
            dialog.setCancelable(true);
            dialog.setConfirmText("Delete");
            dialog.setConfirmButtonTheme("error primary");
            dialog.addConfirmListener(e -> {
                try {
                    service.delete(currentScreening);
                    Notification.show("Show deleted.", 3000, Notification.Position.BOTTOM_START)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    refreshGrid();
                    clearForm();
                } catch (IllegalStateException ex) {
                    Notification.show(ex.getMessage(), 5000, Notification.Position.BOTTOM_START)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
            dialog.open();
        });

        cancel.addClickListener(event -> {
            clearForm();
            grid.asSingleSelect().clear();
        });
    }

    private boolean validateForm() {
        return movie.getValue() != null
                && date.getValue() != null
                && time.getValue() != null
                && availableSeats.getValue() != null
                && availableSeats.getValue() > 0;
    }

    private VerticalLayout createGridLayout() {
        var addButton = new Button("Add Show", event -> {
            grid.asSingleSelect().clear();
            clearForm();
        });

        var layout = new VerticalLayout(new H2("Shows"), addButton, grid);
        layout.setSizeFull();
        return layout;
    }

    private FormLayout createFormLayout() {
        var formLayout = new FormLayout();
        formLayout.add(movie, date, time, availableSeats);
        return formLayout;
    }

    private VerticalLayout createEditorLayout(FormLayout formLayout) {
        var buttons = new HorizontalLayout(save, delete, cancel);
        var layout = new VerticalLayout(new H2("Edit Show"), formLayout, buttons);
        layout.setPadding(true);
        layout.setSpacing(true);
        return layout;
    }

    private void refreshGrid() {
        grid.setItems(service.findAll());
        movie.setItems(service.findAllMovies());
    }

    private void populateForm(Screening screening) {
        currentScreening = screening;
        movie.setValue(screening.getMovie());
        date.setValue(screening.getStartTime().toLocalDate());
        time.setValue(screening.getStartTime().toLocalTime());
        availableSeats.setValue(screening.getAvailableSeats());
        delete.setEnabled(screening.getId() != null);
    }

    private void clearForm() {
        currentScreening = null;
        movie.clear();
        date.clear();
        time.clear();
        availableSeats.clear();
        delete.setEnabled(false);
    }
}
