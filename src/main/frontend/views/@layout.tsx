import { Outlet } from 'react-router';

export default function Layout() {
    return (
        <div className="app-layout">
            <Outlet />
        </div>
    );
}

export const config = {
    flowLayout: false,
    title: 'CineMax',
};
