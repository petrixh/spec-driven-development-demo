import { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { MovieEndpoint } from 'Frontend/generated/endpoints';
import type MovieDetail from 'Frontend/generated/com/example/specdriven/movie/MovieEndpoint/MovieDetail';

export default function MovieDetailView() {
  const { movieId } = useParams<{ movieId: string }>();
  const [movie, setMovie] = useState<MovieDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (movieId) {
      MovieEndpoint.getMovieDetail(Number(movieId)).then((data) => {
        setMovie(data ?? null);
        setLoading(false);
      });
    }
  }, [movieId]);

  if (loading) {
    return <div className="movies-loading">Loading...</div>;
  }

  if (!movie) {
    return <div className="movies-loading">Movie not found</div>;
  }

  // Group shows by date
  const showsByDate = new Map<string, NonNullable<typeof movie.shows>[number][]>();
  movie.shows?.forEach((show) => {
    if (!show) return;
    const date = show.dateTime?.substring(0, 10) ?? '';
    if (!showsByDate.has(date)) {
      showsByDate.set(date, []);
    }
    showsByDate.get(date)!.push(show);
  });

  function formatDate(dateStr: string): string {
    const date = new Date(dateStr + 'T00:00:00');
    return date.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' });
  }

  function formatTime(dateTimeStr: string | undefined): string {
    if (!dateTimeStr) return '';
    const time = dateTimeStr.substring(11, 16);
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const hour = h % 12 || 12;
    return `${hour}:${m.toString().padStart(2, '0')} ${ampm}`;
  }

  return (
    <div className="movie-detail">
      <div className="movie-detail-header">
        <div className="movie-detail-poster">
          {movie.posterFileName ? (
            <img src={`/api/posters/${movie.posterFileName}`} alt={movie.title ?? ''} />
          ) : (
            <div className="movie-poster-placeholder">No Poster</div>
          )}
        </div>
        <div className="movie-detail-info">
          <h1>{movie.title}</h1>
          <span className="movie-detail-duration">{movie.durationMinutes} min</span>
          {movie.description && <p className="movie-detail-description">{movie.description}</p>}
        </div>
      </div>

      <div className="showtimes-section">
        <h2>Showtimes</h2>
        {showsByDate.size === 0 && <p className="no-showtimes">No upcoming showtimes</p>}
        {Array.from(showsByDate.entries()).map(([date, shows]) => (
          <div key={date} className="showtime-date-group">
            <h3 className="showtime-date">{formatDate(date)}</h3>
            <div className="showtime-list">
              {shows.filter((s): s is NonNullable<typeof s> => s != null).map((show) => {
                const isSoldOut = show.soldOut;
                const content = (
                  <>
                    <span className="showtime-time">{formatTime(show.dateTime)}</span>
                    <span className="showtime-room">{show.roomName}</span>
                    <span className={`showtime-seats ${isSoldOut ? 'sold-out' : ''}`}>
                      {isSoldOut ? 'Sold Out' : `${show.availableSeats} seats`}
                    </span>
                  </>
                );
                return isSoldOut ? (
                  <div key={show.id} className="showtime-item showtime-sold-out">
                    {content}
                  </div>
                ) : (
                  <a key={show.id} href={`/show/${show.id}`} className="showtime-item showtime-available">
                    {content}
                  </a>
                );
              })}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
