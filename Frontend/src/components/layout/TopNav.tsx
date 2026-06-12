import { Bell, Search, MapPin, ChevronDown, Clock, Menu } from 'lucide-react';
import { useState, useEffect } from 'react';

export function TopNav({ onMenuClick }: { onMenuClick: () => void }) {
  const [time, setTime] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => setTime(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  return (
    <header className="h-20 md:h-24 px-4 md:px-8 flex items-center justify-between z-40 shrink-0 border-b border-white/5 md:border-none">
      
      {/* Global Search and Menu Button */}
      <div className="flex items-center flex-1 max-w-md">
        <button 
          onClick={onMenuClick} 
          className="md:hidden mr-3 p-2 bg-white/5 border border-white/10 rounded-xl text-white/60 hover:text-white transition-colors"
        >
          <Menu className="w-5 h-5" />
        </button>
        <div className="relative w-full group flex items-center">
          <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
          <input 
            type="text" 
            placeholder="Search operations..." 
            className="w-full bg-wotege-charcoal-light border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50 transition-all font-sans"
          />
        </div>
      </div>

      {/* Right Actions */}
      <div className="flex items-center space-x-6 ml-4">
        
        {/* Status Indicators */}
        <div className="hidden md:flex items-center space-x-4 text-xs font-medium uppercase tracking-wider text-[#F5F2ED]">
          <div className="flex items-center space-x-2 bg-wotege-charcoal-light px-4 py-2.5 rounded-full border border-white/5">
            <div className="w-2 h-2 bg-wotege-gold rounded-full"></div>
            <span>{time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} GMT+4</span>
          </div>
          <div className="flex items-center space-x-1.5 bg-wotege-charcoal-light px-4 py-2.5 rounded-full border border-white/5 cursor-pointer hover:border-wotege-gold/30 transition-colors text-white/60 hover:text-[#F5F2ED]">
            <MapPin className="w-4 h-4" />
            <span>Dubai Marina</span>
            <ChevronDown className="w-3 h-3 ml-1" />
          </div>
        </div>

        {/* Notifications */}
        <button className="relative p-2 text-white/40 hover:text-wotege-gold transition-colors group">
          <Bell className="w-5 h-5 group-hover:drop-shadow-[0_0_8px_rgba(197,160,89,0.5)] transition-all" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-wotege-gold rounded-full ring-2 ring-wotege-black animate-pulse" />
        </button>

        {/* Profile */}
        <div className="relative border-l border-white/5 pl-2">
          <button 
            className="flex items-center space-x-3 group outline-none"
            onClick={() => {
              const el = document.getElementById('profile-dropdown');
              if (el) el.classList.toggle('hidden');
            }}
            onBlur={(e) => {
              // Note: using setTimeout is a simple trick to allow click to trigger on dropdown items
              setTimeout(() => {
                 const el = document.getElementById('profile-dropdown');
                 if (el) el.classList.add('hidden');
              }, 150);
            }}
          >
            <div className="text-right hidden sm:block">
              <div className="text-sm font-medium text-[#F5F2ED] group-hover:text-wotege-gold transition-colors">A. Manager</div>
              <div className="text-[10px] text-white/40 uppercase tracking-widest">Super Admin</div>
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

          {/* Dropdown Menu */}
          <div id="profile-dropdown" className="hidden absolute right-0 mt-3 w-48 bg-wotege-charcoal/90 backdrop-blur-xl border border-white/10 rounded-2xl shadow-[0_10px_30px_rgba(0,0,0,0.5)] overflow-hidden origin-top-right transition-all">
            <div className="py-2">
              <div className="px-4 py-2 border-b border-white/5 mb-2">
                <p className="text-sm text-[#F5F2ED] font-medium">Alexander M.</p>
                <p className="text-xs text-white/40">manager@wotege.com</p>
              </div>
              <a href="#" className="block px-4 py-2 text-sm text-gray-300 hover:text-[#F5F2ED] hover:bg-white/5 transition-colors">Edit Profile</a>
              <a href="#" className="block px-4 py-2 text-sm text-gray-300 hover:text-[#F5F2ED] hover:bg-white/5 transition-colors">Preferences</a>
              <div className="h-px bg-white/5 my-2"></div>
              <a href="/login" className="block px-4 py-2 text-sm text-red-400 hover:text-red-300 hover:bg-red-500/10 transition-colors">Sign out</a>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
