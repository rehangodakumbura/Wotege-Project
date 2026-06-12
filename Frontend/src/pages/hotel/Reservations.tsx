import { motion } from 'motion/react';
import { Calendar as CalendarIcon, Plus, Search, Filter, MoreHorizontal, User, Clock, Users, LayoutList, CalendarDays, ChevronLeft, ChevronRight } from 'lucide-react';
import { useState } from 'react';
import { reservationsData } from '@/data/mockData';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';

const statusStyles: Record<string, string> = {
  'Confirmed': 'text-blue-400 bg-blue-400/10 border-blue-400/20',
  'In House': 'text-wotege-gold bg-wotege-gold/10 border-wotege-gold/20',
  'Pending': 'text-orange-400 bg-orange-400/10 border-orange-400/20',
  'Completed': 'text-green-400 bg-green-400/10 border-green-400/20',
};

const mockCustomers = [
  { id: 1, name: 'Alex M.', phone: '+94 77 123 4567', email: 'alex@example.com', loyalty: 1250 },
  { id: 2, name: 'Sarah W.', phone: '+44 7700 900077', email: 'sarah.w@example.com', loyalty: 450 },
  { id: 3, name: 'John D.', phone: '+1 555-0198', email: 'john.d@example.com', loyalty: 0 },
];

export default function Reservations() {
  const [viewType, setViewType] = useState<'list' | 'calendar'>('list');
  const [calendarMode, setCalendarMode] = useState<'daily' | 'weekly' | 'monthly'>('weekly');
  
  const [search, setSearch] = useState('');
  const [isNewBookingOpen, setIsNewBookingOpen] = useState(false);
  const [bookingType, setBookingType] = useState<'standard' | 'couple' | 'event'>('standard');

  const [searchGuestName, setSearchGuestName] = useState('');
  const [guestPhone, setGuestPhone] = useState('');
  const [guestEmail, setGuestEmail] = useState('');
  const [showGuestDropdown, setShowGuestDropdown] = useState(false);

  const handleGuestSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchGuestName(e.target.value);
    setShowGuestDropdown(true);
  };

  const selectGuest = (guest: any) => {
    setSearchGuestName(guest.name);
    setGuestPhone(guest.phone);
    setGuestEmail(guest.email);
    setShowGuestDropdown(false);
  };
  
  const { toast } = useToast();

  const handleBookingSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsNewBookingOpen(false);
    toast('Reservation created successfully!', 'success');
  };

  const filteredReservations = reservationsData.filter(res => 
    !search || res.guest.toLowerCase().includes(search.toLowerCase()) || res.id.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Reservations</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Sell Rooms & Manage Bookings</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="flex bg-[#141414] border border-white/5 rounded-full p-1 mr-0">
            <button 
              onClick={() => setViewType('list')}
              className={`p-2 rounded-full transition-colors ${viewType === 'list' ? 'bg-wotege-gold text-black' : 'text-white/40 hover:text-white'}`}
            >
              <LayoutList className="w-4 h-4" />
            </button>
            <button 
              onClick={() => setViewType('calendar')}
              className={`p-2 rounded-full transition-colors ${viewType === 'calendar' ? 'bg-wotege-gold text-black' : 'text-white/40 hover:text-white'}`}
            >
              <CalendarDays className="w-4 h-4" />
            </button>
          </div>
          
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search bookings..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button className="p-2.5 bg-[#141414] border border-white/5 rounded-full text-white/40 hover:text-white transition-colors">
            <Filter className="w-5 h-5" />
          </button>
          <button onClick={() => setIsNewBookingOpen(true)} className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center shrink-0 w-full sm:w-auto justify-center">
            <Plus className="w-4 h-4 mr-1" /> New Booking
          </button>
        </div>
      </header>

      {/* Stats row or Calendar controls */}
      {viewType === 'list' ? (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 shrink-0">
          {[
            { label: 'Today\'s Check-ins', value: '14' },
            { label: 'Today\'s Check-outs', value: '8' },
            { label: 'Upcoming Events', value: '2' },
            { label: 'Availability', value: '26 Rooms' },
          ].map((stat, i) => (
            <div key={i} className="bg-[#111] border border-white/5 p-6 rounded-[2rem]">
               <p className="text-xs uppercase tracking-widest text-white/40 font-bold mb-2">{stat.label}</p>
               <h3 className="text-3xl font-serif text-[#F5F2ED]">{stat.value}</h3>
            </div>
          ))}
        </div>
      ) : (
        <div className="flex items-center justify-between shrink-0 bg-[#111] border border-white/5 p-4 rounded-3xl">
          <div className="flex items-center gap-4">
            <button className="p-2 hover:bg-white/5 rounded-full transition-colors text-white/60"><ChevronLeft className="w-5 h-5" /></button>
            <h2 className="text-lg font-serif text-[#F5F2ED] w-32 text-center">May 2026</h2>
            <button className="p-2 hover:bg-white/5 rounded-full transition-colors text-white/60"><ChevronRight className="w-5 h-5" /></button>
          </div>
          <div className="flex space-x-2 bg-[#141414] rounded-full p-1 border border-white/5">
             <button onClick={() => setCalendarMode('daily')} className={`px-4 py-1.5 rounded-full text-xs font-bold uppercase tracking-wider transition-all ${calendarMode === 'daily' ? 'bg-wotege-gold/10 text-wotege-gold' : 'text-white/40 hover:text-white/80'}`}>Daily</button>
             <button onClick={() => setCalendarMode('weekly')} className={`px-4 py-1.5 rounded-full text-xs font-bold uppercase tracking-wider transition-all ${calendarMode === 'weekly' ? 'bg-wotege-gold/10 text-wotege-gold' : 'text-white/40 hover:text-white/80'}`}>Weekly</button>
             <button onClick={() => setCalendarMode('monthly')} className={`px-4 py-1.5 rounded-full text-xs font-bold uppercase tracking-wider transition-all ${calendarMode === 'monthly' ? 'bg-wotege-gold/10 text-wotege-gold' : 'text-white/40 hover:text-white/80'}`}>Monthly</button>
          </div>
        </div>
      )}

      {/* Main Area */}
      <div className="flex-1 bg-[#111] border border-white/5 rounded-[2rem] p-6 flex flex-col overflow-hidden">
        {viewType === 'list' ? (
          <>
            <div className="flex justify-between items-center mb-6">
              <h4 className="text-sm font-bold uppercase tracking-widest text-white/80">Active Reservations</h4>
              <div className="flex space-x-2">
                <button className="px-4 py-1.5 rounded-full border border-white/10 text-xs hover:border-wotege-gold/50 transition-all font-medium">Upcoming</button>
                <button className="px-4 py-1.5 rounded-full bg-white/5 border border-white/20 text-xs font-medium">All Time</button>
              </div>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 no-scrollbar">
              <div className="min-w-[800px]">
                <div className="grid grid-cols-6 gap-4 py-3 px-4 border-b border-white/5 text-[10px] uppercase tracking-widest font-bold text-white/40">
                  <div>Booking ID</div>
                  <div className="col-span-2">Guest & Room</div>
                  <div>Dates</div>
                  <div>Amount</div>
                  <div>Status</div>
                </div>
                
                <div className="space-y-2 mt-4">
                  {filteredReservations.map((res, i) => (
                    <motion.div 
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: i * 0.05 }}
                      key={res.id} 
                      className="grid grid-cols-6 gap-4 py-4 px-4 bg-white/5 rounded-2xl items-center hover:bg-white/10 transition-colors border border-transparent hover:border-white/5 cursor-pointer"
                    >
                      <div className="text-sm font-serif text-wotege-gold">{res.id}</div>
                      
                      <div className="col-span-2 flex items-center space-x-3">
                        <div className="w-10 h-10 rounded-full bg-[#141414] border border-white/5 flex items-center justify-center shrink-0">
                          <User className="w-4 h-4 text-white/40" />
                        </div>
                        <div>
                          <div className="text-sm font-medium text-[#F5F2ED]">{res.guest}</div>
                          <div className="text-[10px] text-white/40 uppercase tracking-widest">{res.room}</div>
                        </div>
                      </div>

                      <div>
                        <div className="text-sm text-[#F5F2ED] flex items-center">
                          <CalendarIcon className="w-3 h-3 mr-1.5 text-white/40" />
                          {res.checkIn}
                        </div>
                        <div className="text-[10px] text-white/40 uppercase tracking-widest mt-1">to {res.checkOut}</div>
                      </div>

                      <div className="text-sm text-[#F5F2ED] font-medium">{res.amount}</div>

                      <div className="flex justify-between items-center">
                        <div className={`px-3 py-1.5 text-[10px] uppercase font-bold tracking-widest rounded-full border flex items-center w-fit ${statusStyles[res.status]}`}>
                          {res.status}
                        </div>
                        <button className="text-white/30 hover:text-white transition-colors p-1">
                          <MoreHorizontal className="w-5 h-5" />
                        </button>
                      </div>
                    </motion.div>
                  ))}
                </div>
              </div>
            </div>
          </>
        ) : (
          <div className="flex-1 flex flex-col relative overflow-hidden">
            {/* Calendar Layout Simulation */}
            <div className="flex border-b border-white/5 pb-2 pl-24 shrink-0">
              {Array.from({ length: calendarMode === 'weekly' ? 7 : calendarMode === 'daily' ? 1 : 14 }).map((_, i) => (
                <div key={i} className="flex-1 text-center border-l border-white/5 px-2">
                  <div className="text-xs text-white/40 uppercase text-center font-bold tracking-widest">May {14 + i}</div>
                </div>
              ))}
            </div>
            
            <div className="flex-1 overflow-y-auto mt-4">
              {[101, 102, 201, 202, 203, 301, 302].map(roomNum => (
                <div key={roomNum} className="flex border-b border-white/5 min-h-[60px] group relative">
                  <div className="w-24 shrink-0 pt-2 pr-4 border-r border-white/5 bg-[#111] z-10 sticky left-0">
                    <span className="text-sm font-serif text-[#F5F2ED]">Room {roomNum}</span>
                    <span className="text-[10px] text-white/40 block uppercase tracking-widest">{roomNum > 200 ? 'Suite' : 'Deluxe'}</span>
                  </div>
                  <div className="flex-1 flex relative">
                     {Array.from({ length: calendarMode === 'weekly' ? 7 : calendarMode === 'daily' ? 1 : 14 }).map((_, i) => (
                       <div key={i} className="flex-1 border-l border-white/5 group-hover:bg-white/[0.02] transition-colors" />
                     ))}
                     
                     {roomNum === 102 && (
                       <div className="absolute top-2 bottom-2 left-[10%] right-[30%] bg-wotege-gold/10 border border-wotege-gold/30 rounded-lg p-2 text-wotege-gold overflow-hidden">
                         <div className="text-xs font-bold truncate">Alex M.</div>
                         <div className="text-[10px] uppercase opacity-70 truncate">Checking out</div>
                       </div>
                     )}
                     {roomNum === 203 && (
                       <div className="absolute top-2 bottom-2 left-[40%] right-[10%] bg-purple-500/10 border border-purple-500/30 rounded-lg p-2 text-purple-400 overflow-hidden">
                         <div className="text-xs font-bold truncate">Sarah W.</div>
                         <div className="text-[10px] uppercase opacity-70 truncate">Reserved - Couple</div>
                       </div>
                     )}
                     {roomNum === 301 && (
                       <div className="absolute top-2 bottom-2 left-0 right-[60%] bg-red-500/10 border border-red-500/30 rounded-lg p-2 text-red-400 overflow-hidden">
                         <div className="text-xs font-bold truncate">Maintenance</div>
                       </div>
                     )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      <Modal isOpen={isNewBookingOpen} onClose={() => setIsNewBookingOpen(false)} title="New Reservation Wizard">
         <form onSubmit={handleBookingSubmit} className="space-y-8">
            {/* Booking Type */}
            <div>
              <div className="flex gap-4">
                <label className="flex items-center gap-2 text-sm text-[#F5F2ED] cursor-pointer group">
                  <input type="radio" name="bookingType" checked={bookingType === 'standard'} onChange={() => setBookingType('standard')} className="accent-wotege-gold w-4 h-4" />
                  <span className="group-hover:text-wotege-gold transition-colors">Standard Guest</span>
                </label>
                <label className="flex items-center gap-2 text-sm text-[#F5F2ED] cursor-pointer group">
                  <input type="radio" name="bookingType" checked={bookingType === 'couple'} onChange={() => setBookingType('couple')} className="accent-wotege-gold w-4 h-4" />
                  <span className="group-hover:text-wotege-gold transition-colors">Couple Booking</span>
                </label>
                <label className="flex items-center gap-2 text-sm text-[#F5F2ED] cursor-pointer group">
                  <input type="radio" name="bookingType" checked={bookingType === 'event'} onChange={() => setBookingType('event')} className="accent-wotege-gold w-4 h-4" />
                  <span className="group-hover:text-wotege-gold transition-colors">Event Reservation</span>
                </label>
              </div>
            </div>

            {bookingType === 'event' ? (
              <div className="space-y-4">
                <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Event Details</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">Event Name <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" placeholder="e.g. Smith Wedding Reception" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">Organizer Name <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">Organizer Phone <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Expected Guests</label>
                     <input type="number" min="1" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Hall / Space Allocation</label>
                     <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                       <option>Grand Ballroom</option>
                       <option>Rooftop Terrace</option>
                       <option>Private Dining Room A</option>
                       <option>Poolside Area</option>
                     </select>
                  </div>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Primary Guest Details</h4>
                <div className="relative">
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Guest Name (Auto-search)</label>
                   <input 
                     type="text" 
                     value={searchGuestName}
                     onChange={handleGuestSearch}
                     onFocus={() => setShowGuestDropdown(true)}
                     onBlur={() => setTimeout(() => setShowGuestDropdown(false), 200)}
                     placeholder="Start typing guest name or phone number..." 
                     className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" 
                     required 
                   />
                   
                   {showGuestDropdown && searchGuestName.length > 0 && (
                     <div className="absolute z-50 w-full mt-2 bg-[#1a1a1a] border border-white/10 rounded-xl shadow-2xl overflow-hidden max-h-48 overflow-y-auto">
                        {mockCustomers
                          .filter(c => c.name.toLowerCase().includes(searchGuestName.toLowerCase()) || c.phone.includes(searchGuestName))
                          .map(c => (
                            <div 
                              key={c.id} 
                              onClick={() => selectGuest(c)}
                              className="p-3 border-b border-white/5 hover:bg-white/5 cursor-pointer flex justify-between items-center transition-colors"
                            >
                               <div>
                                 <div className="text-sm font-medium text-[#F5F2ED]">{c.name}</div>
                                 <div className="text-[10px] text-white/40">{c.phone}</div>
                               </div>
                               <div className="text-[10px] uppercase font-bold tracking-widest text-wotege-gold bg-wotege-gold/10 px-2 py-0.5 rounded">
                                 {c.loyalty} Pts
                               </div>
                            </div>
                        ))}
                     </div>
                   )}
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Phone</label>
                     <input type="text" value={guestPhone} onChange={(e) => setGuestPhone(e.target.value)} className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Email</label>
                     <input type="email" value={guestEmail} onChange={(e) => setGuestEmail(e.target.value)} className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">NIC / Passport <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Nationality</label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Date of Birth</label>
                     <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Gender</label>
                     <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                       <option>Male</option>
                       <option>Female</option>
                       <option>Other</option>
                     </select>
                  </div>
                </div>
              </div>
            )}

            {bookingType === 'couple' && (
              <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} className="space-y-4 pt-4 border-t border-white/5">
                <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Secondary Guest Details</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">Full Name <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2 flex items-center">NIC / Passport <span className="text-red-500 ml-1">*</span></label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Phone</label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
                  </div>
                  <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Email</label>
                     <input type="email" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
                  </div>
                </div>
              </motion.div>
            )}

            <div className="space-y-4">
              <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Stay Details</h4>
               <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Duration Type</label>
                   <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                     <option>Per Night</option>
                     <option>Per Hour</option>
                   </select>
                 </div>
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room / Type</label>
                   <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                     <option>Any Standard</option>
                     <option>Any Deluxe</option>
                     <option>Room 101 - Deluxe Suite (Avail)</option>
                     <option>Room 202 - Standard (Avail)</option>
                   </select>
                 </div>
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check In</label>
                   <div className="flex gap-2">
                     <input type="date" className="w-2/3 bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                     <input type="time" className="w-1/3 bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                   </div>
                 </div>
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check Out</label>
                   <div className="flex gap-2">
                     <input type="date" className="w-2/3 bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                     <input type="time" className="w-1/3 bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                   </div>
                 </div>
              </div>
            </div>
            
            <div className="space-y-4">
              <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Additional</h4>
              <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Special Requests / Notes</label>
                 <textarea rows={2} className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"></textarea>
              </div>
              <div className="flex items-center gap-2 mt-4">
                <input type="checkbox" defaultChecked className="accent-wotege-gold w-4 h-4 cursor-pointer" />
                <span className="text-sm text-white/60">Generate digital invoice and send via email</span>
              </div>
            </div>

            <div className="bg-wotege-gold/10 border border-wotege-gold/20 p-4 rounded-xl flex items-center justify-between">
               <span className="text-wotege-gold font-serif text-lg">Total Estimate</span>
               <span className="text-wotege-gold font-serif text-2xl">LKR 45,000</span>
            </div>

            <div className="flex justify-end pt-4">
               <button type="submit" className="w-full py-4 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]">Confirm Booking</button>
            </div>
         </form>
      </Modal>
    </div>
  );
}
