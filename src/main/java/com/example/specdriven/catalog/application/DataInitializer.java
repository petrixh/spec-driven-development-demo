package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ScreeningRoom;
import com.example.specdriven.catalog.domain.ScreeningRoomRepository;
import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.Ticket;
import com.example.specdriven.catalog.domain.TicketRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ScreeningRoomRepository screeningRoomRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public DataInitializer(
            MovieRepository movieRepository,
            ScreeningRoomRepository screeningRoomRepository,
            ShowRepository showRepository,
            TicketRepository ticketRepository,
            Clock clock) {
        this.movieRepository = movieRepository;
        this.screeningRoomRepository = screeningRoomRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> movies = movieRepository.saveAll(List.of(
                new Movie("AI Developer 2",
                        "A burnt-out engineer is dragged back for one last impossible release.", 126,
                        "ai-developer-2.png"),
                new Movie("Living in the Forest",
                        "A quiet survival story about rebuilding life far from the city.", 98,
                        "living-in-the-forest.png"),
                new Movie("Pink Elephants",
                        "A glossy comedy where every bad idea somehow gets a bigger budget.", 104,
                        "pink-elephants.png"),
                new Movie("Reindeer Hunter",
                        "A snowbound thriller chasing a rumor through the Arctic night.", 112,
                        "reindeer-hunter.png"),
                new Movie("Sleeping with the Fishes",
                        "Family loyalties get messy when a coastal diner hides secrets.", 117,
                        "sleeping-with-the-fishes.png"),
                new Movie("The Gardening Incident",
                        "A suburban mystery sparked by one very suspicious flowerbed.", 109,
                        "gardening-incident.png"),
                new Movie("The Null Mistake",
                        "A developer drama about one unchecked assumption and the fallout after it.", 101,
                        "null-mistake-java.png"),
                new Movie("Threading a Needle",
                        "A small heist film built on timing, trust, and sharp suits.", 95,
                        "threading-a-needle.png")));

        List<ScreeningRoom> rooms = screeningRoomRepository.saveAll(List.of(
                new ScreeningRoom("Room 1", 8, 10),
                new ScreeningRoom("Room 2", 6, 8),
                new ScreeningRoom("Room 3", 5, 6)));

        LocalDateTime baseTime = LocalDateTime.now(clock).withMinute(0).withSecond(0).withNano(0).plusHours(2);
        for (int index = 0; index < movies.size(); index++) {
            Movie movie = movies.get(index);
            ScreeningRoom room = rooms.get(index % rooms.size());
            Show firstShow = showRepository.save(
                    new Show(baseTime.plusDays(index % 3L).plusHours(index * 2L), movie, room));
            showRepository.save(new Show(
                    baseTime.plusDays((index % 3L) + 1).plusHours(index * 2L + 3),
                    movie,
                    rooms.get((index + 1) % rooms.size())));

            if (index == 0) {
                seedSoldOutTickets(firstShow, room, baseTime.minusHours(1));
            }
        }
    }

    private void seedSoldOutTickets(Show show, ScreeningRoom room, LocalDateTime purchasedAt) {
        for (int row = 1; row <= room.getRows(); row++) {
            for (int seat = 1; seat <= room.getSeatsPerRow(); seat++) {
                ticketRepository.save(new Ticket(
                        row,
                        seat,
                        "Sold Seat " + row + "-" + seat,
                        "sold+" + row + "-" + seat + "@example.com",
                        purchasedAt,
                        show));
            }
        }
    }
}
