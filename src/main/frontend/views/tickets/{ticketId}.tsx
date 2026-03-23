import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { Button } from '@vaadin/react-components/Button';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import type TicketDetail from 'Frontend/generated/com/example/specdriven/ticket/TicketDetail';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  title: 'Ticket Detail',
};

const statusColors: Record<string, string> = {
  OPEN: 'var(--resolve-status-open)',
  IN_PROGRESS: 'var(--resolve-status-in-progress)',
  RESOLVED: 'var(--resolve-status-resolved)',
  CLOSED: 'var(--resolve-status-closed)',
};

export default function TicketDetailView() {
  const { ticketId } = useParams();
  const navigate = useNavigate();
  const [ticket, setTicket] = useState<TicketDetail | null>(null);

  useEffect(() => {
    if (ticketId) {
      TicketEndpoint.getTicketDetail(Number(ticketId)).then(setTicket).catch(() => {
        navigate('/tickets');
      });
    }
  }, [ticketId, navigate]);

  if (!ticket) return <div className="resolve-meta">Loading...</div>;

  const sc = statusColors[ticket.status ?? ''] ?? 'var(--resolve-status-closed)';

  return (
    <div style={{ maxWidth: '800px' }}>
      <Button theme="tertiary" onClick={() => navigate('/tickets')} style={{ marginBottom: 'var(--vaadin-space-m)' }}>
        ← Back to My Tickets
      </Button>

      <div className="resolve-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
          <h2 style={{ margin: 0 }}>{ticket.title}</h2>
          <span className="resolve-badge" style={{
            backgroundColor: `color-mix(in srgb, ${sc} 12%, transparent)`, color: sc,
          }}>
            {(ticket.status ?? '').replace('_', ' ')}
          </span>
        </div>

        <div className="resolve-meta" style={{ display: 'flex', gap: 'var(--vaadin-space-m)', marginTop: 'var(--vaadin-space-s)' }}>
          <span>Category: {ticket.category}</span>
          <span>Priority: {ticket.priority}</span>
          <span>Created: {ticket.createdDate ? new Date(ticket.createdDate).toLocaleDateString() : ''}</span>
        </div>

        <p style={{ marginTop: 'var(--vaadin-space-m)', lineHeight: 1.6 }}>{ticket.description}</p>
      </div>

      <h3 style={{ marginTop: 'var(--vaadin-space-l)' }}>Comments</h3>
      {(!ticket.comments || ticket.comments.length === 0) ? (
        <p className="resolve-meta">No comments yet.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-s)' }}>
          {ticket.comments.map((c) => (
            <div key={c.id} className="resolve-card">
              <div className="resolve-meta" style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 'var(--vaadin-space-xs)' }}>
                <strong style={{ color: 'inherit' }}>{c.authorName}</strong>
                <span>{c.createdDate ? new Date(c.createdDate).toLocaleString() : ''}</span>
              </div>
              <p style={{ margin: 0 }}>{c.text}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
