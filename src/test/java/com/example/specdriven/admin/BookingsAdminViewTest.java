package com.example.specdriven.admin;

import com.example.specdriven.data.Booking;
import com.example.specdriven.data.BookingRepository;
import com.example.specdriven.data.Movie;
import com.example.specdriven.data.MovieRepository;
import com.example.specdriven.data.Screening;
import com.example.specdriven.data.ScreeningRepository;
import com.example.specdriven.security.LoginView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.testbench.unit.SpringUIUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class BookingsAdminViewTest extends SpringUIUnitTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Screening screening;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        screeningRepository.deleteAll();
        movieRepository.deleteAll();

        Movie movie = movieRepository.save(new Movie("Inception", "A thriller", "inception.png", 148));
        screening = screeningRepository.save(new Screening(movie, LocalDateTime.of(2026, 3, 1, 20, 0), 100));
    }

    @Test
    void gridShowsAllBookings() {
        bookingRepository.save(new Booking("ABC-001", "Alice", "alice@test.com", screening, 2));
        bookingRepository.save(new Booking("DEF-002", "Bob", "bob@test.com", screening, 3));

        navigate(BookingsAdminView.class);

        @SuppressWarnings("unchecked")
        Grid<Booking> grid = $(Grid.class).first();
        assertEquals(2, grid.getGenericDataView().getItems().count());
    }

    @Test
    void searchByConfirmationCode() {
        bookingRepository.save(new Booking("ABC-001", "Alice", "alice@test.com", screening, 2));
        bookingRepository.save(new Booking("DEF-002", "Bob", "bob@test.com", screening, 3));

        navigate(BookingsAdminView.class);

        test($(TextField.class).first()).setValue("ABC");

        @SuppressWarnings("unchecked")
        Grid<Booking> grid = $(Grid.class).first();
        assertEquals(1, grid.getGenericDataView().getItems().count());
        assertEquals("ABC-001", grid.getGenericDataView().getItems().findFirst().orElseThrow()
                .getConfirmationCode());
    }

    @Test
    void searchByCustomerName() {
        bookingRepository.save(new Booking("ABC-001", "Alice Smith", "alice@test.com", screening, 2));
        bookingRepository.save(new Booking("DEF-002", "Bob Jones", "bob@test.com", screening, 3));

        navigate(BookingsAdminView.class);

        test($(TextField.class).first()).setValue("bob");

        @SuppressWarnings("unchecked")
        Grid<Booking> grid = $(Grid.class).first();
        assertEquals(1, grid.getGenericDataView().getItems().count());
        assertEquals("Bob Jones", grid.getGenericDataView().getItems().findFirst().orElseThrow()
                .getCustomerName());
    }

    @Test
    void searchByEmail() {
        bookingRepository.save(new Booking("ABC-001", "Alice", "alice@example.com", screening, 2));
        bookingRepository.save(new Booking("DEF-002", "Bob", "bob@other.com", screening, 3));

        navigate(BookingsAdminView.class);

        test($(TextField.class).first()).setValue("other.com");

        @SuppressWarnings("unchecked")
        Grid<Booking> grid = $(Grid.class).first();
        assertEquals(1, grid.getGenericDataView().getItems().count());
        assertEquals("bob@other.com", grid.getGenericDataView().getItems().findFirst().orElseThrow()
                .getCustomerEmail());
    }

    @Test
    void gridDisplaysAllRequiredColumns() {
        navigate(BookingsAdminView.class);

        @SuppressWarnings("unchecked")
        Grid<Booking> grid = $(Grid.class).first();
        var headers = grid.getColumns().stream()
                .map(col -> col.getHeaderText())
                .toList();

        assertTrue(headers.contains("Confirmation Code"));
        assertTrue(headers.contains("Customer Name"));
        assertTrue(headers.contains("Email"));
        assertTrue(headers.contains("Movie"));
        assertTrue(headers.contains("Show Time"));
        assertTrue(headers.contains("Tickets"));
    }

    @Test
    @WithAnonymousUser
    void anonymousUserRedirectedToLogin() {
        navigate("admin/bookings", LoginView.class);
    }
}
