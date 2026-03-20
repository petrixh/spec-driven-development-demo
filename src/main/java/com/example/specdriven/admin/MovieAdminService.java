package com.example.specdriven.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
public class MovieAdminService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public MovieAdminService(MovieRepository movieRepository, ShowRepository showRepository,
            TicketRepository ticketRepository, Clock clock) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long countScheduledShows(Movie movie) {
        return showRepository.existsByMovieIdAndDateTimeAfter(movie.getId(), LocalDateTime.now(clock)) ? 1 : 0;
    }

    @Transactional(readOnly = true)
    public int countFutureShows(Movie movie) {
        return (int) movie.getShows().stream()
                .filter(s -> s.getDateTime().isAfter(LocalDateTime.now(clock)))
                .count();
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Movie movie) {
        if (ticketRepository.existsFutureSoldTicketsByMovieId(movie.getId())) {
            throw new IllegalStateException(
                    "Cannot delete movie with future shows that have sold tickets.");
        }
        movieRepository.delete(movie);
    }

    public String savePoster(String originalFilename, byte[] bytes) throws IOException {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Filename is required.");
        }
        String lower = originalFilename.toLowerCase();
        if (!lower.endsWith(".png") && !lower.endsWith(".jpg") && !lower.endsWith(".jpeg")) {
            throw new IllegalArgumentException("Only PNG and JPG posters are accepted.");
        }
        if (bytes.length > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Poster must be smaller than 2 MB.");
        }
        Path postersDir = Path.of("posters").toAbsolutePath();
        Files.createDirectories(postersDir);
        Path target = postersDir.resolve(originalFilename);
        Files.copy(new java.io.ByteArrayInputStream(bytes), target, StandardCopyOption.REPLACE_EXISTING);
        return originalFilename;
    }
}
