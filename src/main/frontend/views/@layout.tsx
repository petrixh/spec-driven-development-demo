import { Outlet } from 'react-router';

export default function Layout() {
  return (
    <div className="min-h-screen bg-[#141414] text-white">
      <header className="p-4 border-b border-gray-800">
        <div className="max-width-container flex justify-between items-center">
          <a href="/" className="text-[#E50914] text-2xl font-bold no-underline">CINEFLEX</a>
          <nav>
            <a href="/admin" className="text-gray-400 no-underline hover:text-white">Admin</a>
          </nav>
        </div>
      </header>
      <main>
        <Outlet />
      </main>
      <footer className="p-8 mt-12 border-t border-gray-800 text-center text-gray-500">
        <p>&copy; 2026 Cineflex. All rights reserved.</p>
      </footer>
    </div>
  );
}
