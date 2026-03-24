import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { MovieEndpoint } from 'Frontend/generated/endpoints';
import type MovieSummary from 'Frontend/generated/com/example/specdriven/movie/MovieEndpoint/MovieSummary';

export default function BrowseMoviesView() {
    const [movies, setMovies] = useState<MovieSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        MovieEndpoint.getMoviesWithFutureShows().then((data) => {
            setMovies(data ?? []);
            setLoading(false);
        });
    }, []);

    if (loading) {
        return (
            <div className="browse-container">
                <h1 className="browse-title">CineMax</h1>
                <p className="browse-subtitle">Loading movies...</p>
            </div>
        );
    }

    return (
        <div className="browse-container">
            <h1 className="browse-title">CineMax</h1>
            <p className="browse-subtitle">Now Showing</p>
            <div className="movie-grid">
                {movies.map((movie) => (
                    <div
                        key={movie.id}
                        className="movie-card"
                        onClick={() => navigate(`/movie/${movie.id}`)}
                        role="link"
                        tabIndex={0}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') navigate(`/movie/${movie.id}`);
                        }}
                    >
                        {movie.posterFileName && (
                            <img
                                className="movie-poster"
                                src={`/api/posters/${movie.posterFileName}`}
                                alt={movie.title ?? ''}
                                loading="lazy"
                            />
                        )}
                        <div className="movie-card-info">
                            <h3 className="movie-card-title">{movie.title}</h3>
                            <span className="movie-card-duration">{movie.durationMinutes} min</span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export const config = {
    flowLayout: false,
    title: 'CineMax - Browse Movies',
};
