package com.example.specdriven.catalog.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
public class MovieAdminService {

    private static final long MAX_POSTER_BYTES = 2L * 1024L * 1024L;

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
    public List<MovieAdminRowDto> listMovies() {
        return movieRepository.findAllByOrderByTitleAsc().stream()
                .map(movie -> new MovieAdminRowDto(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDurationMinutes(),
                        showRepository.countByMovieId(movie.getId()),
                        movie.getPosterFileName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MovieAdminFormData getMovie(long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));
        return new MovieAdminFormData(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getPosterFileName());
    }

    @Transactional
    public MovieAdminRowDto saveMovie(MovieAdminFormData formData, PosterUpload posterUpload) {
        validate(formData);

        String posterFileName = formData.posterFileName();
        if (posterUpload != null) {
            posterFileName = storePoster(formData.title(), posterUpload);
        }

        Movie movie;
        if (formData.id() == null) {
            ensureUniqueTitle(formData.title(), null);
            movie = movieRepository.save(new Movie(
                    formData.title().trim(),
                    formData.description(),
                    formData.durationMinutes(),
                    posterFileName));
        } else {
            movie = movieRepository.findById(formData.id())
                    .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + formData.id()));
            ensureUniqueTitle(formData.title(), movie.getId());
            movie.update(
                    formData.title().trim(),
                    formData.description(),
                    formData.durationMinutes(),
                    posterFileName);
        }

        return new MovieAdminRowDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                showRepository.countByMovieId(movie.getId()),
                movie.getPosterFileName());
    }

    @Transactional
    public void deleteMovie(long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        if (ticketRepository.existsFutureTicketsForMovie(movieId, LocalDateTime.now(clock))) {
            throw new IllegalStateException("Movies with future sold tickets cannot be deleted.");
        }

        movieRepository.delete(movie);
    }

    private void validate(MovieAdminFormData formData) {
        if (formData.title() == null || formData.title().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (formData.durationMinutes() == null || formData.durationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number.");
        }
    }

    private void ensureUniqueTitle(String title, Long movieId) {
        boolean exists = movieId == null
                ? movieRepository.existsByTitleIgnoreCase(title.trim())
                : movieRepository.existsByTitleIgnoreCaseAndIdNot(title.trim(), movieId);

        if (exists) {
            throw new IllegalArgumentException("A movie with this title already exists.");
        }
    }

    private String storePoster(String title, PosterUpload posterUpload) {
        String originalFileName = posterUpload.originalFileName() == null ? "" : posterUpload.originalFileName();
        String extension = extensionOf(originalFileName);
        if (!extension.equals(".png") && !extension.equals(".jpg") && !extension.equals(".jpeg")) {
            throw new IllegalArgumentException("Poster upload must be a PNG or JPG image.");
        }
        if (posterUpload.content().length > MAX_POSTER_BYTES) {
            throw new IllegalArgumentException("Poster upload must be 2 MB or smaller.");
        }

        String baseName = title.trim().toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (baseName.isBlank()) {
            baseName = "movie-poster";
        }

        String fileName = baseName + "-" + System.currentTimeMillis() + extension;
        Path target = Path.of("posters").resolve(fileName);

        try {
            Files.createDirectories(target.getParent());
            Files.write(target, posterUpload.content());
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store poster image.", exception);
        }

        return fileName;
    }

    private String extensionOf(String fileName) {
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex < 0) {
            return "";
        }
        return fileName.substring(extensionIndex).toLowerCase(Locale.ENGLISH);
    }
}
