import { useEffect, useState } from 'react';
import { useParams } from 'react-router';

import { fetchMovieDetails, type MovieDetails, type ShowDateGroup, type ShowTime } from 'Frontend/lib/movies-client';

const eyebrowStyle = { color: '#ff6b72' } as const;
const badgeStyle = { backgroundColor: '#621015', color: '#ffd7d9' } as const;

function formatShowDate(date: string) {
  return new Intl.DateTimeFormat('en-US', {
    weekday: 'long',
    month: 'short',
    day: 'numeric',
  }).format(new Date(`${date}T00:00:00`));
}

function ShowtimeRow({ showtime }: { showtime: ShowTime }) {
  const content = (
    <>
      <div className="showtime-primary">
        <strong>{showtime.time}</strong>
        <span>{showtime.screeningRoomName}</span>
      </div>
      <div className="showtime-secondary">
        <span>{showtime.availableSeats} seats available</span>
        {showtime.soldOut ? (
          <span className="showtime-status sold-out">Sold out</span>
        ) : (
          <span className="showtime-status">Select seats</span>
        )}
      </div>
    </>
  );

  if (showtime.soldOut) {
    return (
      <div aria-disabled="true" className="showtime-card sold-out">
        {content}
      </div>
    );
  }

  return (
    <a className="showtime-card" href={`/show/${showtime.showId}`}>
      {content}
    </a>
  );
}

function ShowtimeGroup({ group }: { group: ShowDateGroup }) {
  return (
    <section className="showtime-group">
      <h2>{formatShowDate(group.date)}</h2>
      <div className="showtime-list">
        {group.showtimes.map((showtime) => (
          <ShowtimeRow key={showtime.showId} showtime={showtime} />
        ))}
      </div>
    </section>
  );
}

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
    <div className="app-shell">
      <main className="page">
        <a className="back-link" href="/">
          Back to all movies
        </a>

        {loading ? <div className="loading-state">Loading movie details…</div> : null}
        {error ? <div className="error-state">{error}</div> : null}

        {!loading && !error && movie ? (
          <>
            <section className="detail-layout">
              <img alt={`${movie.title} poster`} className="detail-poster" src={movie.posterUrl} />
              <div className="detail-copy">
                <span className="eyebrow" style={eyebrowStyle}>Movie Details</span>
                <h1 style={{ color: 'var(--text-primary)' }}>{movie.title}</h1>
                <div className="show-badge" style={badgeStyle}>{movie.durationMinutes} min</div>
                <p>{movie.description}</p>
              </div>
            </section>

            <section className="showtime-section">
              <span className="eyebrow" style={eyebrowStyle}>Upcoming Showtimes</span>
              {movie.showDates.length > 0 ? (
                movie.showDates.map((group) => <ShowtimeGroup group={group} key={group.date} />)
              ) : (
                <div className="empty-state">No future showtimes are currently scheduled for this movie.</div>
              )}
            </section>
          </>
        ) : null}
      </main>
    </div>
  );
}

export default MovieDetailView;
