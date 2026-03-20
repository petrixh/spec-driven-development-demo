import { useEffect, useState } from 'react';
import type Movie from 'Frontend/generated/com/example/specdriven/movie/Movie';
import { MovieEndpoint } from 'Frontend/generated/endpoints';

export default function BrowseMoviesView() {
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    MovieEndpoint.getMovies().then(setMovies);
  }, []);

  return (
    <div className="max-width-container">
      <h1 className="text-3xl font-bold mb-6">Currently Showing</h1>
      <div className="movie-grid">
        {movies.map((movie) => (
          <a key={movie.id} href={`/movie/${movie.id}`} className="movie-card">
            <img
              src={movie.posterFileName ? `/${movie.posterFileName}` : '/placeholder.png'}
              alt={movie.title}
              className="movie-poster"
            />
            <div className="movie-info">
              <h2 className="movie-title">{movie.title}</h2>
              <p className="movie-duration">{movie.durationMinutes} min</p>
            </div>
          </a>
        ))}
      </div>
    </div>
  );
}
