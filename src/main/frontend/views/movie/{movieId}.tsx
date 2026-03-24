import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router';
import { MovieEndpoint, ShowEndpoint } from 'Frontend/generated/endpoints';
import type MovieDetail from 'Frontend/generated/com/example/specdriven/movie/MovieEndpoint/MovieDetail';
import type ShowtimeGroup from 'Frontend/generated/com/example/specdriven/show/ShowEndpoint/ShowtimeGroup';

export default function MovieDetailView() {
    const { movieId } = useParams();
    const navigate = useNavigate();
    const [movie, setMovie] = useState<MovieDetail | null>(null);
    const [showtimes, setShowtimes] = useState<ShowtimeGroup[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const id = Number(movieId);
        if (isNaN(id)) return;

        Promise.all([
            MovieEndpoint.getMovieById(id),
            ShowEndpoint.getShowtimesForMovie(id),
        ]).then(([movieData, showtimeData]) => {
            setMovie(movieData ?? null);
            setShowtimes(showtimeData ?? []);
            setLoading(false);
        });
    }, [movieId]);

    if (loading) {
        return (
            <div className="movie-detail-container">
                <p>Loading...</p>
            </div>
        );
    }

    if (!movie) {
        return (
            <div className="movie-detail-container">
                <p>Movie not found.</p>
                <Link to="/" className="back-link">Back to Movies</Link>
            </div>
        );
    }

    function formatDate(dateStr: string): string {
        const date = new Date(dateStr + 'T00:00:00');
        return date.toLocaleDateString('en-US', {
            weekday: 'long',
            month: 'long',
            day: 'numeric',
        });
    }

    return (
        <div className="movie-detail-container">
            <Link to="/" className="back-link">&larr; Back to Movies</Link>

            <div className="movie-detail-header">
                {movie.posterFileName && (
                    <img
                        className="movie-detail-poster"
                        src={`/api/posters/${movie.posterFileName}`}
                        alt={movie.title ?? ''}
                    />
                )}
                <div className="movie-detail-info">
                    <h1>{movie.title}</h1>
                    <p className="duration">{movie.durationMinutes} minutes</p>
                    {movie.description && (
                        <p className="description">{movie.description}</p>
                    )}
                </div>
            </div>

            <div className="showtimes-section">
                <h2>Showtimes</h2>
                {showtimes.length === 0 ? (
                    <p style={{ color: 'var(--text-secondary)' }}>No upcoming showtimes available.</p>
                ) : (
                    showtimes.map((group) => (
                        <div key={group.date} className="showtime-date-group">
                            <h3>{formatDate(group.date ?? '')}</h3>
                            <div className="showtime-list">
                                {group.showtimes?.map((st) => (
                                    <button
                                        key={st.id}
                                        className={`showtime-btn ${st.soldOut ? 'sold-out' : ''}`}
                                        onClick={() => !st.soldOut && navigate(`/show/${st.id}`)}
                                        disabled={st.soldOut}
                                    >
                                        <span className="time">{st.time}</span>
                                        <span className="room">{st.roomName}</span>
                                        <span className="seats">
                                            {st.soldOut ? 'Sold Out' : `${st.availableSeats} seats`}
                                        </span>
                                    </button>
                                ))}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export const config = {
    route: ':movieId',
    flowLayout: false,
    title: 'Movie Detail',
};
