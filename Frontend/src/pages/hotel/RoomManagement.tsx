import { motion } from 'motion/react';
import { Search, Filter, Plus, Calendar as CalendarIcon, CheckCircle2, Clock, XCircle, LogIn, LogOut, Wrench, BedDouble, Waves, Sun, RefreshCw, AlertCircle, Loader2 } from 'lucide-react';
import { useEffect, useMemo, useState, type FormEvent } from 'react';
import { format, parseISO, differenceInCalendarDays, isToday, isTomorrow, isYesterday, addDays } from 'date-fns';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';
import { useAuth } from '@/lib/auth-context';
import {
  type RoomView,
  type Room,
  type RoomStatus,
  type RoomTypeEntity,
  type Reservation,
  getRooms,
  getRoomTypes,
  createRoom,
  createReservation,
  updateRoomStatus,
  updateReservationStatus,
} from '@/lib/api';

type ActionType = 'check-in' | 'check-out' | 'complete-cleaning' | 'complete-maintenance';

interface AddRoomForm {
  roomNumber: string;
  roomTypeId: string;
  floor: string;
  price: string;
  beds: string;
  amenities: string;
}

interface NewBookingForm {
  guestName: string;
  guestEmail: string;
  guestPhone: string;
  roomId: string;
  checkInDate: string;
  checkOutDate: string;
  notes: string;
}

const emptyAddRoomForm: AddRoomForm = {
  roomNumber: '',
  roomTypeId: '',
  floor: '',
  price: '',
  beds: '',
  amenities: '',
};

const emptyBookingForm: NewBookingForm = {
  guestName: '',
  guestEmail: '',
  guestPhone: '',
  roomId: '',
  checkInDate: format(new Date(), 'yyyy-MM-dd'),
  checkOutDate: format(addDays(new Date(), 1), 'yyyy-MM-dd'),
  notes: '',
};

const statusColors: Record<RoomStatus, string> = {
  AVAILABLE: 'text-green-400 bg-green-400/10 border-green-400/20',
  OCCUPIED: 'text-wotege-gold bg-wotege-gold/10 border-wotege-gold/20',
  CLEANING: 'text-blue-400 bg-blue-400/10 border-blue-400/20',
  RESERVED: 'text-purple-400 bg-purple-400/10 border-purple-400/20',
  MAINTENANCE: 'text-red-400 bg-red-400/10 border-red-400/20',
};

const statusIcons: Record<RoomStatus, any> = {
  AVAILABLE: CheckCircle2,
  OCCUPIED: CalendarIcon,
  CLEANING: Clock,
  RESERVED: CalendarIcon,
  MAINTENANCE: XCircle,
};

const statusFilters: Array<{ label: string; value: 'ALL' | RoomStatus }> = [
  { label: 'All Rooms', value: 'ALL' },
  { label: 'Available', value: 'AVAILABLE' },
  { label: 'Occupied', value: 'OCCUPIED' },
  { label: 'Cleaning', value: 'CLEANING' },
  { label: 'Reserved', value: 'RESERVED' },
  { label: 'Maintenance', value: 'MAINTENANCE' },
];

function formatRelativeDate(dateStr: string | null | undefined): string {
  if (!dateStr) return '—';
  try {
    const d = parseISO(dateStr);
    if (isToday(d)) return 'Today';
    if (isTomorrow(d)) return 'Tomorrow';
    if (isYesterday(d)) return 'Yesterday';
    return format(d, 'd MMM');
  } catch {
    return dateStr;
  }
}

function formatTime(timeStr: string | null | undefined): string {
  if (!timeStr) return '';
  // Backend returns "HH:mm:ss" or "HH:mm"
  const parts = timeStr.split(':');
  if (parts.length < 2) return timeStr;
  return `${parts[0]}:${parts[1]}`;
}

function formatDateWithTime(dateStr: string | null | undefined, timeStr: string | null | undefined): string {
  if (!dateStr) return '—';
  try {
    const d = parseISO(dateStr);
    const base = format(d, 'd MMM');
    return timeStr ? `${base}, ${formatTime(timeStr)}` : base;
  } catch {
    return dateStr;
  }
}

function nightsBetween(checkIn: string | null | undefined, checkOut: string | null | undefined): number {
  if (!checkIn || !checkOut) return 0;
  try {
    const n = differenceInCalendarDays(parseISO(checkOut), parseISO(checkIn));
    return Math.max(0, n);
  } catch {
    return 0;
  }
}

export default function RoomManagement() {
  const { token } = useAuth();
  const { toast } = useToast();

  const [rooms, setRooms] = useState<RoomView[]>([]);
  const [roomTypes, setRoomTypes] = useState<RoomTypeEntity[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [acting, setActing] = useState(false);

  const [filter, setFilter] = useState<'ALL' | RoomStatus>('ALL');
  const [search, setSearch] = useState('');
  const [bedFilter, setBedFilter] = useState('All');
  const [amenityFilter, setAmenityFilter] = useState('All');
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);

  const [isAddRoomOpen, setIsAddRoomOpen] = useState(false);
  const [isNewBookingOpen, setIsNewBookingOpen] = useState(false);
  const [addRoomForm, setAddRoomForm] = useState<AddRoomForm>(emptyAddRoomForm);
  const [bookingForm, setBookingForm] = useState<NewBookingForm>(emptyBookingForm);
  const [addRoomSaving, setAddRoomSaving] = useState(false);
  const [bookingSaving, setBookingSaving] = useState(false);

  const [actionModal, setActionModal] = useState<{ isOpen: boolean; type: ActionType | ''; roomId: number | null }>(
    { isOpen: false, type: '', roomId: null }
  );

  const loadData = async (showSpinner = true) => {
    if (!token) return;
    if (showSpinner) setLoading(true);
    setError(null);
    try {
      const [roomsData, typesData] = await Promise.all([getRooms(token), getRoomTypes(token)]);
      setRooms(roomsData);
      setRoomTypes(typesData);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load rooms');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  const flatRooms: Room[] = useMemo(() => rooms.map(rv => rv.room), [rooms]);

  const filteredRooms = useMemo(() => {
    return flatRooms.filter(room => {
      const reservation = rooms.find(rv => rv.room.id === room.id)?.currentReservation ?? null;
      if (filter !== 'ALL' && room.status !== filter) return false;
      if (search) {
        const q = search.toLowerCase();
        const haystack = [
          room.roomNumber,
          room.roomType?.name ?? '',
          room.guestName ?? '',
          reservation?.guestName ?? '',
          ...(room.amenities ?? []),
        ].join(' ').toLowerCase();
        if (!haystack.includes(q)) return false;
      }
      if (bedFilter !== 'All') {
        if (bedFilter === '4+') {
          if (room.beds < 4) return false;
        } else if (room.beds.toString() !== bedFilter) return false;
      }
      if (amenityFilter !== 'All' && !(room.amenities ?? []).includes(amenityFilter)) return false;
      return true;
    });
  }, [flatRooms, rooms, filter, search, bedFilter, amenityFilter]);

  const reservationFor = (roomId: number): Reservation | null => {
    return rooms.find(rv => rv.room.id === roomId)?.currentReservation ?? null;
  };

  const handleAddRoom = async (e: FormEvent) => {
    e.preventDefault();
    if (!token) return;
    setAddRoomSaving(true);
    try {
      const amenities = addRoomForm.amenities
        .split(',')
        .map(s => s.trim())
        .filter(Boolean);
      const roomTypeId = addRoomForm.roomTypeId ? Number(addRoomForm.roomTypeId) : null;
      await createRoom(
        {
          roomNumber: addRoomForm.roomNumber.trim(),
          floor: Number(addRoomForm.floor),
          price: Number(addRoomForm.price),
          beds: Number(addRoomForm.beds),
          status: 'AVAILABLE',
          amenities,
        },
        roomTypeId,
        token
      );
      toast('Room added successfully', 'success');
      setIsAddRoomOpen(false);
      setAddRoomForm(emptyAddRoomForm);
      await loadData(false);
    } catch (e2) {
      toast(e2 instanceof Error ? e2.message : 'Failed to add room', 'error');
    } finally {
      setAddRoomSaving(false);
    }
  };

  const handleNewBooking = async (e: FormEvent) => {
    e.preventDefault();
    if (!token) return;
    setBookingSaving(true);
    try {
      let roomIdNum: number | null = bookingForm.roomId ? Number(bookingForm.roomId) : null;

      // If "ANY:<roomTypeCode>" is selected, auto-pick the first available room of that type
      if (bookingForm.roomId.startsWith('ANY:')) {
        const code = bookingForm.roomId.substring(4);
        const targetType = roomTypes.find(t => t.code === code);
        if (targetType) {
          const candidate = flatRooms.find(
            r => r.status === 'AVAILABLE' && r.roomType?.code === code
          );
          if (candidate) {
            roomIdNum = candidate.id;
          }
        }
      }

      await createReservation(
        {
          guestName: bookingForm.guestName.trim(),
          guestEmail: bookingForm.guestEmail.trim() || null,
          guestPhone: bookingForm.guestPhone.trim() || null,
          checkInDate: bookingForm.checkInDate,
          checkOutDate: bookingForm.checkOutDate,
          checkInTime: '14:00',
          checkOutTime: '12:00',
          status: 'CONFIRMED',
          bookingType: 'STANDARD',
          notes: bookingForm.notes.trim() || null,
        },
        roomIdNum,
        token
      );
      toast('Booking confirmed', 'success');
      setIsNewBookingOpen(false);
      setBookingForm(emptyBookingForm);
      await loadData(false);
    } catch (e2) {
      toast(e2 instanceof Error ? e2.message : 'Failed to create booking', 'error');
    } finally {
      setBookingSaving(false);
    }
  };

  const confirmAction = async () => {
    const { type, roomId } = actionModal;
    if (!type || roomId == null || !token) return;
    setActing(true);
    try {
      const reservation = reservationFor(roomId);
      if (type === 'check-in') {
        if (reservation) {
          await updateReservationStatus(reservation.id, 'IN_HOUSE', token);
        } else {
          await updateRoomStatus(roomId, 'OCCUPIED', token);
        }
      } else if (type === 'check-out') {
        if (reservation) {
          await updateReservationStatus(reservation.id, 'COMPLETED', token);
        } else {
          await updateRoomStatus(roomId, 'CLEANING', token);
        }
      } else if (type === 'complete-cleaning' || type === 'complete-maintenance') {
        await updateRoomStatus(roomId, 'AVAILABLE', token);
      }
      toast(`Successfully completed: ${type.replace('-', ' ')}`, 'success');
      setActionModal({ isOpen: false, type: '', roomId: null });
      await loadData(false);
    } catch (e) {
      toast(e instanceof Error ? e.message : 'Action failed', 'error');
    } finally {
      setActing(false);
    }
  };

  const getActionTitle = (type: ActionType | '') => {
    switch (type) {
      case 'check-in': return 'Confirm Check-in';
      case 'check-out': return 'Confirm Check-out';
      case 'complete-maintenance': return 'Complete Maintenance';
      case 'complete-cleaning': return 'Complete Cleaning';
      default: return 'Confirm Action';
    }
  };

  const actionRoomNumber =
    actionModal.roomId != null ? flatRooms.find(r => r.id === actionModal.roomId)?.roomNumber ?? '' : '';

  const roomOptionsForBooking = useMemo(() => {
    const available = flatRooms.filter(r => r.status === 'AVAILABLE');
    return available;
  }, [flatRooms]);

  return (
    <div className="space-y-8 h-full flex flex-col overflow-y-auto no-scrollbar">
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Room Management</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Manage hotel rooms and occupancies</p>
        </div>

        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <button
            onClick={() => loadData()}
            disabled={loading}
            className="p-2.5 bg-[#141414] border border-white/5 text-white/40 hover:text-white rounded-full transition-colors disabled:opacity-50"
            title="Refresh"
          >
            <RefreshCw className={`w-5 h-5 ${loading ? 'animate-spin' : ''}`} />
          </button>
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

      {error && (
        <div className="bg-red-500/10 border border-red-500/30 text-red-400 px-4 py-3 rounded-2xl flex items-center gap-3 shrink-0">
          <AlertCircle className="w-5 h-5 shrink-0" />
          <span className="text-sm flex-1">{error}</span>
          <button onClick={() => loadData()} className="text-xs uppercase font-bold tracking-wider hover:text-white">
            Retry
          </button>
        </div>
      )}

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

      <div className="hidden md:flex flex-col space-y-3 shrink-0">
        <div className="flex items-center space-x-2 overflow-x-auto pb-2">
          {statusFilters.map((f) => (
            <button
              key={f.value}
              onClick={() => setFilter(f.value)}
              className={`px-5 py-2 rounded-full text-xs font-bold uppercase tracking-wider whitespace-nowrap transition-all ${
                filter === f.value
                  ? 'bg-[#141414] border border-wotege-gold text-wotege-gold shadow-[0_0_10px_rgba(197,160,89,0.2)]'
                  : 'bg-[#111] border border-white/5 text-white/40 hover:text-wotege-gold'
              }`}
            >
              {f.label}
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

      {loading && rooms.length === 0 ? (
        <div className="flex-1 flex items-center justify-center text-white/40 py-24">
          <Loader2 className="w-6 h-6 mr-3 animate-spin" />
          <span className="text-sm uppercase tracking-widest">Loading rooms…</span>
        </div>
      ) : filteredRooms.length === 0 ? (
        <div className="flex-1 flex flex-col items-center justify-center text-white/40 py-24">
          <BedDouble className="w-12 h-12 mb-3 opacity-30" />
          <span className="text-sm uppercase tracking-widest">No rooms match your filters</span>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6 content-start flex-1">
          {filteredRooms.map((room, index) => {
            const Icon = statusIcons[room.status];
            const reservation = reservationFor(room.id);
            const nights = reservation ? nightsBetween(reservation.checkInDate, reservation.checkOutDate) : 0;
            return (
              <motion.div
                layout
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: index * 0.05, layout: { duration: 0.3 } }}
                key={`${room.id}-${room.status}`}
                className="bg-[#111] border border-white/5 p-6 rounded-[2rem] flex flex-col justify-between hover:border-wotege-gold/30 transition-all group shadow-[0_4px_20px_rgba(0,0,0,0.2)]"
              >
                <div className="flex justify-between items-start mb-6">
                  <div>
                    <div className="text-3xl font-serif text-[#F5F2ED] group-hover:text-wotege-gold transition-colors block leading-none mb-2">{room.roomNumber}</div>
                    <div className="text-[10px] text-white/40 uppercase tracking-widest font-bold">{room.roomType?.name ?? '—'}</div>
                  </div>
                  <div className={`px-3 py-1.5 text-[10px] uppercase font-bold tracking-widest rounded-full border flex items-center space-x-1 ${statusColors[room.status]}`}>
                    <Icon className="w-3 h-3" strokeWidth={2.5} />
                    <span>{room.status.charAt(0) + room.status.slice(1).toLowerCase()}</span>
                  </div>
                </div>

                <div className="flex flex-wrap gap-2 mb-2">
                  <span className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5" title="Beds">
                    <BedDouble className="w-3 h-3 mr-1" />
                    {room.beds} Bed{room.beds > 1 ? 's' : ''}
                  </span>
                  {(room.amenities ?? []).slice(0, 2).map(amen => {
                    const AmenityIcon = amen === 'Sea View' ? Waves : amen === 'Balcony' ? Sun : CheckCircle2;
                    return (
                      <span key={amen} className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5">
                        <AmenityIcon className="w-3 h-3 mr-1" />
                        {amen}
                      </span>
                    );
                  })}
                  {(room.amenities ?? []).length > 2 && <span className="flex items-center text-[10px] text-white/50 bg-[#141414] px-2 py-1 rounded border border-white/5">+{room.amenities.length - 2}</span>}
                </div>

                <div className="space-y-4 mt-2 pt-6 border-t border-white/5">
                  {room.status === 'AVAILABLE' && (
                    <div className="flex justify-between items-center text-sm font-medium">
                      <span className="text-white/40">Rate</span>
                      <span className="text-[#F5F2ED]">LKR {room.price?.toLocaleString()}/night</span>
                    </div>
                  )}
                  {room.status === 'OCCUPIED' && (
                    <>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Guest</span>
                        <span className="text-[#F5F2ED]">{reservation?.guestName ?? room.guestName ?? '—'}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Booking</span>
                        <span className="text-[#F5F2ED]">{nights} Night{nights === 1 ? '' : 's'}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Check-in</span>
                        <span className="text-white/60">{formatDateWithTime(reservation?.checkInDate, reservation?.checkInTime)}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Checkout</span>
                        <span className="text-wotege-gold">
                          {formatRelativeDate(reservation?.checkOutDate)}
                          {reservation?.checkOutTime ? ` (${formatTime(reservation.checkOutTime)})` : ''}
                        </span>
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
                  {room.status === 'RESERVED' && (
                    <>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Upcoming</span>
                        <span className="text-[#F5F2ED]">{reservation?.guestName ?? room.guestName ?? '—'}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-white/40">Check-in</span>
                        <span className="text-[#F5F2ED]">{formatDateWithTime(reservation?.checkInDate, reservation?.checkInTime)}</span>
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
                  {room.status === 'CLEANING' && (
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
                  {room.status === 'MAINTENANCE' && (
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
      )}

      <Modal isOpen={actionModal.isOpen} onClose={() => !acting && setActionModal({ isOpen: false, type: '', roomId: null })} title={getActionTitle(actionModal.type)}>
        <div className="space-y-6">
          <p className="text-white/60 text-sm">
            Are you sure you want to {actionModal.type.replace('-', ' ')} for room <span className="text-wotege-gold font-bold">{actionRoomNumber}</span>?
          </p>
          <div className="flex justify-end gap-3">
            <button
              onClick={() => setActionModal({ isOpen: false, type: '', roomId: null })}
              disabled={acting}
              className="px-6 py-3 bg-[#141414] border border-white/10 rounded-xl font-bold text-xs uppercase tracking-wider hover:bg-white/5 transition-all text-[#F5F2ED] disabled:opacity-50"
            >
              Cancel
            </button>
            <button
              onClick={confirmAction}
              disabled={acting}
              className="px-6 py-3 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98] disabled:opacity-50 flex items-center gap-2"
            >
              {acting && <Loader2 className="w-3 h-3 animate-spin" />}
              Confirm
            </button>
          </div>
        </div>
      </Modal>

      <Modal isOpen={isAddRoomOpen} onClose={() => !addRoomSaving && setIsAddRoomOpen(false)} title="Add New Room">
        <form onSubmit={handleAddRoom} className="space-y-4">
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room Number</label>
            <input
              type="text"
              value={addRoomForm.roomNumber}
              onChange={(e) => setAddRoomForm({ ...addRoomForm, roomNumber: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              placeholder="e.g. 401"
              required
            />
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room Type</label>
            <select
              value={addRoomForm.roomTypeId}
              onChange={(e) => setAddRoomForm({ ...addRoomForm, roomTypeId: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              required
            >
              <option value="">Select type…</option>
              {roomTypes.map(rt => (
                <option key={rt.id} value={rt.id}>{rt.name} (LKR {rt.baseRate.toLocaleString()})</option>
              ))}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Floor</label>
              <input
                type="number"
                min="1"
                value={addRoomForm.floor}
                onChange={(e) => setAddRoomForm({ ...addRoomForm, floor: e.target.value })}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                required
              />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Beds</label>
              <input
                type="number"
                min="1"
                value={addRoomForm.beds}
                onChange={(e) => setAddRoomForm({ ...addRoomForm, beds: e.target.value })}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                required
              />
            </div>
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Base Rate (LKR / night)</label>
            <input
              type="number"
              min="0"
              value={addRoomForm.price}
              onChange={(e) => setAddRoomForm({ ...addRoomForm, price: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              required
            />
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Amenities (comma-separated)</label>
            <input
              type="text"
              value={addRoomForm.amenities}
              onChange={(e) => setAddRoomForm({ ...addRoomForm, amenities: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              placeholder="Sea View, Balcony, Jacuzzi"
            />
          </div>
          <button
            type="submit"
            disabled={addRoomSaving}
            className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98] disabled:opacity-50 flex items-center justify-center gap-2"
          >
            {addRoomSaving && <Loader2 className="w-3 h-3 animate-spin" />}
            Save Room
          </button>
        </form>
      </Modal>

      <Modal isOpen={isNewBookingOpen} onClose={() => !bookingSaving && setIsNewBookingOpen(false)} title="New Booking">
        <form onSubmit={handleNewBooking} className="space-y-4">
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Guest Name</label>
            <input
              type="text"
              value={bookingForm.guestName}
              onChange={(e) => setBookingForm({ ...bookingForm, guestName: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Email</label>
              <input
                type="email"
                value={bookingForm.guestEmail}
                onChange={(e) => setBookingForm({ ...bookingForm, guestEmail: e.target.value })}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                placeholder="optional"
              />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Phone</label>
              <input
                type="tel"
                value={bookingForm.guestPhone}
                onChange={(e) => setBookingForm({ ...bookingForm, guestPhone: e.target.value })}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                placeholder="optional"
              />
            </div>
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room</label>
            <select
              value={bookingForm.roomId}
              onChange={(e) => setBookingForm({ ...bookingForm, roomId: e.target.value })}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
              required
            >
              <option value="">Select a room…</option>
              {roomTypes.map(rt => (
                <option key={`any-${rt.code}`} value={`ANY:${rt.code}`}>
                  Any {rt.name} (auto-assign)
                </option>
              ))}
              {roomOptionsForBooking.map(r => (
                <option key={r.id} value={r.id}>
                  Room {r.roomNumber} — {r.roomType?.name ?? 'Standard'} (Available)
                </option>
              ))}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check In</label>
              <input
                type="date"
                value={bookingForm.checkInDate}
                onChange={(e) => setBookingForm({ ...bookingForm, checkInDate: e.target.value })}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                required
              />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Check Out</label>
              <input
                type="date"
                value={bookingForm.checkOutDate}
                onChange={(e) => setBookingForm({ ...bookingForm, checkOutDate: e.target.value })}
                min={bookingForm.checkInDate}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none"
                required
              />
            </div>
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Notes</label>
            <textarea
              value={bookingForm.notes}
              onChange={(e) => setBookingForm({ ...bookingForm, notes: e.target.value })}
              rows={2}
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none resize-none"
              placeholder="Optional notes"
            />
          </div>
          <button
            type="submit"
            disabled={bookingSaving}
            className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98] disabled:opacity-50 flex items-center justify-center gap-2"
          >
            {bookingSaving && <Loader2 className="w-3 h-3 animate-spin" />}
            Confirm Booking
          </button>
        </form>
      </Modal>
    </div>
  );
}
