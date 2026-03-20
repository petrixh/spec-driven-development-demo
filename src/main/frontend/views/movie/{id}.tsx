import { useEffect, useState } from 'react';
import { useParams } from 'react-router';

import { fetchMovieDetails, type MovieDetails } from 'Frontend/lib/movies-client';

const eyebrowStyle = { color: '#ff6b72' } as const;
const badgeStyle = { backgroundColor: '#621015', color: '#ffd7d9' } as const;

export function MovieDetailView() {
  const { id } = useParams();
  const [movie, setMovie] = useState<MovieDetails | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const movieId = Number(id);
    if (!Number.isFinite(movieId)) {
      setError('Movie not found.');
      setLoading(false);
      return;
    }

    let cancelled = false;

    fetchMovieDetails(movieId)
      .then((response) => {
        if (!cancelled) {
          setMovie(response);
        }
      })
      .catch(() => {
        if (!cancelled) {
          setError('Movie not found.');
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
  }, [id]);

  return (
    <main className="page">
      <a className="back-link" href="/">
        Back to all movies
      </a>

      {loading ? <div className="loading-state">Loading movie details…</div> : null}
      {error ? <div className="error-state">{error}</div> : null}

      {!loading && !error && movie ? (
        <section className="detail-layout">
          <img alt={`${movie.title} poster`} className="detail-poster" src={movie.posterUrl} />
          <div className="detail-copy">
            <span className="eyebrow" style={eyebrowStyle}>Movie Details</span>
            <h1 style={{ color: 'var(--text-primary)' }}>{movie.title}</h1>
            <div className="show-badge" style={badgeStyle}>{movie.durationMinutes} min</div>
            <p>{movie.description}</p>
          </div>
        </section>
      ) : null}
    </main>
  );
}

export default MovieDetailView;
