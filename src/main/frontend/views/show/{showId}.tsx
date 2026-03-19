import { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { ShowEndpoint } from 'Frontend/generated/endpoints';
import type ShowDetail from 'Frontend/generated/com/example/specdriven/show/ShowEndpoint/ShowDetail';

interface SeatKey {
  row: number;
  seat: number;
}

function seatKey(row: number, seat: number): string {
  return `${row}-${seat}`;
}

function rowLabel(row: number): string {
  return String.fromCharCode(64 + row); // 1=A, 2=B, etc.
}

function formatDateTime(dateTimeStr: string | undefined): string {
  if (!dateTimeStr) return '';
  const dt = new Date(dateTimeStr);
  return dt.toLocaleDateString('en-US', {
    weekday: 'long', month: 'long', day: 'numeric'
  }) + ' at ' + dt.toLocaleTimeString('en-US', {
    hour: 'numeric', minute: '2-digit'
  });
}

export default function ShowSeatSelectionView() {
  const { showId } = useParams<{ showId: string }>();
  const [show, setShow] = useState<ShowDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [soldSeats, setSoldSeats] = useState<Set<string>>(new Set());
  const [selectedSeats, setSelectedSeats] = useState<SeatKey[]>([]);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [purchasing, setPurchasing] = useState(false);
  const [confirmation, setConfirmation] = useState<{ message: string; tickets: SeatKey[] } | null>(null);

  useEffect(() => {
    if (showId) {
      loadShow(Number(showId));
    }
  }, [showId]);

  function loadShow(id: number) {
    setLoading(true);
    ShowEndpoint.getShowDetail(id).then((data) => {
      setShow(data ?? null);
      if (data?.soldSeats) {
        setSoldSeats(new Set(data.soldSeats.filter(s => s != null).map(s => seatKey(s!.row, s!.seat))));
      }
      setSelectedSeats([]);
      setLoading(false);
    });
  }

  function toggleSeat(row: number, seat: number) {
    const key = seatKey(row, seat);
    if (soldSeats.has(key)) return;

    setSelectedSeats(prev => {
      const existing = prev.findIndex(s => s.row === row && s.seat === seat);
      if (existing >= 0) {
        return prev.filter((_, i) => i !== existing);
      }
      if (prev.length >= 6) {
        setError('Maximum 6 seats per transaction');
        return prev;
      }
      setError('');
      return [...prev, { row, seat }];
    });
  }

  async function handlePurchase() {
    setError('');
    if (selectedSeats.length === 0) {
      setError('Please select at least one seat');
      return;
    }
    if (!name.trim()) {
      setError('Name is required');
      return;
    }
    if (!email.match(/^[^@]+@[^@]+\.[^@]+$/)) {
      setError('Valid email is required');
      return;
    }

    setPurchasing(true);
    const result = await ShowEndpoint.purchaseTickets({
      showId: Number(showId),
      seats: selectedSeats,
      customerName: name.trim(),
      customerEmail: email.trim()
    });

    if (result?.success) {
      setConfirmation({
        message: result.message ?? 'Purchase successful!',
        tickets: result.tickets?.filter(t => t != null).map(t => ({ row: t!.row, seat: t!.seat })) ?? []
      });
    } else {
      setError(result?.message ?? 'Purchase failed');
      if (showId) loadShow(Number(showId));
    }
    setPurchasing(false);
  }

  if (loading) {
    return <div className="movies-loading">Loading...</div>;
  }

  if (!show) {
    return <div className="movies-loading">Show not found</div>;
  }

  if (confirmation) {
    return (
      <div className="seat-selection">
        <div className="purchase-confirmation">
          <h2>Booking Confirmed!</h2>
          <p className="confirmation-message">{confirmation.message}</p>
          <div className="confirmation-details">
            <p><strong>{show.movieTitle}</strong></p>
            <p>{formatDateTime(show.dateTime)} &mdash; {show.roomName}</p>
            <p className="confirmation-seats">
              Seats: {confirmation.tickets.map(t => `${rowLabel(t.row)}${t.seat}`).join(', ')}
            </p>
          </div>
          <a href={`/movie/${showId}`} className="btn-back">Back to Movie</a>
        </div>
      </div>
    );
  }

  return (
    <div className="seat-selection">
      <div className="show-header">
        <h1>{show.movieTitle}</h1>
        <p className="show-info">{formatDateTime(show.dateTime)} &mdash; {show.roomName}</p>
      </div>

      <div className="seat-map-container">
        <div className="screen-indicator">Screen</div>
        <div className="seat-map">
          {Array.from({ length: show.rows }, (_, r) => r + 1).map(row => (
            <div key={row} className="seat-row">
              <span className="row-label">{rowLabel(row)}</span>
              {Array.from({ length: show.seatsPerRow }, (_, s) => s + 1).map(seat => {
                const key = seatKey(row, seat);
                const isSold = soldSeats.has(key);
                const isSelected = selectedSeats.some(s => s.row === row && s.seat === seat);
                let cls = 'seat';
                if (isSold) cls += ' seat-sold';
                else if (isSelected) cls += ' seat-selected';
                else cls += ' seat-available';

                return (
                  <button
                    key={key}
                    className={cls}
                    disabled={isSold}
                    onClick={() => toggleSeat(row, seat)}
                    title={`${rowLabel(row)}${seat}${isSold ? ' (Sold)' : ''}`}
                  >
                    {seat}
                  </button>
                );
              })}
              <span className="row-label">{rowLabel(row)}</span>
            </div>
          ))}
        </div>

        <div className="seat-legend">
          <span className="legend-item"><span className="seat seat-available legend-swatch"></span> Available</span>
          <span className="legend-item"><span className="seat seat-selected legend-swatch"></span> Selected</span>
          <span className="legend-item"><span className="seat seat-sold legend-swatch"></span> Sold</span>
        </div>
      </div>

      <div className="purchase-section">
        <p className="selected-count">
          {selectedSeats.length > 0
            ? `${selectedSeats.length} seat(s) selected: ${selectedSeats.map(s => `${rowLabel(s.row)}${s.seat}`).join(', ')}`
            : 'No seats selected'}
        </p>

        <div className="purchase-form">
          <input
            type="text"
            placeholder="Your name"
            value={name}
            onChange={e => setName(e.target.value)}
            className="form-input"
          />
          <input
            type="email"
            placeholder="Your email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            className="form-input"
          />
          <button
            className="btn-purchase"
            onClick={handlePurchase}
            disabled={purchasing || selectedSeats.length === 0}
          >
            {purchasing ? 'Processing...' : `Purchase ${selectedSeats.length} Ticket(s)`}
          </button>
        </div>

        {error && <p className="purchase-error">{error}</p>}
      </div>
    </div>
  );
}
