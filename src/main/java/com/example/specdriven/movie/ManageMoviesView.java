package com.example.specdriven.movie;

import com.example.specdriven.admin.AdminLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/movies", layout = AdminLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Manage Movies")
public class ManageMoviesView extends VerticalLayout {

    private final MovieService movieService;
    private final Grid<Movie> grid = new Grid<>(Movie.class, false);
    private final TextField titleField = new TextField("Title");
    private final TextArea descriptionField = new TextArea("Description");
    private final IntegerField durationField = new IntegerField("Duration (minutes)");
    private final VerticalLayout formContainer = new VerticalLayout();
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");

    private Movie currentMovie;
    private String uploadedPosterFileName;

    public ManageMoviesView(MovieService movieService) {
        this.movieService = movieService;
        setSizeFull();

        configureGrid();
        configureForm();

        Button addButton = new Button("Add Movie");
        addButton.addThemeVariants(ButtonVariant.PRIMARY);
        addButton.addClickListener(e -> addMovie());

        HorizontalLayout content = new HorizontalLayout(grid, formContainer);
        content.setSizeFull();
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, formContainer);

        add(addButton, content);
        formContainer.setVisible(false);
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(Movie::getTitle).setHeader("Title").setSortable(true);
        grid.addColumn(Movie::getDurationMinutes).setHeader("Duration (min)").setSortable(true);
        grid.addColumn(movie -> movieService.getShowCount(movie)).setHeader("Shows");
        grid.asSingleSelect().addValueChangeListener(e -> editMovie(e.getValue()));
        grid.setSizeFull();
    }

    private void configureForm() {
        titleField.setRequired(true);
        titleField.setMaxLength(200);
        descriptionField.setMaxLength(2000);
        durationField.setMin(1);
        durationField.setStepButtonsVisible(true);

        Upload posterUpload = new Upload(UploadHandler.inMemory((metadata, data) -> {
            uploadedPosterFileName = metadata.fileName();
            try {
                movieService.savePoster(uploadedPosterFileName, data);
            } catch (Exception e) {
                getUI().ifPresent(ui -> ui.access(() ->
                        Notification.show("Failed to upload poster: " + e.getMessage(),
                                5000, Notification.Position.TOP_CENTER)
                                .addThemeVariants(NotificationVariant.ERROR)));
            }
        }));
        posterUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        posterUpload.setMaxFileSize(2 * 1024 * 1024);
        posterUpload.setMaxFiles(1);

        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        saveButton.addClickListener(e -> save());

        deleteButton.addThemeVariants(ButtonVariant.ERROR);
        deleteButton.addClickListener(e -> delete());

        cancelButton.addClickListener(e -> clearForm());

        FormLayout formLayout = new FormLayout(titleField, durationField, descriptionField, posterUpload);
        formLayout.setColspan(descriptionField, 2);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);

        formContainer.add(new H3("Movie Details"), formLayout, buttons);
        formContainer.setPadding(true);
    }

    private void addMovie() {
        grid.asSingleSelect().clear();
        currentMovie = new Movie();
        populateForm();
        deleteButton.setVisible(false);
        formContainer.setVisible(true);
    }

    private void editMovie(Movie movie) {
        if (movie == null) {
            clearForm();
            return;
        }
        currentMovie = movie;
        populateForm();
        deleteButton.setVisible(true);
        formContainer.setVisible(true);
    }

    private void populateForm() {
        titleField.setValue(currentMovie.getTitle() != null ? currentMovie.getTitle() : "");
        descriptionField.setValue(currentMovie.getDescription() != null ? currentMovie.getDescription() : "");
        durationField.setValue(currentMovie.getDurationMinutes());
        uploadedPosterFileName = null;
    }

    private void save() {
        try {
            currentMovie.setTitle(titleField.getValue());
            currentMovie.setDescription(descriptionField.getValue());
            currentMovie.setDurationMinutes(durationField.getValue());
            if (uploadedPosterFileName != null) {
                currentMovie.setPosterFileName(uploadedPosterFileName);
            }
            movieService.save(currentMovie);
            Notification.show("Movie saved", 3000, Notification.Position.TOP_CENTER);
            clearForm();
            refreshGrid();
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }

    private void delete() {
        try {
            movieService.delete(currentMovie);
            Notification.show("Movie deleted", 3000, Notification.Position.TOP_CENTER);
            clearForm();
            refreshGrid();
        } catch (IllegalStateException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }

    private void clearForm() {
        currentMovie = null;
        titleField.clear();
        descriptionField.clear();
        durationField.clear();
        uploadedPosterFileName = null;
        formContainer.setVisible(false);
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(movieService.findAll());
    }
}
