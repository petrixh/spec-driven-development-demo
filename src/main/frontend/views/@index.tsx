import { useEffect, useState } from 'react';

import { fetchBrowseableMovies, type MovieCard } from 'Frontend/lib/movies-client';

const eyebrowStyle = { color: '#ff6b72' } as const;
const badgeStyle = { backgroundColor: '#621015', color: '#ffd7d9' } as const;

export function HomeView() {
  const [movies, setMovies] = useState<MovieCard[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;

    fetchBrowseableMovies()
      .then((response) => {
        if (!cancelled) {
          setMovies(response);
        }
      })
      .catch(() => {
        if (!cancelled) {
          setError('We could not load the movie lineup right now.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <div className="app-shell">
      <main className="page">
        <section className="hero">
          <span className="eyebrow" style={eyebrowStyle}>Now Screening</span>
          <h1 style={{ color: 'var(--text-primary)' }}>Pick the next film before the lights go down.</h1>
          <p>
            Browse the current lineup, compare runtimes, and jump into the movie details page when
            something catches your eye.
          </p>
        </section>

        {loading ? <div className="loading-state">Loading movies…</div> : null}
        {error ? <div className="error-state">{error}</div> : null}
        {!loading && !error && movies.length === 0 ? (
          <div className="empty-state">No movies with upcoming shows are available right now.</div>
        ) : null}

        {!loading && !error && movies.length > 0 ? (
          <section aria-label="Movies now showing" className="movie-grid">
            {movies.map((movie) => (
              <a className="movie-card" href={`/movie/${movie.id}`} key={movie.id}>
                <img alt={`${movie.title} poster`} className="movie-poster" src={movie.posterUrl} />
                <div>
                  <h2 style={{ color: 'var(--text-primary)' }}>{movie.title}</h2>
                </div>
                <div className="movie-meta">
                  <span>{movie.durationMinutes} min</span>
                  {movie.hasUpcomingShows ? (
                    <span className="show-badge" style={badgeStyle}>
                      Upcoming shows
                    </span>
                  ) : null}
                </div>
              </a>
            ))}
          </section>
        ) : null}
      </main>
    </div>
  );
}

export default HomeView;
