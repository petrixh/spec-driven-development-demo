import { useEffect } from 'react';
import { Outlet } from 'react-router';

export default function AppLayout() {
  useEffect(() => {
    const existing = document.querySelector('link[data-app-styles="true"]');
    if (existing) {
      return;
    }

    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = '/styles.css';
    link.dataset.appStyles = 'true';
    document.head.appendChild(link);

    return () => {
      link.remove();
    };
  }, []);

  return (
    <div className="app-shell">
      <Outlet />
    </div>
  );
}
