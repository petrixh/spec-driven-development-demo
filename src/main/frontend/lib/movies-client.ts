export type MovieCard = {
  id: number;
  title: string;
  durationMinutes: number;
  posterUrl: string;
  hasUpcomingShows: boolean;
};

export type MovieDetails = {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  posterUrl: string;
  showDates: ShowDateGroup[];
};

export type ShowTime = {
  showId: number;
  time: string;
  screeningRoomName: string;
  availableSeats: number;
  soldOut: boolean;
};

export type ShowDateGroup = {
  date: string;
  showtimes: ShowTime[];
};

export async function fetchBrowseableMovies(): Promise<MovieCard[]> {
  const { MovieBrowserEndpoint } = await import('Frontend/generated/endpoints');
  const movies =
    (await MovieBrowserEndpoint.listBrowseableMovies())?.filter(
      (movie): movie is NonNullable<typeof movie> => movie !== undefined,
    ) ?? [];
  return movies
    .map((movie) => ({
      id: movie.id,
      title: movie.title,
      durationMinutes: movie.durationMinutes,
      posterUrl: movie.posterUrl,
      hasUpcomingShows: movie.hasUpcomingShows,
    }));
}

export async function fetchMovieDetails(movieId: number): Promise<MovieDetails> {
  const { MovieBrowserEndpoint } = await import('Frontend/generated/endpoints');
  const movie = await MovieBrowserEndpoint.getMovieDetails(movieId);
  if (!movie) {
    throw new Error(`Movie ${movieId} was not found`);
  }
  return {
    id: movie.id,
    title: movie.title,
    description: movie.description,
    durationMinutes: movie.durationMinutes,
    posterUrl: movie.posterUrl,
    showDates: movie.showDates,
  };
}
