import { useParams } from 'react-router';

export default function ShowSeatSelectionPlaceholder() {
  const { id } = useParams();

  return (
    <main className="page">
      <span className="eyebrow" style={{ color: '#ff6b72' }}>
        Seat Selection
      </span>
      <h1 style={{ color: 'var(--text-primary)' }}>Show {id}</h1>
      <p style={{ color: 'var(--text-secondary)' }}>Seat selection coming in UC3.</p>
    </main>
  );
}
