import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { Select } from '@vaadin/react-components/Select';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import type TicketSummary from 'Frontend/generated/com/example/specdriven/ticket/TicketSummary';
import Status from 'Frontend/generated/com/example/specdriven/domain/Status';

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

function formatDate(dateStr: string | undefined): string {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString();
}

export default function MyTicketsView() {
  const navigate = useNavigate();
  const [tickets, setTickets] = useState<TicketSummary[]>([]);
  const [statusFilter, setStatusFilter] = useState<string>('');

  const statusItems = [
    { label: 'All Statuses', value: '' },
    ...Object.values(Status).map(s => ({ label: formatStatus(s), value: s })),
  ];

  useEffect(() => {
    loadTickets();
  }, [statusFilter]);

  async function loadTickets() {
    const filter = statusFilter ? (statusFilter as Status) : undefined;
    const result = await TicketEndpoint.getMyTickets(filter ?? null);
    setTickets(result);
  }

  return (
    <div>
      <h2 style={{ fontSize: 'var(--aura-font-size-xl)', marginTop: 0 }}>My Tickets</h2>

      <div style={{ marginBottom: 'var(--vaadin-space-m)' }}>
        <Select
          label="Filter by Status"
          items={statusItems}
          value={statusFilter}
          onValueChanged={(e) => setStatusFilter(e.detail.value)}
          style={{ minWidth: '200px' }}
        />
      </div>

      {tickets.length === 0 ? (
        <p style={{ color: 'var(--vaadin-secondary-text-color)' }}>
          No tickets found. <a href="/submit">Submit a ticket</a> to get started.
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-s)' }}>
          {tickets.map(ticket => (
            <div
              key={ticket.id}
              onClick={() => navigate(`/tickets/${ticket.id}`)}
              style={{
                border: '1px solid var(--vaadin-contrast-10pct, #e2e8f0)',
                borderRadius: 'var(--vaadin-radius-m, 8px)',
                padding: 'var(--vaadin-space-m)',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: 'var(--vaadin-space-m)',
                flexWrap: 'wrap',
              }}
            >
              <div style={{ flex: 1, minWidth: '200px' }}>
                <div style={{ fontWeight: 600 }}>{ticket.title}</div>
                <div style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>
                  #{ticket.id} &middot; {formatDate(ticket.createdDate)}
                </div>
              </div>
              <span className={`badge ${priorityBadgeClass[ticket.priority ?? '']}`}>
                {ticket.priority}
              </span>
              <span className={`badge ${statusBadgeClass[ticket.status ?? '']}`}>
                {formatStatus(ticket.status ?? '')}
              </span>
              <span style={{ fontSize: 'var(--aura-font-size-s)', color: 'var(--vaadin-secondary-text-color)' }}>
                {ticket.category}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
