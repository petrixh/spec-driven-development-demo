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
      posterUrl: '/posters/gardening-incident.png',
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
  });
});
