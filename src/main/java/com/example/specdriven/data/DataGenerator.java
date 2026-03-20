package com.example.specdriven.data;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.example.specdriven.room.ScreeningRoom;
import com.example.specdriven.room.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(MovieRepository movieRepository,
                                       ScreeningRoomRepository roomRepository,
                                       ShowRepository showRepository) {
        return args -> {
            if (movieRepository.count() > 0) {
                return;
            }

            Movie m1 = createMovie("AI Developer 2", "The future of coding.", 120, "ai-developer-2.png");
            Movie m2 = createMovie("The Gardening Incident", "A rose by any other name.", 95, "gardening-incident.png");
            Movie m3 = createMovie("Living in the Forest", "Back to nature.", 110, "living-in-the-forest.png");
            Movie m4 = createMovie("The Null Mistake", "A Java tragedy.", 105, "null-mistake-java.png");
            Movie m5 = createMovie("Pink Elephants", "A surreal journey.", 85, "pink-elephants.png");
            Movie m6 = createMovie("Reindeer Hunter", "The search continues.", 130, "reindeer-hunter.png");
            Movie m7 = createMovie("Sleeping with the Fishes", "An underwater thriller.", 115, "sleeping-with-the-fishes.png");
            Movie m8 = createMovie("Threading a Needle", "A delicate operation.", 100, "threading-a-needle.png");

            movieRepository.saveAll(List.of(m1, m2, m3, m4, m5, m6, m7, m8));

            ScreeningRoom r1 = createRoom("Room 1", 8, 10);
            ScreeningRoom r2 = createRoom("Room 2", 6, 8);
            ScreeningRoom r3 = createRoom("Room 3", 5, 6);

            roomRepository.saveAll(List.of(r1, r2, r3));

            // Create shows for the next 7 days
            LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
            for (int i = 0; i < 7; i++) {
                LocalDateTime day = now.plusDays(i);
                showRepository.save(createShow(day.withHour(14), m1, r1));
                showRepository.save(createShow(day.withHour(17), m2, r2));
                showRepository.save(createShow(day.withHour(20), m3, r3));
                showRepository.save(createShow(day.withHour(14), m4, r2));
                showRepository.save(createShow(day.withHour(17), m5, r3));
                showRepository.save(createShow(day.withHour(20), m6, r1));
                showRepository.save(createShow(day.withHour(14), m7, r3));
                showRepository.save(createShow(day.withHour(17), m8, r1));
            }
        };
    }

    private Movie createMovie(String title, String description, int duration, String poster) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setDescription(description);
        m.setDurationMinutes(duration);
        m.setPosterFileName(poster);
        return m;
    }

    private ScreeningRoom createRoom(String name, int rows, int seats) {
        ScreeningRoom r = new ScreeningRoom();
        r.setName(name);
        r.setRows(rows);
        r.setSeatsPerRow(seats);
        return r;
    }

    private Show createShow(LocalDateTime dateTime, Movie movie, ScreeningRoom room) {
        Show s = new Show();
        s.setDateTime(dateTime);
        s.setMovie(movie);
        s.setScreeningRoom(room);
        return s;
    }
}
