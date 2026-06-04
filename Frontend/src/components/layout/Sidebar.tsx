import { 
  Building2, 
  LayoutDashboard, 
  Users, 
  CalendarDays, 
  Store, 
  BarChart3, 
  Settings,
  LogOut,
  UserCircle,
  MenuSquare,
  Gift,
  PackageSearch,
  ShieldAlert,
  BedDouble
} from 'lucide-react';
import { NavLink, useLocation } from 'react-router-dom';
import { cn } from '@/lib/utils';

const navItems = [
  { icon: LayoutDashboard, label: 'Dashboard', path: '/dashboard' },
  { icon: Building2, label: 'Hotel Rooms', path: '/hotel/rooms' },
  { icon: BedDouble, label: 'Room Types', path: '/settings/room-types' },
  { icon: CalendarDays, label: 'Reservations', path: '/reservations' },
  { icon: Gift, label: 'Packages', path: '/hotel/packages' },
  { icon: Store, label: 'POS Terminal', path: '/pos' },
  { icon: MenuSquare, label: 'Restaurant Menu', path: '/restaurant/menu' },
  { icon: PackageSearch, label: 'Inventory', path: '/inventory' },
  { icon: UserCircle, label: 'Customers', path: '/customers' },
  { icon: Users, label: 'Staff', path: '/staff' },
  { icon: ShieldAlert, label: 'Roles Matrix', path: '/settings/roles' },
  { icon: BarChart3, label: 'Reports', path: '/reports' },
];

export function Sidebar() {
  const location = useLocation();

  return (
    <aside className="static inset-y-0 left-0 z-50 w-20 md:w-24 bg-wotege-charcoal border-r border-white/5 flex flex-col items-center py-6 md:py-8 shrink-0">
      <div className="mb-12">
        <span className="text-wotege-gold font-serif text-3xl font-bold tracking-tighter">W</span>
      </div>

      <nav className="flex-1 flex flex-col gap-6 w-full px-4 overflow-y-auto no-scrollbar scroll-smooth">
        {navItems.map((item) => {
          const isActive = location.pathname.startsWith(item.path);
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={cn(
                "w-full aspect-square max-w-[3.5rem] mx-auto rounded-xl flex items-center justify-center transition-all duration-200 group relative",
                isActive 
                  ? "bg-wotege-gold/10 text-wotege-gold shadow-[0_0_20px_rgba(197,160,89,0.2)]" 
                  : "text-white/40 hover:text-wotege-gold cursor-pointer"
              )}
              title={item.label}
            >
              <item.icon className="w-6 h-6 shrink-0 transition-colors" strokeWidth={1.5} />
            </NavLink>
          );
        })}
      </nav>

      <div className="mt-auto px-4 w-full flex flex-col gap-4">
        <NavLink to="/settings" className={({isActive}) => cn(
          "w-full aspect-square max-w-[3.5rem] mx-auto rounded-xl flex items-center justify-center transition-all",
          isActive ? "bg-wotege-gold/10 text-wotege-gold shadow-[0_0_20px_rgba(197,160,89,0.2)]" : "text-white/40 hover:text-wotege-gold"
        )}>
          <Settings className="w-6 h-6 shrink-0" strokeWidth={1.5} />
        </NavLink>
        <NavLink to="/login" className="w-full aspect-square max-w-[3.5rem] mx-auto rounded-xl flex items-center justify-center text-white/40 hover:text-red-400 hover:bg-red-500/10 transition-all">
          <LogOut className="w-6 h-6 shrink-0" strokeWidth={1.5} />
        </NavLink>
      </div>
    </aside>
  );
}
