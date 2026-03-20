import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';

import { HomeView } from 'Frontend/views/@index';

vi.mock('Frontend/lib/movies-client', () => ({
  fetchBrowseableMovies: vi.fn(),
}));

describe('HomeView', () => {
  it('renders browseable movies with poster, title, duration, and detail links', async () => {
    const { fetchBrowseableMovies } = await import('Frontend/lib/movies-client');
    vi.mocked(fetchBrowseableMovies).mockResolvedValue([
      {
        id: 7,
        title: 'AI Developer 2',
        durationMinutes: 126,
        posterUrl: '/api/posters/ai-developer-2.png',
        hasUpcomingShows: true,
      },
    ]);

    render(<HomeView />);

    await waitFor(() => expect(screen.getByRole('heading', { name: 'AI Developer 2' })).toBeInTheDocument());
    expect(screen.getByText('126 min')).toBeInTheDocument();
    expect(screen.getByAltText('AI Developer 2 poster')).toHaveAttribute('src', '/api/posters/ai-developer-2.png');
    expect(screen.getByRole('link', { name: /AI Developer 2 poster/i })).toHaveAttribute('href', '/movie/7');
  });

  it('shows an empty state when no browseable movies exist', async () => {
    const { fetchBrowseableMovies } = await import('Frontend/lib/movies-client');
    vi.mocked(fetchBrowseableMovies).mockResolvedValue([]);

    render(<HomeView />);

    await waitFor(() =>
      expect(screen.getByText('No movies with upcoming shows are available right now.')).toBeInTheDocument(),
    );
  });
});
