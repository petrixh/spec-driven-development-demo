import { FormEvent, useEffect, useState } from 'react';
import { useParams } from 'react-router';

import {
  fetchShowSeatSelection,
  purchaseTickets,
  type Seat,
  type SelectedSeat,
  type ShowSeatSelection,
  type TicketConfirmation,
} from 'Frontend/lib/movies-client';

const MAX_SELECTION = 6;

function rowLabel(row: number) {
  return String.fromCharCode(64 + row);
}

function seatLabel(seat: SelectedSeat) {
  return `${rowLabel(seat.row)}${seat.number}`;
}

function formatShowDate(date: string) {
  return new Intl.DateTimeFormat('en-US', {
    weekday: 'long',
    month: 'short',
    day: 'numeric',
  }).format(new Date(`${date}T00:00:00`));
}

function isSeatSelected(selectedSeats: SelectedSeat[], seat: SelectedSeat) {
  return selectedSeats.some((selected) => selected.row === seat.row && selected.number === seat.number);
}

export default function ShowSeatSelectionView() {
  const { id } = useParams();
  const [show, setShow] = useState<ShowSeatSelection | null>(null);
  const [selectedSeats, setSelectedSeats] = useState<SelectedSeat[]>([]);
  const [customerName, setCustomerName] = useState('');
  const [customerEmail, setCustomerEmail] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [confirmation, setConfirmation] = useState<TicketConfirmation | null>(null);

  useEffect(() => {
    const showId = Number(id);
    if (!Number.isFinite(showId)) {
      setError('Show not found.');
      setLoading(false);
      return;
    }

    let cancelled = false;

    fetchShowSeatSelection(showId)
      .then((response) => {
        if (!cancelled) {
          setShow(response);
          setSelectedSeats([]);
          setConfirmation(null);
          setError('');
        }
      })
      .catch(() => {
        if (!cancelled) {
          setError('Show not found.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, [id]);

  async function refreshShow() {
    if (!show) {
      return;
    }
    const refreshedShow = await fetchShowSeatSelection(show.showId);
    setShow(refreshedShow);
    setSelectedSeats([]);
  }

  function toggleSeat(seat: SelectedSeat, sold: boolean) {
    if (sold || confirmation) {
      return;
    }

    setError('');
    setSelectedSeats((current) => {
      if (isSeatSelected(current, seat)) {
        return current.filter((selected) => !(selected.row === seat.row && selected.number === seat.number));
      }
      if (current.length >= MAX_SELECTION) {
        setError('You can select up to 6 seats per purchase.');
        return current;
      }
      return [...current, seat];
    });
  }

  async function handlePurchase(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!show) {
      return;
    }

    if (selectedSeats.length === 0) {
      setError('Select at least one seat before purchasing.');
      return;
    }

    if (!customerName.trim() || !customerEmail.trim()) {
      setError('Name and email are required.');
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(customerEmail.trim())) {
      setError('Enter a valid email address.');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      const result = await purchaseTickets({
        showId: show.showId,
        seats: selectedSeats,
        customerName,
        customerEmail,
      });
      setConfirmation(result);
      setSelectedSeats([]);
    } catch (purchaseError) {
      const message = purchaseError instanceof Error ? purchaseError.message : 'Purchase failed.';
      setError(message);
      if (message.includes('just sold')) {
        await refreshShow();
      }
    } finally {
      setSubmitting(false);
    }
  }

  function renderSeat(seat: Seat) {
    const selected = isSeatSelected(selectedSeats, seat);
    const classes = ['seat-button'];
    let status = 'available';

    if (seat.sold) {
      classes.push('sold');
      status = 'sold';
    } else if (selected) {
      classes.push('selected');
      status = 'selected';
    }

    return (
      <button
        aria-label={`Seat ${seatLabel(seat)} ${status}`}
        className={classes.join(' ')}
        disabled={seat.sold || submitting || confirmation !== null}
        key={seatLabel(seat)}
        type="button"
        onClick={() => toggleSeat({ row: seat.row, number: seat.number }, seat.sold)}
      >
        <span>{seatLabel(seat)}</span>
      </button>
    );
  }

  const soldSeatKeys = new Set(show?.soldSeats.map((seat) => `${seat.row}:${seat.number}`) ?? []);
  const seatRows =
    show == null
      ? []
      : Array.from({ length: show.rows }, (_, rowIndex) => ({
          row: rowIndex + 1,
          seats: Array.from({ length: show.seatsPerRow }, (_, seatIndex) => ({
            row: rowIndex + 1,
            number: seatIndex + 1,
            sold: soldSeatKeys.has(`${rowIndex + 1}:${seatIndex + 1}`),
          })),
        }));

  return (
    <div className="app-shell">
      <main className="page seat-page">
        {show ? (
          <a className="back-link" href={`/movie/${show.movieId}`}>
            Back to Movie
          </a>
        ) : null}

        {loading ? <div className="loading-state">Loading seats…</div> : null}
        {!loading && error && !show ? <div className="error-state">{error}</div> : null}

        {!loading && show ? (
          <>
            <section className="seat-hero">
              <span className="eyebrow">Seat Selection</span>
              <h1>{show.movieTitle}</h1>
              <p>
                {formatShowDate(show.showDate)} at {show.showTime} in {show.screeningRoomName}
              </p>
            </section>

            <section className="seat-layout">
              <div className="seat-map-panel">
                <div className="screen-indicator">
                  <span>Screen</span>
                </div>

                <div aria-label="Seat legend" className="seat-legend">
                  <div className="legend-item">
                    <span className="legend-swatch available" />
                    Available
                  </div>
                  <div className="legend-item">
                    <span className="legend-swatch selected" />
                    Selected
                  </div>
                  <div className="legend-item">
                    <span className="legend-swatch sold" />
                    Sold
                  </div>
                </div>

                <div aria-label="Seat map" className="seat-map">
                  {seatRows.map((seatRow) => (
                    <div className="seat-row" key={seatRow.row}>
                      <span className="seat-row-label">{rowLabel(seatRow.row)}</span>
                      <div className="seat-row-grid">{seatRow.seats.map(renderSeat)}</div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="purchase-panel">
                <section className="purchase-summary">
                  <h2>Selection Summary</h2>
                  <p>
                    {selectedSeats.length} seat{selectedSeats.length === 1 ? '' : 's'} selected
                  </p>
                  <div className="selected-seat-list">
                    {selectedSeats.length > 0 ? selectedSeats.map((seat) => <span key={seatLabel(seat)}>{seatLabel(seat)}</span>) : <span>Choose up to 6 seats.</span>}
                  </div>
                </section>

                <form className="purchase-form" onSubmit={handlePurchase}>
                  <label>
                    Name
                    <input
                      required
                      type="text"
                      value={customerName}
                      onChange={(event) => setCustomerName(event.target.value)}
                    />
                  </label>

                  <label>
                    Email
                    <input
                      required
                      type="email"
                      value={customerEmail}
                      onChange={(event) => setCustomerEmail(event.target.value)}
                    />
                  </label>

                  {error ? <div className="purchase-error">{error}</div> : null}

                  <button className="purchase-button" disabled={submitting || confirmation !== null} type="submit">
                    {submitting ? 'Purchasing…' : 'Purchase'}
                  </button>
                </form>

                {confirmation ? (
                  <section className="purchase-confirmation">
                    <h2>Purchase Confirmed</h2>
                    <p>
                      {confirmation.customerName}, your tickets for {confirmation.movieTitle} are reserved.
                    </p>
                    <p>
                      {formatShowDate(confirmation.showDate)} at {confirmation.showTime} in {confirmation.screeningRoomName}
                    </p>
                    <p>Seats: {confirmation.seats.join(', ')}</p>
                    <a className="back-link" href={`/movie/${confirmation.movieId}`}>
                      Back to Movie
                    </a>
                  </section>
                ) : null}
              </div>
            </section>
          </>
        ) : null}
      </main>
    </div>
  );
}
