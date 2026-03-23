import { AppLayout } from '@vaadin/react-components/AppLayout';
import { NavLink, Outlet } from 'react-router';

export default function MainLayout() {
  return (
    <AppLayout>
      <div slot="navbar" style={{ display: 'flex', alignItems: 'center', width: '100%', padding: '0 var(--vaadin-space-l)', gap: 'var(--vaadin-space-l)' }}>
        <span className="resolve-brand" style={{ marginRight: 'auto' }}>
          <span className="resolve-brand-re">re</span>
          <span className="resolve-brand-colon">:</span>
          <span className="resolve-brand-solve">solve</span>
        </span>
        <nav className="resolve-nav" style={{ display: 'flex', gap: 'var(--vaadin-space-m)' }}>
          <NavLink to="/submit">Submit Ticket</NavLink>
          <NavLink to="/tickets">My Tickets</NavLink>
        </nav>
      </div>
      <div style={{ padding: 'var(--vaadin-space-l)', maxWidth: '1200px', margin: '0 auto' }}>
        <Outlet />
      </div>
    </AppLayout>
  );
}
