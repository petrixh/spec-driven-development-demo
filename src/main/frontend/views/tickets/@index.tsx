import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { Select } from '@vaadin/react-components/Select';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import type TicketSummary from 'Frontend/generated/com/example/specdriven/ticket/TicketSummary';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  title: 'My Tickets',
};

const priorityColors: Record<string, { bg: string; color: string }> = {
  LOW: { bg: 'var(--resolve-priority-low-bg)', color: 'var(--resolve-priority-low-color)' },
  MEDIUM: { bg: 'var(--resolve-priority-medium-bg)', color: 'var(--resolve-priority-medium-color)' },
  HIGH: { bg: 'var(--resolve-priority-high-bg)', color: 'var(--resolve-priority-high-color)' },
  CRITICAL: { bg: 'var(--resolve-priority-critical-bg)', color: 'var(--resolve-priority-critical-color)' },
};

const statusColors: Record<string, string> = {
  OPEN: 'var(--resolve-status-open)',
  IN_PROGRESS: 'var(--resolve-status-in-progress)',
  RESOLVED: 'var(--resolve-status-resolved)',
  CLOSED: 'var(--resolve-status-closed)',
};

function Badge({ label, bg, color }: { label: string; bg: string; color: string }) {
  return (
    <span className="resolve-badge" style={{ backgroundColor: bg, color }}>
      {label}
    </span>
  );
}

export default function TicketsView() {
  const [tickets, setTickets] = useState<TicketSummary[]>([]);
  const [statusFilter, setStatusFilter] = useState('ALL');
  const navigate = useNavigate();

  useEffect(() => {
    TicketEndpoint.getMyTickets().then(setTickets).catch(() => {
      window.location.href = '/login';
    });
  }, []);

  const filtered = statusFilter === 'ALL'
    ? tickets
    : tickets.filter((t) => t.status === statusFilter);

  if (tickets.length === 0) {
    return (
      <div>
        <h2>My Tickets</h2>
        <p className="resolve-meta">No tickets yet. <a href="/submit">Submit one</a>.</p>
      </div>
    );
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 'var(--vaadin-space-m)' }}>
        <h2 style={{ margin: 0 }}>My Tickets</h2>
        <Select
          label="Filter by status"
          value={statusFilter}
          onValueChanged={(e) => setStatusFilter(e.detail.value)}
          items={[
            { label: 'All', value: 'ALL' },
            { label: 'Open', value: 'OPEN' },
            { label: 'In Progress', value: 'IN_PROGRESS' },
            { label: 'Resolved', value: 'RESOLVED' },
            { label: 'Closed', value: 'CLOSED' },
          ]}
          style={{ width: '200px' }}
        />
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-s)' }}>
        {filtered.map((t) => {
          const pc = priorityColors[t.priority ?? ''] ?? priorityColors.MEDIUM;
          const sc = statusColors[t.status ?? ''] ?? 'var(--resolve-status-closed)';
          return (
            <div
              key={t.id}
              className="resolve-card"
              onClick={() => navigate(`/tickets/${t.id}`)}
              style={{ cursor: 'pointer', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
            >
              <div>
                <div style={{ fontWeight: 600, marginBottom: 'var(--vaadin-space-xs)' }}>{t.title}</div>
                <div className="resolve-meta" style={{ display: 'flex', gap: 'var(--vaadin-space-s)', alignItems: 'center' }}>
                  <span>{t.category}</span>
                  <span>·</span>
                  <Badge label={t.priority ?? ''} bg={pc.bg} color={pc.color} />
                  <span>·</span>
                  <span>{t.createdDate ? new Date(t.createdDate).toLocaleDateString() : ''}</span>
                </div>
              </div>
              <Badge label={(t.status ?? '').replace('_', ' ')} bg={`color-mix(in srgb, ${sc} 12%, transparent)`} color={sc} />
            </div>
          );
        })}
      </div>
    </div>
  );
}
