import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router';
import { vi } from 'vitest';

import ShowSeatSelectionView from 'Frontend/views/show/{id}';

vi.mock('Frontend/lib/movies-client', () => ({
  fetchShowSeatSelection: vi.fn(),
  purchaseTickets: vi.fn(),
}));

describe('ShowSeatSelectionView', () => {
  it('renders sold seats, allows selecting seats, and shows confirmation after purchase', async () => {
    const { fetchShowSeatSelection, purchaseTickets } = await import('Frontend/lib/movies-client');

    vi.mocked(fetchShowSeatSelection).mockResolvedValue({
      showId: 2,
      movieId: 1,
      movieTitle: 'AI Developer 2',
      screeningRoomName: 'Room 2',
      showDate: '2026-03-21',
      showTime: '16:00',
      rows: 2,
      seatsPerRow: 3,
      soldSeats: [{ row: 1, number: 2, sold: true }],
    });
    vi.mocked(purchaseTickets).mockResolvedValue({
      showId: 2,
      movieId: 1,
      movieTitle: 'AI Developer 2',
      screeningRoomName: 'Room 2',
      showDate: '2026-03-21',
      showTime: '16:00',
      customerName: 'Morgan',
      customerEmail: 'morgan@example.com',
      seats: ['A1'],
    });

    render(
      <MemoryRouter initialEntries={['/show/2']}>
        <Routes>
          <Route element={<ShowSeatSelectionView />} path="/show/:id" />
        </Routes>
      </MemoryRouter>,
    );

    await waitFor(() => expect(screen.getByRole('heading', { name: 'AI Developer 2' })).toBeInTheDocument());

    expect(screen.getByRole('button', { name: 'Seat A2 sold' })).toBeDisabled();

    fireEvent.click(screen.getByRole('button', { name: 'Seat A1 available' }));
    expect(screen.getByText('1 seat selected')).toBeInTheDocument();

    fireEvent.change(screen.getByLabelText('Name'), { target: { value: 'Morgan' } });
    fireEvent.change(screen.getByLabelText('Email'), { target: { value: 'morgan@example.com' } });
    fireEvent.click(screen.getByRole('button', { name: 'Purchase' }));

    await waitFor(() => expect(purchaseTickets).toHaveBeenCalledWith({
      showId: 2,
      seats: [{ row: 1, number: 1 }],
      customerName: 'Morgan',
      customerEmail: 'morgan@example.com',
    }));

    expect(screen.getByRole('heading', { name: 'Purchase Confirmed' })).toBeInTheDocument();
    expect(screen.getByText('Seats: A1')).toBeInTheDocument();
    expect(screen.getAllByRole('link', { name: 'Back to Movie' }).at(-1)).toHaveAttribute('href', '/movie/1');
  });

  it('prevents selecting more than 6 seats', async () => {
    const { fetchShowSeatSelection } = await import('Frontend/lib/movies-client');

    vi.mocked(fetchShowSeatSelection).mockResolvedValue({
      showId: 5,
      movieId: 1,
      movieTitle: 'AI Developer 2',
      screeningRoomName: 'Room 1',
      showDate: '2026-03-21',
      showTime: '20:30',
      rows: 1,
      seatsPerRow: 7,
      soldSeats: [],
    });

    render(
      <MemoryRouter initialEntries={['/show/5']}>
        <Routes>
          <Route element={<ShowSeatSelectionView />} path="/show/:id" />
        </Routes>
      </MemoryRouter>,
    );

    await waitFor(() => expect(screen.getByRole('button', { name: 'Seat A1 available' })).toBeInTheDocument());

    ['A1', 'A2', 'A3', 'A4', 'A5', 'A6', 'A7'].forEach((seatLabel) => {
      fireEvent.click(screen.getByRole('button', { name: `Seat ${seatLabel} available` }));
    });

    expect(screen.getByText('You can select up to 6 seats per purchase.')).toBeInTheDocument();
    expect(screen.getByText('6 seats selected')).toBeInTheDocument();
  });
});
