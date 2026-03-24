package com.example.specdriven.data;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.example.specdriven.screeningroom.ScreeningRoom;
import com.example.specdriven.screeningroom.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ScreeningRoomRepository screeningRoomRepository;
    private final ShowRepository showRepository;

    public DataSeeder(MovieRepository movieRepository,
                      ScreeningRoomRepository screeningRoomRepository,
                      ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.screeningRoomRepository = screeningRoomRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> movies = createMovies();
        List<ScreeningRoom> rooms = createScreeningRooms();
        createShows(movies, rooms);
    }

    private List<Movie> createMovies() {
        return List.of(
                saveMovie("AI Developer 2", "A sequel about AI taking over software development. The machines are coding, but can they handle legacy code?", 118, "ai-developer-2.png"),
                saveMovie("The Gardening Incident", "A peaceful gardening competition turns into chaos when exotic plants develop a mind of their own.", 95, "gardening-incident.png"),
                saveMovie("Living in the Forest", "A documentary about people who abandoned city life to live among ancient trees.", 102, "living-in-the-forest.png"),
                saveMovie("The Null Mistake", "A Java developer's worst nightmare comes to life when NullPointerExceptions start manifesting in the real world.", 110, "null-mistake-java.png"),
                saveMovie("Pink Elephants", "A whimsical animation about elephants discovering they can fly, but only when no one is watching.", 88, "pink-elephants.png"),
                saveMovie("Reindeer Hunter", "A wildlife photographer embarks on an Arctic journey to capture the perfect shot of wild reindeer.", 130, "reindeer-hunter.png"),
                saveMovie("Sleeping with the Fishes", "An undercover agent infiltrates a marine biology lab, only to discover the fish are the real masterminds.", 105, "sleeping-with-the-fishes.png"),
                saveMovie("Threading a Needle", "A thrilling heist movie where the team must navigate impossible concurrency problems to crack the vault.", 115, "threading-a-needle.png")
        );
    }

    private Movie saveMovie(String title, String description, int duration, String posterFileName) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setDurationMinutes(duration);
        movie.setPosterFileName(posterFileName);
        return movieRepository.save(movie);
    }

    private List<ScreeningRoom> createScreeningRooms() {
        return List.of(
                saveRoom("Room 1", 8, 10),
                saveRoom("Room 2", 6, 8),
                saveRoom("Room 3", 5, 6)
        );
    }

    private ScreeningRoom saveRoom(String name, int rows, int seatsPerRow) {
        ScreeningRoom room = new ScreeningRoom();
        room.setName(name);
        room.setRows(rows);
        room.setSeatsPerRow(seatsPerRow);
        return screeningRoomRepository.save(room);
    }

    private void createShows(List<Movie> movies, List<ScreeningRoom> rooms) {
        LocalDate today = LocalDate.now();
        LocalTime[] times = {
                LocalTime.of(10, 0), LocalTime.of(13, 0),
                LocalTime.of(16, 0), LocalTime.of(19, 0), LocalTime.of(21, 30)
        };

        int movieIndex = 0;
        for (int day = 0; day < 7; day++) {
            LocalDate date = today.plusDays(day + 1);
            for (ScreeningRoom room : rooms) {
                for (int slot = 0; slot < 3; slot++) {
                    Movie movie = movies.get(movieIndex % movies.size());
                    LocalDateTime dateTime = LocalDateTime.of(date, times[slot % times.length]);

                    Show show = new Show();
                    show.setMovie(movie);
                    show.setScreeningRoom(room);
                    show.setDateTime(dateTime);
                    showRepository.save(show);

                    movieIndex++;
                }
            }
        }
    }
}
