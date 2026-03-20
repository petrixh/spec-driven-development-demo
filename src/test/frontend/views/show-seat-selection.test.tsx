import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router';
import { vi } from 'vitest';

vi.mock('Frontend/generated/endpoints', () => ({
  TicketPurchaseEndpoint: {
    getShowDetails: vi.fn(),
    purchaseTickets: vi.fn(),
  },
}));

import ShowSeatSelectionView from 'Frontend/views/show/{id}';

const mockShow = {
  showId: 5,
  movieTitle: 'AI Developer 2',
  movieId: 1,
  dateTime: 'Friday, Mar 21 at 18:00',
  screeningRoomName: 'Room 1',
  rows: 2,
  seatsPerRow: 3,
  soldSeats: [{ row: 1, seatNumber: 2 }],
};

function renderView(showId = '5') {
  return render(
    <MemoryRouter initialEntries={[`/show/${showId}`]}>
      <Routes>
        <Route element={<ShowSeatSelectionView />} path="/show/:id" />
      </Routes>
    </MemoryRouter>,
  );
}

describe('ShowSeatSelectionView', () => {
  beforeEach(() => {
    const { TicketPurchaseEndpoint } = require('Frontend/generated/endpoints');
    vi.mocked(TicketPurchaseEndpoint.getShowDetails).mockResolvedValue(mockShow);
    vi.mocked(TicketPurchaseEndpoint.purchaseTickets).mockResolvedValue({
      success: true,
      errorMessage: null,
      tickets: [
        { id: 1, row: 1, seatNumber: 1, movieTitle: 'AI Developer 2',
          showDateTime: 'Friday, Mar 21 at 18:00', screeningRoomName: 'Room 1' },
      ],
    });
  });

  it('renders the seat map with movie and show info', async () => {
    renderView();
    await waitFor(() => expect(screen.getByText('AI Developer 2')).toBeInTheDocument());
    expect(screen.getByText(/Friday, Mar 21 at 18:00/)).toBeInTheDocument();
    expect(screen.getByText('Screen')).toBeInTheDocument();
  });

  it('shows sold seats as disabled buttons', async () => {
    renderView();
    await waitFor(() => screen.getByText('AI Developer 2'));
    const soldBtn = screen.getByRole('button', { name: /Row A Seat 2.*sold/i });
    expect(soldBtn).toBeDisabled();
  });

  it('lets the user select and deselect available seats', async () => {
    renderView();
    await waitFor(() => screen.getByText('AI Developer 2'));
    const seat = screen.getByRole('button', { name: /Row A Seat 1$/i });
    fireEvent.click(seat);
    expect(seat).toHaveClass('selected');
    fireEvent.click(seat);
    expect(seat).toHaveClass('available');
  });

  it('completes a purchase and shows confirmation', async () => {
    renderView();
    await waitFor(() => screen.getByText('AI Developer 2'));
    fireEvent.click(screen.getByRole('button', { name: /Row A Seat 1$/i }));
    fireEvent.change(screen.getByLabelText('Name'), { target: { value: 'Alice' } });
    fireEvent.change(screen.getByLabelText('Email'), { target: { value: 'alice@example.com' } });
    fireEvent.click(screen.getByRole('button', { name: /Purchase/i }));
    await waitFor(() => expect(screen.getByText(/Booking Confirmed/i)).toBeInTheDocument());
    expect(screen.getByRole('link', { name: /Back to Movie/i })).toHaveAttribute('href', '/movie/1');
  });
});
