import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router';
import { ShowEndpoint, TicketEndpoint } from 'Frontend/generated/endpoints';
import type ShowDetail from 'Frontend/generated/com/example/specdriven/show/ShowEndpoint/ShowDetail';
import type SeatStatus from 'Frontend/generated/com/example/specdriven/ticket/TicketEndpoint/SeatStatus';
import type PurchaseResult from 'Frontend/generated/com/example/specdriven/ticket/TicketEndpoint/PurchaseResult';

interface SelectedSeat {
    row: number;
    seat: number;
}

export default function SeatSelectionView() {
    const { showId } = useParams();
    const [showDetail, setShowDetail] = useState<ShowDetail | null>(null);
    const [seats, setSeats] = useState<SeatStatus[]>([]);
    const [selected, setSelected] = useState<SelectedSeat[]>([]);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [result, setResult] = useState<PurchaseResult | null>(null);
    const [loading, setLoading] = useState(true);
    const [purchasing, setPurchasing] = useState(false);

    useEffect(() => {
        loadData();
    }, [showId]);

    function loadData() {
        const id = Number(showId);
        if (isNaN(id)) return;

        Promise.all([
            ShowEndpoint.getShowDetail(id),
            TicketEndpoint.getSeatsForShow(id),
        ]).then(([detail, seatData]) => {
            setShowDetail(detail ?? null);
            setSeats(seatData ?? []);
            setLoading(false);
        });
    }

    function toggleSeat(row: number, seat: number) {
        const idx = selected.findIndex((s) => s.row === row && s.seat === seat);
        if (idx >= 0) {
            setSelected(selected.filter((_, i) => i !== idx));
        } else if (selected.length < 6) {
            setSelected([...selected, { row, seat }]);
        }
        setError('');
    }

    function rowLabel(row: number): string {
        return String.fromCharCode('A'.charCodeAt(0) + row - 1);
    }

    async function handlePurchase() {
        if (!name.trim()) { setError('Name is required'); return; }
        if (!email.trim()) { setError('Email is required'); return; }
        if (!/^[^@]+@[^@]+\.[^@]+$/.test(email)) { setError('Please enter a valid email'); return; }
        if (selected.length === 0) { setError('Please select at least one seat'); return; }

        setPurchasing(true);
        setError('');

        const purchaseResult = await TicketEndpoint.purchaseTickets(
            Number(showId),
            selected.map((s) => ({ row: s.row, seat: s.seat })),
            name,
            email,
        );

        setPurchasing(false);

        if (purchaseResult?.success) {
            setResult(purchaseResult);
        } else {
            setError(purchaseResult?.message ?? 'Purchase failed');
            // Refresh seat map on conflict
            const seatData = await TicketEndpoint.getSeatsForShow(Number(showId));
            setSeats(seatData ?? []);
            setSelected([]);
        }
    }

    if (loading) {
        return <div className="seat-selection-container"><p>Loading...</p></div>;
    }

    if (!showDetail) {
        return (
            <div className="seat-selection-container">
                <p>Show not found.</p>
                <Link to="/" className="back-link">Back to Movies</Link>
            </div>
        );
    }

    if (result) {
        return (
            <div className="seat-selection-container">
                <div className="confirmation">
                    <h2>Purchase Confirmed!</h2>
                    <p>{result.message}</p>
                    <p><strong>{showDetail.movieTitle}</strong></p>
                    <p style={{ color: 'var(--text-secondary)' }}>
                        {showDetail.dateTime} &bull; {showDetail.roomName}
                    </p>
                    <ul className="ticket-list">
                        {result.tickets?.map((t, i) => (
                            <li key={i}>Seat {t.label}</li>
                        ))}
                    </ul>
                    <Link to={`/movie/${showDetail.movieId}`} className="back-link">
                        &larr; Back to Movie
                    </Link>
                </div>
            </div>
        );
    }

    // Build seat grid from flat list
    const seatGrid: SeatStatus[][] = [];
    for (let r = 1; r <= showDetail.rows; r++) {
        const row: SeatStatus[] = [];
        for (let s = 1; s <= showDetail.seatsPerRow; s++) {
            const seatStatus = seats.find((st) => st.row === r && st.seat === s);
            row.push(seatStatus ?? { row: r, seat: s, sold: false });
        }
        seatGrid.push(row);
    }

    return (
        <div className="seat-selection-container">
            <Link to={`/movie/${showDetail.movieId}`} className="back-link">
                &larr; Back to Movie
            </Link>

            <div className="show-info">
                <h1>{showDetail.movieTitle}</h1>
                <p className="details">
                    {showDetail.dateTime} &bull; {showDetail.roomName}
                </p>
            </div>

            <div className="screen-indicator">
                <div className="screen-bar"></div>
                <span className="screen-label">Screen</span>
            </div>

            <div className="seat-map">
                {seatGrid.map((row, rowIdx) => (
                    <div key={rowIdx} className="seat-row">
                        <span className="row-label">{rowLabel(rowIdx + 1)}</span>
                        {row.map((s) => {
                            const isSold = s.sold;
                            const isSelected = selected.some(
                                (sel) => sel.row === s.row && sel.seat === s.seat
                            );
                            let className = 'seat ';
                            if (isSold) className += 'sold';
                            else if (isSelected) className += 'selected';
                            else className += 'available';

                            return (
                                <button
                                    key={s.seat}
                                    className={className}
                                    onClick={() => !isSold && toggleSeat(s.row, s.seat)}
                                    disabled={isSold}
                                    title={`${rowLabel(s.row)}${s.seat}`}
                                >
                                    {s.seat}
                                </button>
                            );
                        })}
                        <span className="row-label">{rowLabel(rowIdx + 1)}</span>
                    </div>
                ))}
            </div>

            <div className="seat-legend">
                <div className="legend-item">
                    <div className="legend-swatch" style={{ background: 'var(--seat-available)' }}></div>
                    Available
                </div>
                <div className="legend-item">
                    <div className="legend-swatch" style={{ background: 'var(--seat-selected)' }}></div>
                    Selected
                </div>
                <div className="legend-item">
                    <div className="legend-swatch" style={{ background: 'var(--seat-sold)' }}></div>
                    Sold
                </div>
            </div>

            <div className="purchase-form">
                <p className="summary">
                    {selected.length === 0
                        ? 'Select seats to continue'
                        : `${selected.length} seat(s) selected${selected.length >= 6 ? ' (max)' : ''}`}
                </p>
                <input
                    type="text"
                    placeholder="Your name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="email"
                    placeholder="Your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                {error && <div className="error-message">{error}</div>}
                <button
                    className="purchase-btn"
                    onClick={handlePurchase}
                    disabled={selected.length === 0 || purchasing}
                >
                    {purchasing ? 'Processing...' : `Purchase ${selected.length} Ticket(s)`}
                </button>
            </div>
        </div>
    );
}

export const config = {
    route: ':showId',
    flowLayout: false,
    title: 'Seat Selection',
};
