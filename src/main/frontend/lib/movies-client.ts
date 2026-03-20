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

export type Seat = {
  row: number;
  number: number;
  sold: boolean;
};

export type ShowSeatSelection = {
  showId: number;
  movieId: number;
  movieTitle: string;
  screeningRoomName: string;
  showDate: string;
  showTime: string;
  rows: number;
  seatsPerRow: number;
  soldSeats: Seat[];
};

export type SelectedSeat = {
  row: number;
  number: number;
};

export type TicketConfirmation = {
  showId: number;
  movieId: number;
  movieTitle: string;
  screeningRoomName: string;
  showDate: string;
  showTime: string;
  customerName: string;
  customerEmail: string;
  seats: string[];
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

export async function fetchShowSeatSelection(showId: number): Promise<ShowSeatSelection> {
  const { ShowBookingEndpoint } = await import('Frontend/generated/endpoints');
  const show = await ShowBookingEndpoint.getShowSeatSelection(showId);
  if (!show) {
    throw new Error(`Show ${showId} was not found`);
  }
  return {
    showId: show.showId,
    movieId: show.movieId,
    movieTitle: show.movieTitle,
    screeningRoomName: show.screeningRoomName,
    showDate: show.showDate,
    showTime: show.showTime,
    rows: show.rows,
    seatsPerRow: show.seatsPerRow,
    soldSeats: show.soldSeats,
  };
}

export async function purchaseTickets(input: {
  showId: number;
  seats: SelectedSeat[];
  customerName: string;
  customerEmail: string;
}): Promise<TicketConfirmation> {
  const { ShowBookingEndpoint } = await import('Frontend/generated/endpoints');
  const confirmation = await ShowBookingEndpoint.purchaseTickets(input);
  return {
    showId: confirmation.showId,
    movieId: confirmation.movieId,
    movieTitle: confirmation.movieTitle,
    screeningRoomName: confirmation.screeningRoomName,
    showDate: confirmation.showDate,
    showTime: confirmation.showTime,
    customerName: confirmation.customerName,
    customerEmail: confirmation.customerEmail,
    seats: confirmation.seats,
  };
}
