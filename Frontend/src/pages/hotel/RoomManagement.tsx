import { motion } from 'motion/react';
import { Search, Filter, Plus, Calendar as CalendarIcon, CheckCircle2, Clock, XCircle, LogIn, LogOut, Wrench, BedDouble, Waves, Sun } from 'lucide-react';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';

const initialRooms = [
  { id: '101', type: 'Deluxe Suite', status: 'Available', floor: 1, price: 35000, beds: 2, amenities: ['Sea View', 'Balcony'] },
  { id: '102', type: 'Deluxe Suite', status: 'Occupied', floor: 1, guest: 'Alex M.', checkout: 'Today', beds: 2, amenities: ['Balcony'] },
  { id: '201', type: 'Presidential', status: 'Cleaning', floor: 2, beds: 3, amenities: ['Sea View', 'Private Pool', 'Balcony'] },
  { id: '202', type: 'Standard', status: 'Available', floor: 2, price: 15000, beds: 1, amenities: [] },
  { id: '203', type: 'Standard', status: 'Reserved', floor: 2, guest: 'Sarah W.', checkin: 'Tomorrow', beds: 1, amenities: ['Sea View'] },
  { id: '301', type: 'Penthouse', status: 'Maintenance', floor: 3, beds: 4, amenities: ['Sea View', 'Balcony', 'Jacuzzi'] },
];

const statusColors: Record<string, string> = {
  'Available': 'text-green-400 bg-green-400/10 border-green-400/20',
  'Occupied': 'text-wotege-gold bg-wotege-gold/10 border-wotege-gold/20',
  'Cleaning': 'text-blue-400 bg-blue-400/10 border-blue-400/20',
  'Reserved': 'text-purple-400 bg-purple-400/10 border-purple-400/20',
  'Maintenance': 'text-red-400 bg-red-400/10 border-red-400/20',
};

const statusIcons: Record<string, any> = {
  'Available': CheckCircle2,
  'Occupied': CalendarIcon,
  'Cleaning': Clock,
  'Reserved': CalendarIcon,
  'Maintenance': XCircle,
};

export default function RoomManagement() {
  const { toast } = useToast();
  const [filter, setFilter] = useState('All Rooms');
  const [search, setSearch] = useState('');
  const [bedFilter, setBedFilter] = useState('All');
  const [amenityFilter, setAmenityFilter] = useState('All');
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);

  const [isAddRoomOpen, setIsAddRoomOpen] = useState(false);
  const [isNewBookingOpen, setIsNewBookingOpen] = useState(false);
  const [rooms, setRooms] = useState(initialRooms);
  
  // Quick Action Modal states
  const [actionModal, setActionModal] = useState<{isOpen: boolean, type: string, roomId: string}>({isOpen: false, type: '', roomId: ''});

  const filteredRooms = rooms.filter(room => {
    if (filter !== 'All Rooms' && room.status !== filter) return false;
    if (search && !room.id.includes(search) && !room.type.toLowerCase().includes(search.toLowerCase()) && !room.guest?.toLowerCase().includes(search.toLowerCase()) && !room.amenities.some(a => a.toLowerCase().includes(search.toLowerCase()))) return false;
    if (bedFilter !== 'All' && room.beds.toString() !== bedFilter) return false;
    if (amenityFilter !== 'All' && !room.amenities.includes(amenityFilter)) return false;
    return true;
  });

  const handleAddRoom = (e: React.FormEvent) => {
    e.preventDefault();
    setIsAddRoomOpen(false);
    toast('Room added successfully', 'success');
  };

  const handleNewBooking = (e: React.FormEvent) => {
    e.preventDefault();
    setIsNewBookingOpen(false);
    toast('Booking confirmed', 'success');
  };

  const confirmAction = () => {
    const { type, roomId } = actionModal;
    setRooms(prev => prev.map(r => {
      if (r.id === roomId) {
        if (type === 'check-in') return { ...r, status: 'Occupied' };
        if (type === 'check-out') return { ...r, status: 'Cleaning', guest: undefined, checkout: undefined };
        if (type === 'complete-maintenance') return { ...r, status: 'Available' };
        if (type === 'complete-cleaning') return { ...r, status: 'Available' };
      }
      return r;
    }));
    setActionModal({ isOpen: false, type: '', roomId: '' });
    toast(`Successfully completed: ${type.replace('-', ' ')}`, 'success');
  };

  const getActionTitle = (type: string) => {
    switch (type) {
      case 'check-in': return 'Confirm Check-in';
      case 'check-out': return 'Confirm Check-out';
      case 'complete-maintenance': return 'Complete Maintenance';
      case 'complete-cleaning': return 'Complete Cleaning';
      default: return 'Confirm Action';
    }
  };

  return (
    <div className="space-y-8 h-full flex flex-col overflow-y-auto no-scrollbar">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Room Management</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Manage hotel rooms and occupancies</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <button 
            onClick={() => setShowAdvancedFilters(!showAdvancedFilters)}
            className={`p-2.5 border rounded-full transition-colors ${showAdvancedFilters ? 'bg-wotege-gold/10 border-wotege-gold text-wotege-gold' : 'bg-[#141414] border-white/5 text-white/40 hover:text-white'}`}
          >
            <Filter className="w-5 h-5" />
          </button>
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search rooms..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button onClick={() => setIsNewBookingOpen(true)} className="px-4 py-2.5 bg-[#141414] text-[#F5F2ED] border border-white/10 rounded-full text-xs font-bold uppercase tracking-wider hover:border-wotege-gold/50 transition-all flex items-center shrink-0 w-full sm:w-auto justify-center">
            <CalendarIcon className="w-4 h-4 mr-1.5" /> New Booking
          </button>
          <button onClick={() => setIsAddRoomOpen(true)} className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center shrink-0 w-full sm:w-auto justify-center">
            <Plus className="w-4 h-4 mr-1" /> Add Room
          </button>
        </div>
      </header>
      
      {/* Advanced Filters */}
      {showAdvancedFilters && (
        <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} className="flex flex-wrap gap-4 bg-[#111] border border-white/5 p-4 rounded-2xl shrink-0">
          <div className="flex items-center gap-2">
            <label className="text-[10px] uppercase font-bold tracking-widest text-white/40">Beds:</label>
            <select value={bedFilter} onChange={(e) => setBedFilter(e.target.value)} className="bg-[#141414] border border-white/10 rounded-lg px-2 py-1.5 text-xs text-[#F5F2ED] focus:outline-none focus:border-wotege-gold/50">
              <option value="All">All</option>
              <option value="1">1 Bed</option>
              <option value="2">2 Beds</option>
              <option value="3">3 Beds</option>
              <option value="4">4+ Beds</option>
            </select>
          </div>
          <div className="flex items-center gap-2">
            <label className="text-[10px] uppercase font-bold tracking-widest text-white/40">Amenities:</label>
            <select value={amenityFilter} onChange={(e) => setAmenityFilter(e.target.value)} className="bg-[#141414] border border-white/10 rounded-lg px-2 py-1.5 text-xs text-[#F5F2ED] focus:outline-none focus:border-wotege-gold/50">
              <option value="All">All</option>
              <option value="Sea View">Sea View</option>
              <option value="Balcony">Balcony</option>
              <option value="Jacuzzi">Jacuzzi</option>
              <option value="Private Pool">Private Pool</option>
            </select>
          </div>
        </motion.div>
      )}

      {/* Filters (Desktop) */}
      <div className="hidden md:flex flex-col space-y-3 shrink-0">
        <div className="flex items-center space-x-2 overflow-x-auto pb-2">
          {['All Rooms', 'Available', 'Occupied', 'Cleaning', 'Reserved', 'Maintenance'].map((f, i) => (
            <button 
              key={f}
              onClick={() => setFilter(f)}
              className={`px-5 py-2 rounded-full text-xs font-bold uppercase tracking-wider whitespace-nowrap transition-all ${
                filter === f 
                  ? 'bg-[#141414] border border-wotege-gold text-wotege-gold shadow-[0_0_10px_rgba(197,160,89,0.2)]' 
                  : 'bg-[#111] border border-white/5 text-white/40 hover:text-wotege-gold'
              }`}
            >
              {f}
            </button>
          ))}
        </div>
        <div className="flex items-center space-x-2 overflow-x-auto pb-2">
          <span className="text-[10px] uppercase font-bold tracking-widest text-white/40 mr-2">Quick Filters:</span>
          {['All', 'Sea View', 'Balcony'].map((a) => (
            <button 
              key={a}
              onClick={() => setAmenityFilter(a)}
              className={`px-4 py-1.5 rounded-full text-[10px] font-bold uppercase tracking-wider transition-all ${
                amenityFilter === a 
                  ? 'bg-wotege-gold/20 text-wotege-gold border border-wotege-gold/50' 
                  : 'bg-white/5 border border-white/10 text-white/40 hover:text-white'
              }`}
            >
              {a === 'All' ? 'All Amenities' : a}
            </button>
          ))}
        </div>
      </div>

      {/* Room Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6 content-start flex-1 overflow-y-auto">
        {filteredRooms.map((room, index) => {
          const Icon = statusIcons[room.status];
          return (
            <motion.div
              layout
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: index * 0.05, layout: { duration: 0.3 } }}
              key={`${room.id}-${room.status}`}
              className="bg-[#111] border border-white/5 p-6 rounded-[2rem] flex flex-col justify-between hover:border-wotege-gold/30 transition-all cursor-pointer group shadow-[0_4px_20px_rgba(0,0,0,0.2)]"
            >
              <div className="flex justify-between items-start mb-6">
                <div>
                  <div className="text-3xl font-serif text-[#F5F2ED] group-hover:text-wotege-gold transition-colors block leading-none mb-2">{room.id}</div>
                  <div className="text-[10px] text-white/40 uppercase tracking-widest font-bold">{room.type}</div>
                </div>
                <div className={`px-3 py-1.5 text-[10px] uppercase font-bold tracking-widest rounded-full border flex items-center space-x-1 ${statusColors[room.status]}`}>
                  <Icon className="w-3 h-3" strokeWidth={2.5} />
                  <span>{room.status}</span>
                </div>
              </div>

              <div className="flex flex-wrap gap-2 mb-2">
                 <span className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5" title="Beds">
                   <BedDouble className="w-3 h-3 mr-1" />
                   {room.beds} Bed{room.beds > 1 ? 's' : ''}
                 </span>
                 {room.amenities.slice(0, 2).map(amen => {
                    const AmenityIcon = amen === 'Sea View' ? Waves : amen === 'Balcony' ? Sun : CheckCircle2;
                    return (
                      <span key={amen} className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5">
                        <AmenityIcon className="w-3 h-3 mr-1" />
                        {amen}
                      </span>
                    );
                 })}
                 {room.amenities.length > 2 && <span className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5">+{room.amenities.length - 2}</span>}
              </div>

              <div className="space-y-4 mt-2 pt-6 border-t border-white/5">
                {room.status === 'Available' && (
                  <div className="flex justify-between items-center text-sm font-medium">
                    <span className="text-white/40">Rate</span>
                    <span className="text-[#F5F2ED]">LKR {room.price?.toLocaleString()}/night</span>
                  </div>
                )}
                {room.status === 'Occupied' && (
                  <>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Guest</span>
                      <span className="text-[#F5F2ED]">{room.guest}</span>
                    </div>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Booking</span>
                      <span className="text-[#F5F2ED]">2 Nights</span>
                    </div>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Check-in</span>
                      <span className="text-white/60">12 May, 14:00</span>
                    </div>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Checkout</span>
                      <span className="text-wotege-gold">{room.checkout} (12:00)</span>
                    </div>
                    <div className="flex justify-between items-center mt-2 pt-4 border-t border-white/5">
                      <button 
                        onClick={(e) => { e.stopPropagation(); setActionModal({ isOpen: true, type: 'check-out', roomId: room.id }); }}
                        className="w-full px-4 py-2.5 bg-white/5 hover:bg-white/10 rounded-xl text-xs font-bold uppercase tracking-wider text-white transition-colors flex items-center justify-center gap-2"
                      >
                        <LogOut className="w-4 h-4" /> Check-Out
                      </button>
                    </div>
                  </>
                )}
                {room.status === 'Reserved' && (
                  <>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Upcoming</span>
                      <span className="text-[#F5F2ED]">{room.guest}</span>
                    </div>
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Check-in</span>
                      <span className="text-[#F5F2ED]">{room.checkin}</span>
                    </div>
                    <div className="flex justify-between items-center mt-2 pt-4 border-t border-white/5">
                      <button 
                        onClick={(e) => { e.stopPropagation(); setActionModal({ isOpen: true, type: 'check-in', roomId: room.id }); }}
                        className="w-full px-4 py-2.5 bg-wotege-gold text-black rounded-xl text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.3)] transition-all flex items-center justify-center gap-2"
                      >
                        <LogIn className="w-4 h-4" /> Check-In
                      </button>
                    </div>
                  </>
                )}
                {(room.status === 'Cleaning') && (
                  <div className="flex items-center justify-between text-xs font-medium uppercase tracking-wider text-white/40">
                    <div className="flex items-center">
                      <Clock className="w-4 h-4 mr-2" />
                      Est. 2hrs
                    </div>
                    <button 
                      onClick={(e) => { e.stopPropagation(); setActionModal({ isOpen: true, type: 'complete-cleaning', roomId: room.id }); }}
                      className="px-3 py-1.5 bg-white/5 hover:bg-white/10 rounded-full transition-colors text-white"
                    >
                      Complete
                    </button>
                  </div>
                )}
                {(room.status === 'Maintenance') && (
                  <div className="flex items-center justify-between text-xs font-medium uppercase tracking-wider text-white/40">
                    <div className="flex items-center text-red-400">
                      <Wrench className="w-4 h-4 mr-2" />
                      Under Repair
                    </div>
                    <button 
                      onClick={(e) => { e.stopPropagation(); setActionModal({ isOpen: true, type: 'complete-maintenance', roomId: room.id }); }}
                      className="px-3 py-1.5 bg-white/5 hover:bg-white/10 rounded-full transition-colors text-white"
                    >
                      Complete
                    </button>
                  </div>
                )}
              </div>
            </motion.div>
          );
        })}
      </div>

      {/* Confirmation Modal */}
      <Modal isOpen={actionModal.isOpen} onClose={() => setActionModal({ isOpen: false, type: '', roomId: '' })} title={getActionTitle(actionModal.type)}>
        <div className="space-y-6">
          <p className="text-white/60 text-sm">
            Are you sure you want to {actionModal.type.replace('-', ' ')} for room <span className="text-wotege-gold font-bold">{actionModal.roomId}</span>?
          </p>
          <div className="flex justify-end gap-3">
            <button 
              onClick={() => setActionModal({ isOpen: false, type: '', roomId: '' })}
              className="px-6 py-3 bg-[#141414] border border-white/10 rounded-xl font-bold text-xs uppercase tracking-wider hover:bg-white/5 transition-all text-[#F5F2ED]"
            >
              Cancel
            </button>
            <button 
              onClick={confirmAction}
              className="px-6 py-3 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]"
            >
              Confirm
            </button>
          </div>
        </div>
      </Modal>

      <Modal isOpen={isAddRoomOpen} onClose={() => setIsAddRoomOpen(false)} title="Add New Room">
         <form onSubmit={handleAddRoom} className="space-y-4">
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room Number</label>
               <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Type</label>
               <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                 <option>Standard</option>
                 <option>Deluxe Suite</option>
                 <option>Presidential</option>
                 <option>Penthouse</option>
               </select>
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Floor</label>
               <input type="number" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Base Rate (LKR)</label>
               <input type="number" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <button type="submit" className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]">Save Room</button>
         </form>
      </Modal>

      <Modal isOpen={isNewBookingOpen} onClose={() => setIsNewBookingOpen(false)} title="New Booking">
         <form onSubmit={handleNewBooking} className="space-y-4">
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Guest Name</label>
               <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room / Type</label>
               <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                 <option>Any Standard</option>
                 <option>Any Deluxe</option>
                 <option>Room 101 - Deluxe Suite (Avail)</option>
                 <option>Room 202 - Standard (Avail)</option>
               </select>
            </div>
             <div className="grid grid-cols-2 gap-4">
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check In</label>
                 <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
               </div>
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check Out</label>
                 <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
               </div>
            </div>
            <button type="submit" className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]">Confirm Booking</button>
         </form>
      </Modal>
    </div>
  );
}
