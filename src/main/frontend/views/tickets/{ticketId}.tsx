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
  OPEN: '#2563EB',
  IN_PROGRESS: '#D97706',
  RESOLVED: '#16A34A',
  CLOSED: '#64748B',
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

  if (!ticket) return <div>Loading...</div>;

  const sc = statusColors[ticket.status ?? ''] ?? '#64748B';

  return (
    <div style={{ maxWidth: '800px' }}>
      <Button theme="tertiary" onClick={() => navigate('/tickets')} style={{ marginBottom: 'var(--vaadin-space-m)' }}>
        ← Back to My Tickets
      </Button>

      <div style={{ backgroundColor: 'white', borderRadius: '8px', padding: 'var(--vaadin-space-l)', border: '1px solid #E2E8F0' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
          <h2 style={{ margin: 0 }}>{ticket.title}</h2>
          <span style={{
            padding: '4px 12px', borderRadius: '4px', fontSize: '0.85rem',
            fontWeight: 600, backgroundColor: `${sc}18`, color: sc,
          }}>
            {(ticket.status ?? '').replace('_', ' ')}
          </span>
        </div>

        <div style={{ display: 'flex', gap: 'var(--vaadin-space-m)', marginTop: 'var(--vaadin-space-s)', fontSize: '0.85rem', color: '#64748B' }}>
          <span>Category: {ticket.category}</span>
          <span>Priority: {ticket.priority}</span>
          <span>Created: {ticket.createdDate ? new Date(ticket.createdDate).toLocaleDateString() : ''}</span>
        </div>

        <p style={{ marginTop: 'var(--vaadin-space-m)', lineHeight: 1.6 }}>{ticket.description}</p>
      </div>

      <h3 style={{ marginTop: 'var(--vaadin-space-l)' }}>Comments</h3>
      {(!ticket.comments || ticket.comments.length === 0) ? (
        <p style={{ color: '#64748B' }}>No comments yet.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-s)' }}>
          {ticket.comments.map((c) => (
            <div key={c.id} style={{
              backgroundColor: 'white', borderRadius: '8px',
              padding: 'var(--vaadin-space-m)', border: '1px solid #E2E8F0',
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', color: '#64748B', marginBottom: '4px' }}>
                <strong>{c.authorName}</strong>
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
