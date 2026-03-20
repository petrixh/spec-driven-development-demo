package com.example.specdriven.admin;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import jakarta.annotation.security.RolesAllowed;

@Route("admin/movies")
@RolesAllowed("ADMIN")
public class MovieAdminView extends VerticalLayout {

    private final MovieAdminService movieAdminService;
    private final ShowRepository showRepository;
    private final Clock clock;

    private final Grid<Movie> grid = new Grid<>(Movie.class, false);
    private final TextField titleField = new TextField("Title");
    private final TextArea descriptionField = new TextArea("Description");
    private final IntegerField durationField = new IntegerField("Duration (minutes)");
    private final MemoryBuffer uploadBuffer = new MemoryBuffer();
    private final Upload posterUpload = new Upload(uploadBuffer);

    private Movie editingMovie = null;
    private String pendingPosterFilename = null;
    private byte[] pendingPosterBytes = null;

    public MovieAdminView(MovieAdminService movieAdminService, ShowRepository showRepository, Clock clock) {
        this.movieAdminService = movieAdminService;
        this.showRepository = showRepository;
        this.clock = clock;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        // Reset text color — inherited white from .app-shell dark cinema theme
        getStyle().set("color", "#1a1a1a").set("background", "#f5f5f5");

        add(new RouterLink("← Admin Index", AdminIndexView.class));
        add(new H2("Manage Movies"));

        configureGrid();
        configureUpload();

        HorizontalLayout mainLayout = new HorizontalLayout(buildGridSection(), buildFormSection());
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(2, mainLayout.getComponentAt(0));
        mainLayout.setFlexGrow(1, mainLayout.getComponentAt(1));

        add(mainLayout);
        refreshGrid();
    }

    private VerticalLayout buildGridSection() {
        Button addBtn = new Button("Add Movie", e -> openNew());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout section = new VerticalLayout(addBtn, grid);
        section.setPadding(false);
        section.setSizeFull();
        return section;
    }

    private VerticalLayout buildFormSection() {
        titleField.setRequired(true);
        titleField.setWidth("100%");
        descriptionField.setWidth("100%");
        descriptionField.setMinHeight("80px");
        durationField.setRequired(true);
        durationField.setMin(1);
        posterUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        posterUpload.setMaxFileSize(2 * 1024 * 1024);
        posterUpload.setMaxFiles(1);

        FormLayout form = new FormLayout(titleField, durationField, descriptionField);
        form.setColspan(descriptionField, 2);
        form.setWidth("100%");

        Div posterDiv = new Div(new Span("Poster (PNG/JPG, max 2 MB)"), posterUpload);
        posterDiv.getStyle().set("margin-top", "10px");

        Button saveBtn = new Button("Save", e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelBtn = new Button("Cancel", e -> clearForm());

        HorizontalLayout actions = new HorizontalLayout(saveBtn, cancelBtn);

        VerticalLayout formSection = new VerticalLayout(new H2("Movie Details"), form, posterDiv, actions);
        formSection.setWidth("380px");
        formSection.setPadding(true);
        formSection.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        return formSection;
    }

    private void configureGrid() {
        grid.addColumn(Movie::getTitle).setHeader("Title").setSortable(true).setFlexGrow(1);
        grid.addColumn(Movie::getDurationMinutes).setHeader("Duration (min)").setWidth("120px").setFlexGrow(0);
        grid.addColumn(movie -> showRepository.existsByMovieIdAndDateTimeAfter(movie.getId(), LocalDateTime.now(clock))
                ? "Yes" : "No")
                .setHeader("Has Future Shows").setWidth("140px").setFlexGrow(0);
        grid.addComponentColumn(movie -> {
            Button editBtn = new Button("Edit", e -> openEdit(movie));
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button deleteBtn = new Button("Delete", e -> confirmDelete(movie));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setWidth("160px").setFlexGrow(0);
        grid.setHeight("400px");
    }

    private void configureUpload() {
        posterUpload.addSucceededListener(event -> {
            pendingPosterFilename = event.getFileName();
            try (InputStream is = uploadBuffer.getInputStream()) {
                pendingPosterBytes = is.readAllBytes();
            } catch (IOException ex) {
                showError("Failed to read uploaded file.");
            }
        });
    }

    private void openNew() {
        editingMovie = new Movie(null, null, null, null);
        clearForm();
    }

    private void openEdit(Movie movie) {
        editingMovie = movie;
        titleField.setValue(Optional.ofNullable(movie.getTitle()).orElse(""));
        descriptionField.setValue(Optional.ofNullable(movie.getDescription()).orElse(""));
        durationField.setValue(movie.getDurationMinutes());
        pendingPosterFilename = null;
        pendingPosterBytes = null;
    }

    private void save() {
        String title = titleField.getValue().trim();
        if (title.isEmpty()) {
            showError("Title is required.");
            return;
        }
        Integer duration = durationField.getValue();
        if (duration == null || duration <= 0) {
            showError("Duration must be a positive number.");
            return;
        }

        if (editingMovie == null) {
            editingMovie = new Movie(null, null, null, null);
        }

        editingMovie.setTitle(title);
        editingMovie.setDescription(descriptionField.getValue().trim());
        editingMovie.setDurationMinutes(duration);

        if (pendingPosterBytes != null) {
            try {
                String filename = movieAdminService.savePoster(pendingPosterFilename, pendingPosterBytes);
                editingMovie.setPosterFileName(filename);
            } catch (IOException | IllegalArgumentException ex) {
                showError("Poster error: " + ex.getMessage());
                return;
            }
        }

        try {
            movieAdminService.save(editingMovie);
            Notification.show("Movie saved.", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            clearForm();
            refreshGrid();
        } catch (Exception ex) {
            showError("Save failed: " + ex.getMessage());
        }
    }

    private void confirmDelete(Movie movie) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete \"" + movie.getTitle() + "\"?");
        dialog.setText("This action cannot be undone.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> deleteMovie(movie));
        dialog.open();
    }

    private void deleteMovie(Movie movie) {
        try {
            movieAdminService.delete(movie);
            Notification.show("Movie deleted.", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            clearForm();
            refreshGrid();
        } catch (IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearForm() {
        editingMovie = null;
        titleField.clear();
        descriptionField.clear();
        durationField.clear();
        pendingPosterFilename = null;
        pendingPosterBytes = null;
    }

    private void refreshGrid() {
        grid.setItems(movieAdminService.findAll());
    }

    private void showError(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
