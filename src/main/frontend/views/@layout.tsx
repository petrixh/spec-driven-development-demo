import { AppLayout } from '@vaadin/react-components/AppLayout';
import { NavLink, Outlet } from 'react-router';

export default function MainLayout() {
  return (
    <AppLayout>
      <div slot="navbar" style={{ display: 'flex', alignItems: 'center', width: '100%', padding: '0 var(--vaadin-space-l)', gap: 'var(--vaadin-space-l)' }}>
        <span style={{ fontSize: 'var(--aura-font-size-l)', marginRight: 'auto' }}>
          <span style={{ fontWeight: 400, color: '#64748B' }}>re</span>
          <span style={{ fontWeight: 700, color: '#2563EB' }}>:</span>
          <span style={{ fontWeight: 700 }}>solve</span>
        </span>
        <nav style={{ display: 'flex', gap: 'var(--vaadin-space-m)' }}>
          <NavLink to="/submit" style={({ isActive }) => ({ fontWeight: isActive ? 700 : 400, textDecoration: 'none' })}>Submit Ticket</NavLink>
          <NavLink to="/tickets" style={({ isActive }) => ({ fontWeight: isActive ? 700 : 400, textDecoration: 'none' })}>My Tickets</NavLink>
        </nav>
      </div>
      <div style={{ padding: 'var(--vaadin-space-l)', maxWidth: '1200px', margin: '0 auto' }}>
        <Outlet />
      </div>
    </AppLayout>
  );
}
