package com.example.specdriven.movie;

import com.example.specdriven.show.ShowRepository;
import com.example.specdriven.show.TicketRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
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
import jakarta.annotation.security.RolesAllowed;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Route("admin/movies")
@RolesAllowed("ADMIN")
public class ManageMoviesView extends VerticalLayout {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    private final Grid<Movie> grid = new Grid<>(Movie.class, false);
    private final TextField titleField = new TextField("Title");
    private final TextArea descriptionField = new TextArea("Description");
    private final IntegerField durationField = new IntegerField("Duration (minutes)");
    private final Upload posterUpload;
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");

    private Movie currentMovie;
    private String uploadedPosterFileName;

    public ManageMoviesView(MovieRepository movieRepository,
                            ShowRepository showRepository,
                            TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;

        addClassName("admin-view");
        setSizeFull();

        add(new H2("Manage Movies"));

        // Grid setup
        grid.addColumn(Movie::getTitle).setHeader("Title").setSortable(true);
        grid.addColumn(Movie::getDurationMinutes).setHeader("Duration (min)").setSortable(true);
        grid.addColumn(movie -> showRepository.countByMovieId(movie.getId()))
                .setHeader("Shows").setSortable(true);
        grid.asSingleSelect().addValueChangeListener(e -> editMovie(e.getValue()));

        // Form setup
        titleField.setRequired(true);
        titleField.setWidthFull();
        descriptionField.setWidthFull();
        durationField.setMin(1);
        durationField.setRequired(true);

        MemoryBuffer buffer = new MemoryBuffer();
        posterUpload = new Upload(buffer);
        posterUpload.addSucceededListener(event -> {
            String fileName = System.currentTimeMillis() + "-" + event.getFileName();
            try (InputStream is = buffer.getInputStream()) {
                Path postersDir = Path.of("posters");
                Files.createDirectories(postersDir);
                Files.write(postersDir.resolve(fileName), is.readAllBytes());
                uploadedPosterFileName = fileName;
                Notification.show("Poster uploaded", 3000, Notification.Position.TOP_CENTER);
            } catch (IOException ex) {
                Notification.show("Failed to save poster: " + ex.getMessage(), 5000,
                        Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.ERROR);
            }
        });
        posterUpload.setAcceptedFileTypes("image/png", ".png", "image/jpeg", ".jpg", ".jpeg");
        posterUpload.setMaxFileSize(2 * 1024 * 1024); // 2 MB
        posterUpload.setMaxFiles(1);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveMovie());

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> deleteMovie());

        cancelButton.addClickListener(e -> clearForm());

        FormLayout formLayout = new FormLayout();
        formLayout.add(titleField, durationField, descriptionField, posterUpload);
        formLayout.setColspan(descriptionField, 2);
        formLayout.setColspan(posterUpload, 2);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);

        VerticalLayout formContainer = new VerticalLayout(formLayout, buttonLayout);
        formContainer.setWidth("400px");
        formContainer.setPadding(false);

        Button addButton = new Button("Add Movie");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            grid.asSingleSelect().clear();
            clearForm();
            currentMovie = new Movie("", "", 0, null);
            deleteButton.setVisible(false);
            saveButton.setEnabled(true);
            cancelButton.setEnabled(true);
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

    private void editMovie(Movie movie) {
        if (movie == null) {
            clearForm();
            return;
        }
        currentMovie = movie;
        titleField.setValue(movie.getTitle() != null ? movie.getTitle() : "");
        descriptionField.setValue(movie.getDescription() != null ? movie.getDescription() : "");
        durationField.setValue(movie.getDurationMinutes());
        uploadedPosterFileName = movie.getPosterFileName();
        deleteButton.setVisible(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

    private void saveMovie() {
        String title = titleField.getValue();
        if (title == null || title.isBlank()) {
            Notification.show("Title is required", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
            return;
        }

        Integer duration = durationField.getValue();
        if (duration == null || duration <= 0) {
            Notification.show("Duration must be a positive number", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
            return;
        }

        // Check unique title
        boolean titleExists = currentMovie.getId() == null
                ? movieRepository.existsByTitle(title.trim())
                : movieRepository.existsByTitleAndIdNot(title.trim(), currentMovie.getId());
        if (titleExists) {
            Notification.show("A movie with this title already exists", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
            return;
        }

        currentMovie.setTitle(title.trim());
        currentMovie.setDescription(descriptionField.getValue());
        currentMovie.setDurationMinutes(duration);
        if (uploadedPosterFileName != null) {
            currentMovie.setPosterFileName(uploadedPosterFileName);
        }
        movieRepository.save(currentMovie);

        Notification.show("Movie saved", 3000, Notification.Position.TOP_CENTER);
        clearForm();
        refreshGrid();
    }

    private void deleteMovie() {
        if (currentMovie == null || currentMovie.getId() == null) {
            return;
        }

        // Check if movie has future shows with sold tickets
        boolean hasFutureSoldTickets = showRepository
                .findByMovieIdAndDateTimeAfterOrderByDateTimeAsc(currentMovie.getId(), LocalDateTime.now())
                .stream()
                .anyMatch(show -> ticketRepository.countByShowId(show.getId()) > 0);

        if (hasFutureSoldTickets) {
            Notification.show("Cannot delete: this movie has future shows with sold tickets", 5000,
                    Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.ERROR);
            return;
        }

        // Delete associated shows first
        showRepository.findByMovieId(currentMovie.getId())
                .forEach(show -> {
                    ticketRepository.findByShowId(show.getId())
                            .forEach(ticketRepository::delete);
                    showRepository.delete(show);
                });

        movieRepository.delete(currentMovie);
        Notification.show("Movie deleted", 3000, Notification.Position.TOP_CENTER);
        clearForm();
        refreshGrid();
    }

    private void clearForm() {
        currentMovie = null;
        titleField.clear();
        descriptionField.clear();
        durationField.clear();
        uploadedPosterFileName = null;
        deleteButton.setVisible(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(movieRepository.findAll());
    }
}
