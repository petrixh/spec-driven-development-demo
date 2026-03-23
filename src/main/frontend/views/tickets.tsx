import { useEffect, useState } from 'react';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import type TicketSummary from 'Frontend/generated/com/example/specdriven/ticket/TicketSummary';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  title: 'My Tickets',
};

export default function TicketsView() {
  const [tickets, setTickets] = useState<TicketSummary[]>([]);

  useEffect(() => {
    TicketEndpoint.getMyTickets().then(setTickets);
  }, []);

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
      <h2>My Tickets</h2>
      <ul>
        {tickets.map((t) => (
          <li key={t.id}>{t.title} — {t.status}</li>
        ))}
      </ul>
    </div>
  );
}
