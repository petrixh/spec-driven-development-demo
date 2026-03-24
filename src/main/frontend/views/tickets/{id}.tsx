import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { Button } from '@vaadin/react-components/Button';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import type TicketDetail from 'Frontend/generated/com/example/specdriven/ticket/TicketDetail';

const statusBadgeClass: Record<string, string> = {
  OPEN: 'badge-open',
  IN_PROGRESS: 'badge-in-progress',
  RESOLVED: 'badge-resolved',
  CLOSED: 'badge-closed',
};

const priorityBadgeClass: Record<string, string> = {
  LOW: 'badge-low',
  MEDIUM: 'badge-medium',
  HIGH: 'badge-high',
  CRITICAL: 'badge-critical',
};

function formatStatus(status: string): string {
  return status.replace('_', ' ');
}

function formatDateTime(dateStr: string | undefined): string {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleString();
}

export default function TicketDetailView() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [ticket, setTicket] = useState<TicketDetail | undefined>();
  const [error, setError] = useState('');

  useEffect(() => {
    if (id) {
      TicketEndpoint.getTicketDetail(parseInt(id))
        .then(setTicket)
        .catch(() => setError('Ticket not found'));
    }
  }, [id]);

  if (error) {
    return <p style={{ color: 'var(--vaadin-error-color)' }}>{error}</p>;
  }

  if (!ticket) {
    return <p>Loading...</p>;
  }

  return (
    <div>
      <Button theme="tertiary" onClick={() => navigate('/tickets')} style={{ marginBottom: 'var(--vaadin-space-m)' }}>
        &larr; Back to My Tickets
      </Button>

      <h2 style={{ fontSize: 'var(--aura-font-size-xl)', marginTop: 0 }}>{ticket.title}</h2>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
        gap: 'var(--vaadin-space-m)',
        marginBottom: 'var(--vaadin-space-l)',
        border: '1px solid var(--vaadin-contrast-10pct, #e2e8f0)',
        borderRadius: 'var(--vaadin-radius-m, 8px)',
        padding: 'var(--vaadin-space-m)',
      }}>
        <div>
          <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>Status</div>
          <span className={`badge ${statusBadgeClass[ticket.status ?? '']}`}>{formatStatus(ticket.status ?? '')}</span>
        </div>
        <div>
          <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>Priority</div>
          <span className={`badge ${priorityBadgeClass[ticket.priority ?? '']}`}>{ticket.priority}</span>
        </div>
        <div>
          <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>Category</div>
          <div>{ticket.category}</div>
        </div>
        <div>
          <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>Created</div>
          <div>{formatDateTime(ticket.createdDate)}</div>
        </div>
        <div>
          <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>Assigned To</div>
          <div>{ticket.assignedToName ?? 'Unassigned'}</div>
        </div>
      </div>

      <h3 style={{ fontSize: 'var(--aura-font-size-l)' }}>Description</h3>
      <p style={{ whiteSpace: 'pre-wrap' }}>{ticket.description}</p>

      <h3 style={{ fontSize: 'var(--aura-font-size-l)' }}>Comments</h3>
      {ticket.comments && ticket.comments.length > 0 ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-s)' }}>
          {ticket.comments.map(comment => (
            <div key={comment.id} className="comment-item">
              <div className="comment-meta">
                <strong>{comment.authorName}</strong> &middot; {formatDateTime(comment.createdDate)}
              </div>
              <p style={{ margin: 'var(--vaadin-space-xs) 0 0' }}>{comment.text}</p>
            </div>
          ))}
        </div>
      ) : (
        <p style={{ color: 'var(--vaadin-secondary-text-color)' }}>No comments yet.</p>
      )}
    </div>
  );
}
