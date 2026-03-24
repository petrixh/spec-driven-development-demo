package com.example.specdriven.movie;

import com.example.specdriven.show.ShowRepository;
import com.example.specdriven.ticket.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public MovieService(MovieRepository movieRepository,
                        ShowRepository showRepository,
                        TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<Movie> findMoviesWithFutureShows() {
        return movieRepository.findMoviesWithFutureShows(LocalDateTime.now());
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Transactional
    public Movie save(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (movie.getDurationMinutes() == null || movie.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number");
        }
        if (movie.getId() == null) {
            if (movieRepository.existsByTitle(movie.getTitle())) {
                throw new IllegalArgumentException("A movie with this title already exists");
            }
        } else {
            if (movieRepository.existsByTitleAndIdNot(movie.getTitle(), movie.getId())) {
                throw new IllegalArgumentException("A movie with this title already exists");
            }
        }
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Movie movie) {
        var futureShows = showRepository.findFutureShowsByMovieId(movie.getId(), LocalDateTime.now());
        for (var show : futureShows) {
            if (ticketRepository.countByShowId(show.getId()) > 0) {
                throw new IllegalStateException(
                        "Cannot delete movie with future shows that have sold tickets");
            }
        }
        movieRepository.delete(movie);
    }

    public long getShowCount(Movie movie) {
        return showRepository.countByMovieId(movie.getId());
    }

    public void savePoster(String fileName, byte[] data) throws IOException {
        Path postersDir = Path.of("posters");
        Files.createDirectories(postersDir);
        Files.write(postersDir.resolve(fileName), data);
    }
}
