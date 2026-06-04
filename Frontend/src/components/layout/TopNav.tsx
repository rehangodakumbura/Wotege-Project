import { Bell, Search, MapPin, ChevronDown, Clock } from 'lucide-react';
import { useState, useEffect, useRef, type KeyboardEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/lib/auth-context';
import { getBranches, type Branch } from '@/lib/api';

export function TopNav() {
  const [time, setTime] = useState(new Date());
  const [searchQuery, setSearchQuery] = useState('');
  const [branches, setBranches] = useState<Branch[]>([]);
  const [selectedBranch, setSelectedBranch] = useState<Branch | null>(null);
  const [showBranchDropdown, setShowBranchDropdown] = useState(false);
  const branchDropdownRef = useRef<HTMLDivElement>(null);
  const { user, logout, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setInterval(() => setTime(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  useEffect(() => {
    if (!token) return;
    getBranches(token)
      .then((data) => {
        setBranches(data);
        if (data.length > 0 && !selectedBranch) {
          setSelectedBranch(data[0]);
        }
      })
      .catch(() => {});
  }, [token]);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (branchDropdownRef.current && !branchDropdownRef.current.contains(e.target as Node)) {
        setShowBranchDropdown(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleSearchKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  return (
    <header className="h-20 md:h-24 px-4 md:px-8 flex items-center justify-between z-40 shrink-0 border-b border-white/5 md:border-none">
      <div className="flex items-center flex-1 max-w-md">
        <div className="relative w-full group flex items-center">
          <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
          <input 
            type="text" 
            placeholder="Search operations..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={handleSearchKeyDown}
            className="w-full bg-wotege-charcoal-light border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50 transition-all font-sans"
          />
        </div>
      </div>

      <div className="flex items-center space-x-6 ml-4">
        <div className="hidden md:flex items-center space-x-4 text-xs font-medium uppercase tracking-wider text-[#F5F2ED]">
          <div className="flex items-center space-x-2 bg-wotege-charcoal-light px-4 py-2.5 rounded-full border border-white/5">
            <div className="w-2 h-2 bg-wotege-gold rounded-full"></div>
            <span>{time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} GMT+4</span>
          </div>

          <div className="relative" ref={branchDropdownRef}>
            <button
              onClick={() => setShowBranchDropdown(!showBranchDropdown)}
              className="flex items-center space-x-1.5 bg-wotege-charcoal-light px-4 py-2.5 rounded-full border border-white/5 cursor-pointer hover:border-wotege-gold/30 transition-colors text-white/60 hover:text-[#F5F2ED]"
            >
              <MapPin className="w-4 h-4" />
              <span>{selectedBranch?.name || 'Select Location'}</span>
              <ChevronDown className={`w-3 h-3 ml-1 transition-transform ${showBranchDropdown ? 'rotate-180' : ''}`} />
            </button>

            {showBranchDropdown && (
              <div className="absolute right-0 mt-2 w-64 bg-wotege-charcoal/95 backdrop-blur-xl border border-white/10 rounded-2xl shadow-[0_10px_30px_rgba(0,0,0,0.5)] overflow-hidden z-50">
                <div className="px-4 py-3 border-b border-white/5">
                  <p className="text-[10px] uppercase tracking-widest text-white/40 font-bold">Select Branch</p>
                </div>
                <div className="py-1 max-h-60 overflow-y-auto">
                  {branches.length === 0 && (
                    <div className="px-4 py-3 text-xs text-white/30">No locations available</div>
                  )}
                  {branches.map((branch) => (
                    <button
                      key={branch.id}
                      onClick={() => {
                        setSelectedBranch(branch);
                        setShowBranchDropdown(false);
                      }}
                      className={`w-full text-left px-4 py-3 flex items-center gap-3 transition-colors hover:bg-white/5 ${
                        selectedBranch?.id === branch.id ? 'bg-wotege-gold/10 border-l-2 border-l-wotege-gold' : ''
                      }`}
                    >
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold shrink-0 ${
                        selectedBranch?.id === branch.id ? 'bg-wotege-gold/20 text-wotege-gold' : 'bg-white/10 text-white/40'
                      }`}>
                        {branch.name.charAt(0)}
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="text-sm font-medium text-[#F5F2ED] truncate">{branch.name}</div>
                        <div className="text-[10px] text-white/40 truncate">{branch.city}, {branch.country}</div>
                      </div>
                      {selectedBranch?.id === branch.id && (
                        <div className="w-2 h-2 rounded-full bg-wotege-gold shrink-0" />
                      )}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>

        <button className="relative p-2 text-white/40 hover:text-wotege-gold transition-colors group">
          <Bell className="w-5 h-5 group-hover:drop-shadow-[0_0_8px_rgba(197,160,89,0.5)] transition-all" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-wotege-gold rounded-full ring-2 ring-wotege-black animate-pulse" />
        </button>

        <div className="relative border-l border-white/5 pl-2">
          <button 
            className="flex items-center space-x-3 group outline-none"
            onClick={() => {
              const el = document.getElementById('profile-dropdown');
              if (el) el.classList.toggle('hidden');
            }}
            onBlur={() => {
              setTimeout(() => {
                 const el = document.getElementById('profile-dropdown');
                 if (el) el.classList.add('hidden');
              }, 150);
            }}
          >
            <div className="text-right hidden sm:block">
              <div className="text-sm font-medium text-[#F5F2ED] group-hover:text-wotege-gold transition-colors">
                {user?.fullName || 'User'}
              </div>
              <div className="text-[10px] text-white/40 uppercase tracking-widest">
                {user?.role ? (typeof user.role === 'string' ? user.role : user.role.code) : 'Admin'}
              </div>
            </div>
            <div className="w-10 h-10 rounded-full bg-wotege-charcoal-light border border-wotege-gold/30 p-0.5 overflow-hidden relative">
              <div className="w-full h-full bg-[#1A1A1A] rounded-full overflow-hidden flex items-center justify-center">
                <img 
                  src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=150&auto=format&fit=crop" 
                  alt="Profile" 
                  className="w-full h-full object-cover"
                />
              </div>
            </div>
          </button>

          <div id="profile-dropdown" className="hidden absolute right-0 mt-3 w-56 bg-wotege-charcoal/90 backdrop-blur-xl border border-white/10 rounded-2xl shadow-[0_10px_30px_rgba(0,0,0,0.5)] overflow-hidden origin-top-right transition-all">
            <div className="py-2">
              <div className="px-4 py-2 border-b border-white/5 mb-2">
                <p className="text-sm text-[#F5F2ED] font-medium">{user?.fullName || 'User'}</p>
                <p className="text-xs text-white/40">{user?.email || ''}</p>
              </div>
              <button
                onClick={() => {
                  document.getElementById('profile-dropdown')?.classList.add('hidden');
                  navigate('/profile');
                }}
                className="w-full text-left block px-4 py-2 text-sm text-gray-300 hover:text-[#F5F2ED] hover:bg-white/5 transition-colors"
              >
                Edit Profile
              </button>
              <button
                onClick={() => {
                  document.getElementById('profile-dropdown')?.classList.add('hidden');
                  navigate('/preferences');
                }}
                className="w-full text-left block px-4 py-2 text-sm text-gray-300 hover:text-[#F5F2ED] hover:bg-white/5 transition-colors"
              >
                Preferences
              </button>
              <div className="h-px bg-white/5 my-2"></div>
              <button onClick={handleLogout} className="w-full text-left px-4 py-2 text-sm text-red-400 hover:text-red-300 hover:bg-red-500/10 transition-colors">
                Sign out
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
