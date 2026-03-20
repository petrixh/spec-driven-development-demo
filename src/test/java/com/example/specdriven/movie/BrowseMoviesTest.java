package com.example.specdriven.movie;

import com.example.specdriven.room.ScreeningRoom;
import com.example.specdriven.room.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BrowseMoviesTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository roomRepository;

    private MovieEndpoint movieEndpoint;

    @BeforeEach
    public void setup() {
        movieEndpoint = new MovieEndpoint(movieRepository);
    }

    @Test
    public void onlyMoviesWithFutureShowsAreReturned() {
        showRepository.deleteAll();
        movieRepository.deleteAll();
        roomRepository.deleteAll();

        Movie m1 = createMovie("Future Movie", 120);
        Movie m2 = createMovie("Past Movie", 90);
        createMovie("No Show Movie", 100);

        ScreeningRoom room = new ScreeningRoom();
        room.setName("Room 1");
        room.setRows(10);
        room.setSeatsPerRow(10);
        roomRepository.save(room);

        // Future show
        Show s1 = new Show();
        s1.setMovie(m1);
        s1.setDateTime(LocalDateTime.now().plusDays(1));
        s1.setScreeningRoom(room);
        showRepository.save(s1);

        // Past show
        Show s2 = new Show();
        s2.setMovie(m2);
        s2.setDateTime(LocalDateTime.now().minusDays(1));
        s2.setScreeningRoom(room);
        showRepository.save(s2);

        List<Movie> movies = movieEndpoint.getMovies();

        assertThat(movies).hasSize(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Future Movie");
    }

    @Test
    public void moviesAreSortedAlphabeticallyByTitle() {
        showRepository.deleteAll();
        movieRepository.deleteAll();
        roomRepository.deleteAll();

        Movie mB = createMovie("B Movie", 120);
        Movie mA = createMovie("A Movie", 120);

        ScreeningRoom room = new ScreeningRoom();
        room.setName("Room 1");
        room.setRows(10);
        room.setSeatsPerRow(10);
        roomRepository.save(room);

        createShow(mA, room, LocalDateTime.now().plusDays(1));
        createShow(mB, room, LocalDateTime.now().plusDays(1));

        List<Movie> movies = movieEndpoint.getMovies();

        assertThat(movies).hasSize(2);
        assertThat(movies.get(0).getTitle()).isEqualTo("A Movie");
        assertThat(movies.get(1).getTitle()).isEqualTo("B Movie");
    }

    private Movie createMovie(String title, int duration) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setDurationMinutes(duration);
        return movieRepository.save(m);
    }

    private void createShow(Movie movie, ScreeningRoom room, LocalDateTime dateTime) {
        Show s = new Show();
        s.setMovie(movie);
        s.setScreeningRoom(room);
        s.setDateTime(dateTime);
        showRepository.save(s);
    }
}
