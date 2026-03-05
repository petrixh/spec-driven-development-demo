package com.example.specdriven.admin;

import com.example.specdriven.data.Movie;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/movies", layout = AdminLayout.class)
@PageTitle("Manage Movies")
@RolesAllowed("ADMIN")
public class MoviesAdminView extends VerticalLayout {

    private final MovieAdminService service;

    private final Grid<Movie> grid = new Grid<>(Movie.class, false);
    private final TextField title = new TextField("Title");
    private final TextArea description = new TextArea("Description");
    private final TextField posterFileName = new TextField("Poster Filename");
    private final IntegerField durationMinutes = new IntegerField("Duration (minutes)");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private final Binder<Movie> binder = new BeanValidationBinder<>(Movie.class);
    private Movie currentMovie;

    public MoviesAdminView(MovieAdminService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureBinder();
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
        grid.addColumn(Movie::getTitle).setHeader("Title").setAutoWidth(true);
        grid.addColumn(Movie::getDurationMinutes).setHeader("Duration (min)").setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue());
            } else {
                clearForm();
            }
        });
    }

    private void configureBinder() {
        binder.forField(title)
                .asRequired("Title is required")
                .bind(Movie::getTitle, Movie::setTitle);
        binder.forField(description)
                .asRequired("Description is required")
                .bind(Movie::getDescription, Movie::setDescription);
        binder.forField(posterFileName)
                .asRequired("Poster filename is required")
                .bind(Movie::getPosterFileName, Movie::setPosterFileName);
        binder.forField(durationMinutes)
                .asRequired("Duration is required")
                .withValidator(d -> d != null && d > 0, "Duration must be positive")
                .bind(Movie::getDurationMinutes, Movie::setDurationMinutes);
    }

    private void configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            try {
                if (currentMovie == null) {
                    currentMovie = new Movie("", "", "", 0);
                }
                binder.writeBean(currentMovie);
                service.save(currentMovie);
                Notification.show("Movie saved.", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                refreshGrid();
                clearForm();
            } catch (ValidationException e) {
                Notification.show("Please check the form for errors.", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> {
            if (currentMovie == null || currentMovie.getId() == null) {
                return;
            }
            var dialog = new ConfirmDialog();
            dialog.setHeader("Delete \"" + currentMovie.getTitle() + "\"?");
            dialog.setText("Are you sure you want to permanently delete this movie?");
            dialog.setCancelable(true);
            dialog.setConfirmText("Delete");
            dialog.setConfirmButtonTheme("error primary");
            dialog.addConfirmListener(e -> {
                try {
                    service.delete(currentMovie);
                    Notification.show("Movie deleted.", 3000, Notification.Position.BOTTOM_START)
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

    private VerticalLayout createGridLayout() {
        var addButton = new Button("Add Movie", event -> {
            grid.asSingleSelect().clear();
            clearForm();
        });

        var layout = new VerticalLayout(new H2("Movies"), addButton, grid);
        layout.setSizeFull();
        return layout;
    }

    private FormLayout createFormLayout() {
        var formLayout = new FormLayout();
        formLayout.add(title, description, posterFileName, durationMinutes);
        formLayout.setColspan(description, 2);
        return formLayout;
    }

    private VerticalLayout createEditorLayout(FormLayout formLayout) {
        var buttons = new HorizontalLayout(save, delete, cancel);
        var layout = new VerticalLayout(new H2("Edit Movie"), formLayout, buttons);
        layout.setPadding(true);
        layout.setSpacing(true);
        return layout;
    }

    private void refreshGrid() {
        grid.setItems(service.findAll());
    }

    private void populateForm(Movie movie) {
        currentMovie = movie;
        binder.readBean(movie);
        delete.setEnabled(movie.getId() != null);
    }

    private void clearForm() {
        currentMovie = null;
        binder.readBean(new Movie("", "", "", 0));
        delete.setEnabled(false);
    }
}
