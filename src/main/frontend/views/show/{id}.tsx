import { useParams } from 'react-router';

export default function ShowSeatSelectionPlaceholder() {
  const { id } = useParams();

  return (
    <main className="page">
      <span className="eyebrow" style={{ color: '#ff6b72' }}>
        Seat Selection
      </span>
      <h1 style={{ color: 'var(--text-primary)' }}>Show {id}</h1>
      <p>This route is ready for UC3 seat selection.</p>
    </main>
  );
}
