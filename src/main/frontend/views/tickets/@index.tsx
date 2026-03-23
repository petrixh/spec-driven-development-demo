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
  LOW: { bg: '#F1F5F9', color: '#475569' },
  MEDIUM: { bg: '#FEF3C7', color: '#92400E' },
  HIGH: { bg: '#FED7AA', color: '#9A3412' },
  CRITICAL: { bg: '#FEE2E2', color: '#991B1B' },
};

const statusColors: Record<string, string> = {
  OPEN: '#2563EB',
  IN_PROGRESS: '#D97706',
  RESOLVED: '#16A34A',
  CLOSED: '#64748B',
};

function Badge({ label, bg, color }: { label: string; bg: string; color: string }) {
  return (
    <span style={{
      padding: '2px 8px', borderRadius: '4px', fontSize: '0.75rem',
      fontWeight: 600, backgroundColor: bg, color,
    }}>
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
        <p>No tickets yet. <a href="/submit">Submit one</a>.</p>
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
          const sc = statusColors[t.status ?? ''] ?? '#64748B';
          return (
            <div
              key={t.id}
              onClick={() => navigate(`/tickets/${t.id}`)}
              style={{
                padding: 'var(--vaadin-space-m)', borderRadius: '8px',
                backgroundColor: 'white', cursor: 'pointer',
                border: '1px solid #E2E8F0',
                display: 'flex', justifyContent: 'space-between', alignItems: 'center',
              }}
            >
              <div>
                <div style={{ fontWeight: 600, marginBottom: '4px' }}>{t.title}</div>
                <div style={{ display: 'flex', gap: '8px', alignItems: 'center', fontSize: '0.85rem', color: '#64748B' }}>
                  <span>{t.category}</span>
                  <span>·</span>
                  <Badge label={t.priority ?? ''} bg={pc.bg} color={pc.color} />
                  <span>·</span>
                  <span>{t.createdDate ? new Date(t.createdDate).toLocaleDateString() : ''}</span>
                </div>
              </div>
              <Badge label={(t.status ?? '').replace('_', ' ')} bg={`${sc}18`} color={sc} />
            </div>
          );
        })}
      </div>
    </div>
  );
}
