import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router';
import { vi } from 'vitest';

import { MovieDetailView } from 'Frontend/views/movie/{id}';

vi.mock('Frontend/lib/movies-client', () => ({
  fetchMovieDetails: vi.fn(),
}));

describe('MovieDetailView', () => {
  it('loads the selected movie from the route parameter', async () => {
    const { fetchMovieDetails } = await import('Frontend/lib/movies-client');
    vi.mocked(fetchMovieDetails).mockResolvedValue({
      id: 3,
      title: 'The Gardening Incident',
      description: 'A suburban mystery sparked by one very suspicious flowerbed.',
      durationMinutes: 109,
      posterUrl: '/api/posters/gardening-incident.png',
      showDates: [
        {
          date: '2026-03-21',
          showtimes: [
            {
              showId: 11,
              time: '18:30',
              screeningRoomName: 'Room 1',
              availableSeats: 12,
              soldOut: false,
            },
            {
              showId: 12,
              time: '21:00',
              screeningRoomName: 'Room 2',
              availableSeats: 0,
              soldOut: true,
            },
          ],
        },
      ],
    });

    render(
      <MemoryRouter initialEntries={['/movie/3']}>
        <Routes>
          <Route element={<MovieDetailView />} path="/movie/:id" />
        </Routes>
      </MemoryRouter>,
    );

    await waitFor(() =>
      expect(screen.getByRole('heading', { name: 'The Gardening Incident' })).toBeInTheDocument(),
    );
    expect(fetchMovieDetails).toHaveBeenCalledWith(3);
    expect(screen.getByText('109 min')).toBeInTheDocument();
    expect(screen.getByText('Saturday, Mar 21')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /18:30/i })).toHaveAttribute('href', '/show/11');
    expect(screen.getByText('Sold out')).toBeInTheDocument();
  });
});
