import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { TopNav } from './TopNav';

export function MainLayout() {
  return (
    <div className="flex h-screen overflow-hidden bg-wotege-black relative">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0 h-full overflow-hidden">
        <TopNav />
        <main className="flex-1 overflow-y-auto p-4 md:p-6 lg:p-8 scroll-smooth relative">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
