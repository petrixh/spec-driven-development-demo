package com.example.specdriven.admin.ui;

import java.io.IOException;
import java.io.InputStream;

import jakarta.annotation.security.RolesAllowed;

import com.example.specdriven.catalog.application.MovieAdminFormData;
import com.example.specdriven.catalog.application.MovieAdminRowDto;
import com.example.specdriven.catalog.application.MovieAdminService;
import com.example.specdriven.catalog.application.PosterUpload;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/movies", layout = AdminLayout.class)
@PageTitle("Manage Movies")
@RolesAllowed("ADMIN")
public class MovieAdminView extends VerticalLayout {

    private final MovieAdminService movieAdminService;
    private final Grid<MovieAdminRowDto> grid = new Grid<>(MovieAdminRowDto.class, false);
    private final TextField title = new TextField("Title");
    private final TextArea description = new TextArea("Description");
    private final IntegerField durationMinutes = new IntegerField("Duration (min)");
    private final Div currentPoster = new Div();
    private final MemoryBuffer uploadBuffer = new MemoryBuffer();
    private final Upload upload = new Upload(uploadBuffer);
    private final Button save = new Button("Save");
    private final Button addMovie = new Button("Add Movie");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private Long editingMovieId;
    private String existingPosterFileName;

    public MovieAdminView(MovieAdminService movieAdminService) {
        this.movieAdminService = movieAdminService;

        setSizeFull();

        H1 heading = new H1("Manage Movies");
        configureGrid();
        configureForm();
        VerticalLayout formPanel = createFormPanel();

        HorizontalLayout toolbar = new HorizontalLayout(addMovie);
        HorizontalLayout body = new HorizontalLayout(grid, formPanel);
        body.setSizeFull();
        body.setFlexGrow(1.4, grid);
        body.setFlexGrow(1, formPanel);

        add(heading, toolbar, body);
        refreshGrid();
        editMovie(null);
    }

    private void configureGrid() {
        grid.addColumn(MovieAdminRowDto::title).setHeader("Title").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(MovieAdminRowDto::durationMinutes).setHeader("Duration").setAutoWidth(true);
        grid.addColumn(MovieAdminRowDto::scheduledShows).setHeader("Scheduled Shows").setAutoWidth(true);
        grid.setHeightFull();
        grid.asSingleSelect().addValueChangeListener(event -> editMovie(event.getValue()));
    }

    private void configureForm() {
        title.setRequired(true);
        description.setMaxLength(2000);
        description.setMinHeight("12rem");
        durationMinutes.setRequiredIndicatorVisible(true);
        durationMinutes.setStepButtonsVisible(true);
        durationMinutes.setMin(1);

        upload.setAcceptedFileTypes(".png", ".jpg", ".jpeg");
        upload.setMaxFileSize(2 * 1024 * 1024);
        upload.setDropAllowed(true);
        upload.addFileRejectedListener(event -> showError(event.getErrorMessage()));

        addMovie.addClickListener(event -> {
            grid.deselectAll();
            editMovie(null);
        });
        save.addClickListener(event -> saveMovie());
        delete.addClickListener(event -> deleteMovie());
        cancel.addClickListener(event -> {
            grid.deselectAll();
            editMovie(null);
        });

        delete.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR);
    }

    private VerticalLayout createFormPanel() {
        FormLayout form = new FormLayout();
        form.add(title, durationMinutes, description, currentPoster, upload);
        form.setColspan(description, 2);
        form.setColspan(currentPoster, 2);
        form.setColspan(upload, 2);

        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        VerticalLayout panel = new VerticalLayout(new Paragraph("Create or update a movie."), form, actions);
        panel.setWidth("420px");
        panel.setPadding(false);
        return panel;
    }

    private void saveMovie() {
        try {
            MovieAdminRowDto savedMovie = movieAdminService.saveMovie(
                    new MovieAdminFormData(
                            editingMovieId,
                            title.getValue(),
                            description.getValue(),
                            durationMinutes.getValue(),
                            existingPosterFileName),
                    readUpload());
            refreshGrid();
            grid.select(savedMovie);
            editMovie(savedMovie);
            Notification.show("Movie saved.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteMovie() {
        if (editingMovieId == null) {
            return;
        }
        try {
            movieAdminService.deleteMovie(editingMovieId);
            Notification.show("Movie deleted.");
            refreshGrid();
            grid.deselectAll();
            editMovie(null);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception.getMessage());
        }
    }

    private PosterUpload readUpload() {
        if (uploadBuffer.getFileName() == null || uploadBuffer.getFileName().isBlank()) {
            return null;
        }

        try (InputStream inputStream = uploadBuffer.getInputStream()) {
            byte[] content = inputStream.readAllBytes();
            upload.clearFileList();
            return new PosterUpload(uploadBuffer.getFileName(), content);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read uploaded poster.", exception);
        }
    }

    private void refreshGrid() {
        grid.setItems(movieAdminService.listMovies());
    }

    private void editMovie(MovieAdminRowDto row) {
        if (row == null) {
            editingMovieId = null;
            existingPosterFileName = null;
            title.clear();
            description.clear();
            durationMinutes.clear();
            currentPoster.setText("No poster selected");
            delete.setEnabled(false);
            return;
        }

        MovieAdminFormData movie = movieAdminService.getMovie(row.id());
        editingMovieId = movie.id();
        existingPosterFileName = movie.posterFileName();
        title.setValue(movie.title());
        description.setValue(movie.description() == null ? "" : movie.description());
        durationMinutes.setValue(movie.durationMinutes());
        currentPoster.setText(movie.posterFileName() == null ? "No poster uploaded" : "Current poster: " + movie.posterFileName());
        delete.setEnabled(true);
    }

    private void showError(String message) {
        Notification notification = Notification.show(message, 4000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
