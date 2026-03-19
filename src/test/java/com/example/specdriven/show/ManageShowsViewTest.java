package com.example.specdriven.show;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Browserless view tests for UC-005: Manage Shows (Admin).
 */
@SpringBootTest
@WithMockUser(roles = "ADMIN")
class ManageShowsViewTest extends SpringBrowserlessTest {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Movie movie;
    private ScreeningRoom room;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        movie = movieRepository.save(new Movie("Inception", "desc", 148, null));
        room = screeningRoomRepository.save(new ScreeningRoom("Room A", 5, 8));
    }

    // --- View rendering ---

    @Test
    void viewRendersWithHeadingAndGrid() {
        navigate(ManageShowsView.class);

        assertTrue($(H2.class).withText("Manage Shows").exists());
        assertTrue($(Grid.class).exists());
    }

    @Test
    void viewShowsAddShowButton() {
        navigate(ManageShowsView.class);

        assertTrue($(Button.class).withText("Add Show").exists());
    }

    @Test
    void viewShowsFormFields() {
        navigate(ManageShowsView.class);

        assertTrue($(ComboBox.class).withPropertyValue(ComboBox::getLabel, "Movie").exists());
        assertTrue($(ComboBox.class).withPropertyValue(ComboBox::getLabel, "Screening Room").exists());
        assertTrue($(DateTimePicker.class).exists());
    }

    @Test
    void gridDisplaysExistingShows() {
        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));
        showRepository.save(new Show(LocalDateTime.now().plusDays(2), movie, room));

        navigate(ManageShowsView.class);

        @SuppressWarnings("unchecked")
        Grid<Show> showGrid = $(Grid.class).single();
        assertEquals(2, test(showGrid).size());
    }

    // --- Add show flow ---

    @Test
    @SuppressWarnings("unchecked")
    void addShowCreatesNewEntry() {
        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();

        ComboBox<Movie> movieField = $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single();
        test(movieField).selectItem(movie.getTitle());

        ComboBox<ScreeningRoom> roomField = $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Screening Room").single();
        test(roomField).selectItem(room.getName());

        DateTimePicker dtPicker = $(DateTimePicker.class).single();
        test(dtPicker).setValue(LocalDateTime.now().plusDays(5));

        test($(Button.class).withText("Save").single()).click();

        assertEquals(1, showRepository.count());
        assertTrue($(Notification.class).exists());
        assertEquals("Show saved", test($(Notification.class).single()).getText());
    }

    // --- Validation ---

    @Test
    void saveWithoutMovieShowsError() {
        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();
        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Movie is required", test($(Notification.class).single()).getText());
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveWithPastDateShowsError() {
        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();

        test((ComboBox<Movie>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single()).selectItem(movie.getTitle());
        test((ComboBox<ScreeningRoom>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Screening Room").single()).selectItem(room.getName());
        test($(DateTimePicker.class).single()).setValue(LocalDateTime.now().minusDays(1));

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Show must be scheduled in the future",
                test($(Notification.class).single()).getText());
    }

    @Test
    @SuppressWarnings("unchecked")
    void overlappingShowRejected() {
        // Existing show: 148 min + 30 min buffer
        LocalDateTime existingTime = LocalDateTime.now().plusDays(5).withHour(14).withMinute(0);
        showRepository.save(new Show(existingTime, movie, room));

        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();

        test((ComboBox<Movie>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single()).selectItem(movie.getTitle());
        test((ComboBox<ScreeningRoom>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Screening Room").single()).selectItem(room.getName());
        // Overlapping: 2 hours after existing (which needs ~3h slot)
        test($(DateTimePicker.class).single()).setValue(existingTime.plusMinutes(120));

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertTrue(test($(Notification.class).single()).getText().contains("overlapping"));
        assertEquals(1, showRepository.count()); // no new show created
    }

    // --- Edit show ---

    @Test
    @SuppressWarnings("unchecked")
    void selectingShowPopulatesFormWithMovieDisabled() {
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));

        navigate(ManageShowsView.class);

        Grid<Show> showGrid = $(Grid.class).single();
        showGrid.asSingleSelect().setValue(show);

        ComboBox<Movie> movieField = $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single();
        assertEquals(movie, movieField.getValue());
        assertFalse(movieField.isEnabled()); // BR-04
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveWithoutRoomShowsError() {
        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();

        test((ComboBox<Movie>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single()).selectItem(movie.getTitle());
        test($(DateTimePicker.class).single()).setValue(LocalDateTime.now().plusDays(5));

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Screening room is required", test($(Notification.class).single()).getText());
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveWithoutDateTimeShowsError() {
        navigate(ManageShowsView.class);

        test($(Button.class).withText("Add Show").single()).click();

        test((ComboBox<Movie>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Movie").single()).selectItem(movie.getTitle());
        test((ComboBox<ScreeningRoom>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Screening Room").single()).selectItem(room.getName());

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Date and time are required", test($(Notification.class).single()).getText());
    }

    @Test
    @SuppressWarnings("unchecked")
    void editShowUpdatesDateTimeAndRoom() {
        // AC: Admin can edit date/time and room of a show
        ScreeningRoom roomB = screeningRoomRepository.save(new ScreeningRoom("Room B", 3, 6));
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));

        navigate(ManageShowsView.class);

        Grid<Show> showGrid = $(Grid.class).single();
        showGrid.asSingleSelect().setValue(show);

        LocalDateTime newTime = LocalDateTime.now().plusDays(10);
        test((ComboBox<ScreeningRoom>) $(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Screening Room").single()).selectItem(roomB.getName());
        test($(DateTimePicker.class).single()).setValue(newTime);

        test($(Button.class).withText("Save").single()).click();

        assertTrue($(Notification.class).exists());
        assertEquals("Show saved", test($(Notification.class).single()).getText());

        Show updated = showRepository.findById(show.getId()).orElseThrow();
        assertEquals("Room B", updated.getScreeningRoom().getName());
    }

    // --- Delete show ---

    @Test
    void deleteShowWithNoTickets() {
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));

        navigate(ManageShowsView.class);

        @SuppressWarnings("unchecked")
        Grid<Show> showGrid = $(Grid.class).single();
        showGrid.asSingleSelect().setValue(show);

        test($(Button.class).withText("Delete").single()).click();

        assertEquals(0, showRepository.count());
        assertTrue($(Notification.class).exists());
        assertEquals("Show deleted", test($(Notification.class).single()).getText());
    }

    @Test
    void deleteShowWithSoldTicketsShowsError() {
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));
        ticketRepository.save(new Ticket(1, 1, "Alice", "alice@example.com", show));

        navigate(ManageShowsView.class);

        @SuppressWarnings("unchecked")
        Grid<Show> showGrid = $(Grid.class).single();
        showGrid.asSingleSelect().setValue(show);

        test($(Button.class).withText("Delete").single()).click();

        assertEquals(1, showRepository.count()); // not deleted
        assertTrue($(Notification.class).exists());
        assertTrue(test($(Notification.class).single()).getText().contains("sold ticket"));
    }

    // --- Delete button visibility ---

    @Test
    void deleteButtonHiddenInitially() {
        navigate(ManageShowsView.class);

        assertFalse($(Button.class).withText("Delete").exists());
    }
}
