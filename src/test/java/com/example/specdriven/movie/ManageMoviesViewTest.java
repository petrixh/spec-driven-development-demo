package com.example.specdriven.movie;

import com.example.specdriven.show.ScreeningRoom;
import com.example.specdriven.show.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import com.example.specdriven.show.Ticket;
import com.example.specdriven.show.TicketRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Browserless view tests for UC-004: Manage Movies (Admin).
 */
@SpringBootTest
@WithMockUser(roles = "ADMIN")
class ManageMoviesViewTest extends SpringBrowserlessTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();
    }

    // --- View rendering ---

    @Test
    void viewRendersWithHeadingAndGrid() {
        navigate(ManageMoviesView.class);

        assertTrue($(H2.class).withText("Manage Movies").exists());
        assertTrue($(Grid.class).exists());
    }

    @Test
    void viewShowsAddMovieButton() {
        navigate(ManageMoviesView.class);

        assertTrue($(Button.class).withText("Add Movie").exists());
    }

    @Test
    void viewShowsFormFields() {
        navigate(ManageMoviesView.class);

        assertTrue($(TextField.class).withPropertyValue(TextField::getLabel, "Title").exists());
        assertTrue($(IntegerField.class).withPropertyValue(IntegerField::getLabel, "Duration (minutes)").exists());
        assertTrue($(TextArea.class).withPropertyValue(TextArea::getLabel, "Description").exists());
        assertTrue($(Upload.class).exists());
    }

    @Test
    void gridDisplaysExistingMovies() {
        movieRepository.save(new Movie("Inception", "desc", 148, null));
        movieRepository.save(new Movie("Dune", "desc", 155, null));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        assertEquals(2, test(grid).size());
    }

    // --- Add movie flow ---

    @Test
    void addMovieCreatesNewEntry() {
        navigate(ManageMoviesView.class);

        test($(Button.class).withText("Add Movie").single()).click();

        test($(TextField.class).withPropertyValue(TextField::getLabel, "Title").single())
                .setValue("New Movie");
        test($(IntegerField.class).withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single())
                .setValue(120);

        test($(Button.class).withText("Save").single()).click();

        assertEquals(1, movieRepository.count());
        assertEquals("New Movie", movieRepository.findAll().get(0).getTitle());
    }

    @Test
    void addMovieShowsSuccessNotification() {
        navigate(ManageMoviesView.class);

        test($(Button.class).withText("Add Movie").single()).click();

        test($(TextField.class).withPropertyValue(TextField::getLabel, "Title").single())
                .setValue("New Movie");
        test($(IntegerField.class).withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single())
                .setValue(120);

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Movie saved", test($(Notification.class).single()).getText());
    }

    // --- Edit movie flow ---

    @Test
    void selectingMovieInGridPopulatesForm() {
        Movie movie = movieRepository.save(new Movie("Inception", "A thriller", 148, "poster.jpg"));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        TextField titleField = $(TextField.class)
                .withPropertyValue(TextField::getLabel, "Title").single();
        assertEquals("Inception", titleField.getValue());

        IntegerField durationField = $(IntegerField.class)
                .withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single();
        assertEquals(148, durationField.getValue());
    }

    @Test
    void editMovieUpdatesExisting() {
        Movie movie = movieRepository.save(new Movie("Old Title", "desc", 100, null));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        test($(TextField.class).withPropertyValue(TextField::getLabel, "Title").single())
                .setValue("New Title");
        test($(Button.class).withText("Save").single()).click();

        Movie updated = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals("New Title", updated.getTitle());
    }

    // --- Validation ---

    @Test
    void saveWithBlankTitleShowsError() {
        navigate(ManageMoviesView.class);

        test($(Button.class).withText("Add Movie").single()).click();

        test($(IntegerField.class).withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single())
                .setValue(120);

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Title is required", test($(Notification.class).single()).getText());
        assertEquals(0, movieRepository.count());
    }

    @Test
    void saveWithDuplicateTitleShowsError() {
        movieRepository.save(new Movie("Inception", "desc", 148, null));

        navigate(ManageMoviesView.class);

        test($(Button.class).withText("Add Movie").single()).click();

        test($(TextField.class).withPropertyValue(TextField::getLabel, "Title").single())
                .setValue("Inception");
        test($(IntegerField.class).withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single())
                .setValue(120);

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("A movie with this title already exists", test($(Notification.class).single()).getText());
        assertEquals(1, movieRepository.count());
    }

    @Test
    void durationFieldHasMinimumOfOne() {
        // BR-02: Duration must be a positive number — enforced at field level
        navigate(ManageMoviesView.class);

        IntegerField durationField = $(IntegerField.class)
                .withPropertyValue(IntegerField::getLabel, "Duration (minutes)").single();
        assertEquals(1, durationField.getMin());
        assertTrue(durationField.isRequired());
    }

    @Test
    void saveWithNullDurationShowsError() {
        // BR-02: Duration must be a positive number
        navigate(ManageMoviesView.class);

        test($(Button.class).withText("Add Movie").single()).click();

        test($(TextField.class).withPropertyValue(TextField::getLabel, "Title").single())
                .setValue("Test Movie");
        // Don't set duration — leave null

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Duration must be a positive number", test($(Notification.class).single()).getText());
        assertEquals(0, movieRepository.count());
    }

    @Test
    void posterUploadAcceptsPngAndJpgOnly() {
        // BR-04: Poster upload accepts PNG and JPG, max 2 MB
        navigate(ManageMoviesView.class);

        Upload upload = $(Upload.class).single();
        List<String> accepted = upload.getAcceptedFileTypes();
        String joined = String.join(",", accepted);
        assertTrue(joined.contains("image/png") || joined.contains(".png"));
        assertTrue(joined.contains("image/jpeg") || joined.contains(".jpg") || joined.contains(".jpeg"));
    }

    @Test
    void posterUploadMaxSize2MB() {
        // BR-04: max 2 MB
        navigate(ManageMoviesView.class);

        Upload upload = $(Upload.class).single();
        assertEquals(2 * 1024 * 1024, upload.getMaxFileSize());
    }

    // --- Delete movie flow ---

    @Test
    void deleteButtonHiddenInitially() {
        movieRepository.save(new Movie("Inception", "desc", 148, null));

        navigate(ManageMoviesView.class);

        // Delete button exists but is not visible, so the query finds nothing
        assertFalse($(Button.class).withText("Delete").exists());
    }

    @Test
    void deleteButtonVisibleWhenMovieSelected() {
        Movie movie = movieRepository.save(new Movie("Inception", "desc", 148, null));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        assertTrue($(Button.class).withText("Delete").exists());
    }

    @Test
    void deleteMovieRemovesFromDatabase() {
        Movie movie = movieRepository.save(new Movie("Inception", "desc", 148, null));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        test($(Button.class).withText("Delete").single()).click();

        assertEquals(0, movieRepository.count());
        assertTrue($(Notification.class).exists());
        assertEquals("Movie deleted", test($(Notification.class).single()).getText());
    }

    @Test
    void deleteMovieWithFutureSoldTicketsShowsError() {
        Movie movie = movieRepository.save(new Movie("Inception", "desc", 148, null));
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room A", 5, 8));
        Show futureShow = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));
        ticketRepository.save(new Ticket(1, 1, "Alice", "alice@example.com", futureShow));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        test($(Button.class).withText("Delete").single()).click();

        assertEquals(1, movieRepository.count());
        assertTrue($(Notification.class).exists());
        assertEquals("Cannot delete: this movie has future shows with sold tickets",
                test($(Notification.class).single()).getText());
    }

    // --- Cancel ---

    @Test
    void cancelClearsForm() {
        Movie movie = movieRepository.save(new Movie("Inception", "desc", 148, null));

        navigate(ManageMoviesView.class);

        @SuppressWarnings("unchecked")
        Grid<Movie> grid = $(Grid.class).single();
        grid.asSingleSelect().setValue(movie);

        test($(Button.class).withText("Cancel").single()).click();

        TextField titleField = $(TextField.class)
                .withPropertyValue(TextField::getLabel, "Title").single();
        assertEquals("", titleField.getValue());
    }
}
