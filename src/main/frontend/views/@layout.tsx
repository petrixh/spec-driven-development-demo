import { AppLayout } from '@vaadin/react-components/AppLayout';
import { SideNav } from '@vaadin/react-components/SideNav';
import { SideNavItem } from '@vaadin/react-components/SideNavItem';
import { DrawerToggle } from '@vaadin/react-components/DrawerToggle';
import { Outlet } from 'react-router';

export default function MainLayout() {
  return (
    <AppLayout>
      <div slot="navbar" style={{ display: 'flex', alignItems: 'center', width: '100%', padding: '0 var(--vaadin-space-m)' }}>
        <DrawerToggle />
        <img src="icons/icon.svg" alt="re:solve" style={{ height: '28px', marginRight: 'var(--vaadin-space-s)' }} />
        <h1 style={{ fontSize: 'var(--aura-font-size-l)', margin: 0, flex: 1 }}>re:solve</h1>
        <a href="/logout" style={{ color: 'inherit' }}>Log out</a>
      </div>
      <div slot="drawer">
        <SideNav>
          <SideNavItem path="/submit">Submit Ticket</SideNavItem>
          <SideNavItem path="/tickets">My Tickets</SideNavItem>
        </SideNav>
      </div>
      <div style={{ padding: 'var(--vaadin-space-l)', maxWidth: '1200px', margin: '0 auto', width: '100%' }}>
        <Outlet />
      </div>
    </AppLayout>
  );
}
