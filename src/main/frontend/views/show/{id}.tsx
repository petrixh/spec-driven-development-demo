import { useEffect, useState } from 'react';
import { useParams } from 'react-router';

type ShowDetails = {
  showId: number;
  movieTitle: string;
  movieId: number;
  dateTime: string;
  screeningRoomName: string;
  rows: number;
  seatsPerRow: number;
  soldSeats: { row: number; seatNumber: number }[];
};

type TicketDto = {
  id: number;
  row: number;
  seatNumber: number;
  movieTitle: string;
  showDateTime: string;
  screeningRoomName: string;
};

type SelectedSeat = { row: number; seatNumber: number };

function seatKey(row: number, seatNumber: number) {
  return `${row}-${seatNumber}`;
}

function rowLabel(row: number) {
  return String.fromCharCode(64 + row);
}

export default function ShowSeatSelectionView() {
  const { id } = useParams();
  const [show, setShow] = useState<ShowDetails | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selected, setSelected] = useState<SelectedSeat[]>([]);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [nameError, setNameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [purchaseError, setPurchaseError] = useState('');
  const [purchasing, setPurchasing] = useState(false);
  const [confirmed, setConfirmed] = useState<TicketDto[]>([]);

  async function loadShow() {
    const showId = Number(id);
    if (!Number.isFinite(showId)) {
      setError('Show not found.');
      setLoading(false);
      return;
    }
    try {
      const { TicketPurchaseEndpoint } = await import('Frontend/generated/endpoints');
      const details = await TicketPurchaseEndpoint.getShowDetails(showId);
      if (!details) throw new Error('not found');
      setShow(details as ShowDetails);
    } catch {
      setError('Show not found.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { loadShow(); }, [id]);

  function isSold(row: number, seatNumber: number) {
    return show?.soldSeats.some((s) => s.row === row && s.seatNumber === seatNumber) ?? false;
  }

  function isSelected(row: number, seatNumber: number) {
    return selected.some((s) => s.row === row && s.seatNumber === seatNumber);
  }

  function toggleSeat(row: number, seatNumber: number) {
    if (isSold(row, seatNumber)) return;
    if (isSelected(row, seatNumber)) {
      setSelected((prev) => prev.filter((s) => !(s.row === row && s.seatNumber === seatNumber)));
    } else {
      if (selected.length >= 6) return;
      setSelected((prev) => [...prev, { row, seatNumber }]);
    }
  }

  async function handlePurchase() {
    let valid = true;
    setNameError('');
    setEmailError('');
    setPurchaseError('');
    if (!name.trim()) { setNameError('Name is required.'); valid = false; }
    if (!/^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
      setEmailError('Enter a valid email address.'); valid = false;
    }
    if (!valid || selected.length === 0) return;

    setPurchasing(true);
    try {
      const { TicketPurchaseEndpoint } = await import('Frontend/generated/endpoints');
      const result = await TicketPurchaseEndpoint.purchaseTickets(Number(id), {
        seats: selected,
        customerName: name,
        customerEmail: email,
      });
      if (!result) throw new Error('No response');
      if (!result.success) {
        setPurchaseError(result.errorMessage ?? 'Purchase failed.');
        if (result.errorMessage?.includes('just taken')) {
          setSelected([]);
          await loadShow();
        }
      } else {
        setConfirmed((result.tickets ?? []).filter((t): t is NonNullable<typeof t> => t !== null));
      }
    } catch {
      setPurchaseError('Purchase failed. Please try again.');
    } finally {
      setPurchasing(false);
    }
  }

  if (loading) return <main className="seat-page"><div className="loading-state">Loading show…</div></main>;
  if (error) return <main className="seat-page"><div className="error-state">{error}</div></main>;
  if (!show) return null;

  if (confirmed.length > 0) {
    return (
      <main className="seat-page">
        <div className="confirmation">
          <h2>🎟 Booking Confirmed!</h2>
          <p>
            <strong>{confirmed[0].movieTitle}</strong>
            {' — '}{confirmed[0].showDateTime}
          </p>
          <p>{confirmed[0].screeningRoomName}</p>
          <p>
            Seats:{' '}
            {confirmed.map((t) => `${rowLabel(t.row)}${t.seatNumber}`).join(', ')}
          </p>
          <p>Enjoy the show, <strong>{name}</strong>! Confirmation sent to {email}.</p>
          <a className="back-btn" href={`/movie/${show.movieId}`}>← Back to Movie</a>
        </div>
      </main>
    );
  }

  return (
    <main className="seat-page">
      <a className="back-link" href={`/movie/${show.movieId}`}>← Back to {show.movieTitle}</a>
      <div style={{ marginTop: 20 }}>
        <span className="eyebrow" style={{ color: '#ff6b72' }}>Seat Selection</span>
        <h1 style={{ color: 'var(--text-primary)', margin: '8px 0' }}>{show.movieTitle}</h1>
        <p style={{ color: 'var(--text-secondary)', margin: 0 }}>
          {show.dateTime} · {show.screeningRoomName}
        </p>
      </div>

      <div className="seat-map-container">
        <div className="screen-indicator">
          <span className="screen-bar">Screen</span>
        </div>

        <div className="seat-grid">
          {Array.from({ length: show.rows }, (_, rowIdx) => {
            const row = rowIdx + 1;
            return (
              <div className="seat-row" key={row}>
                <span className="seat-row-label">{rowLabel(row)}</span>
                {Array.from({ length: show.seatsPerRow }, (_, seatIdx) => {
                  const seatNumber = seatIdx + 1;
                  const sold = isSold(row, seatNumber);
                  const sel = isSelected(row, seatNumber);
                  const cls = `seat-btn ${sold ? 'sold' : sel ? 'selected' : 'available'}`;
                  return (
                    <button
                      aria-label={`Row ${rowLabel(row)} Seat ${seatNumber}${sold ? ' (sold)' : sel ? ' (selected)' : ''}`}
                      className={cls}
                      disabled={sold}
                      key={seatKey(row, seatNumber)}
                      onClick={() => toggleSeat(row, seatNumber)}
                      title={`${rowLabel(row)}${seatNumber}`}
                      type="button"
                    >
                      {seatNumber}
                    </button>
                  );
                })}
              </div>
            );
          })}
        </div>

        <div className="seat-legend">
          <div className="legend-item"><div className="legend-swatch available" /><span>Available</span></div>
          <div className="legend-item"><div className="legend-swatch selected" /><span>Selected</span></div>
          <div className="legend-item"><div className="legend-swatch sold" /><span>Sold</span></div>
        </div>

        {purchaseError && <div className="error-banner">{purchaseError}</div>}

        <div className="purchase-form">
          <h2>Purchase Tickets</h2>
          <p className="purchase-summary">
            {selected.length === 0
              ? 'No seats selected'
              : <>Selected: <strong>{selected.map((s) => `${rowLabel(s.row)}${s.seatNumber}`).join(', ')}</strong> ({selected.length} ticket{selected.length > 1 ? 's' : ''})</>}
          </p>
          <div className="form-field">
            <label htmlFor="customer-name">Name</label>
            <input
              id="customer-name"
              onChange={(e) => setName(e.target.value)}
              placeholder="Your full name"
              type="text"
              value={name}
            />
            {nameError && <span className="field-error">{nameError}</span>}
          </div>
          <div className="form-field">
            <label htmlFor="customer-email">Email</label>
            <input
              id="customer-email"
              onChange={(e) => setEmail(e.target.value)}
              placeholder="your@email.com"
              type="email"
              value={email}
            />
            {emailError && <span className="field-error">{emailError}</span>}
          </div>
          <button
            className="purchase-btn"
            disabled={selected.length === 0 || purchasing}
            onClick={handlePurchase}
            type="button"
          >
            {purchasing ? 'Processing…' : `Purchase ${selected.length > 0 ? selected.length + ' ticket' + (selected.length > 1 ? 's' : '') : ''}`}
          </button>
        </div>
      </div>
    </main>
  );
}
