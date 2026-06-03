import { motion } from 'motion/react';
import { BedDouble, Plus, Edit, Trash2, CheckCircle2 } from 'lucide-react';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';

const initialRoomTypes = [
  { id: 'RT-001', name: 'Standard Room', baseRate: 15000, beds: 1, capacity: 2, amenities: ['Free Wi-Fi', 'AC', 'TV', 'Mini Fridge'] },
  { id: 'RT-002', name: 'Deluxe Suite', baseRate: 35000, beds: 2, capacity: 4, amenities: ['Free Wi-Fi', 'AC', 'TV', 'Mini Bar', 'Sea View', 'Balcony'] },
  { id: 'RT-003', name: 'Presidential Suite', baseRate: 75000, beds: 3, capacity: 6, amenities: ['Free Wi-Fi', 'AC', 'Premium Bar', 'Sea View', 'Private Pool', 'Balcony', 'Jacuzzi'] },
  { id: 'RT-004', name: 'Penthouse', baseRate: 120000, beds: 4, capacity: 8, amenities: ['Free Wi-Fi', 'AC', 'Butler', 'Butler Service', 'Private Terrace', 'Jacuzzi', 'Sea View', 'Balcony'] },
];

export default function RoomTypes() {
  const { toast } = useToast();
  const [roomTypes, setRoomTypes] = useState(initialRoomTypes);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [editingType, setEditingType] = useState<any>(null);

  // Form states
  const [typeName, setTypeName] = useState('');
  const [baseRate, setBaseRate] = useState('');
  const [beds, setBeds] = useState('');
  const [capacity, setCapacity] = useState('');
  const [amenities, setAmenities] = useState<string[]>([]);
  const [newAmenity, setNewAmenity] = useState('');

  const openAddModal = () => {
    setEditingType(null);
    setTypeName('');
    setBaseRate('');
    setBeds('');
    setCapacity('');
    setAmenities([]);
    setIsAddModalOpen(true);
  };

  const openEditModal = (rt: any) => {
    setEditingType(rt);
    setTypeName(rt.name);
    setBaseRate(rt.baseRate.toString());
    setBeds(rt.beds.toString());
    setCapacity(rt.capacity.toString());
    setAmenities([...rt.amenities]);
    setIsAddModalOpen(true);
  };

  const handleAddAmenity = () => {
    if (newAmenity.trim() && !amenities.includes(newAmenity.trim())) {
      setAmenities([...amenities, newAmenity.trim()]);
      setNewAmenity('');
    }
  };

  const handleRemoveAmenity = (amenity: string) => {
    setAmenities(amenities.filter(a => a !== amenity));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingType) {
      setRoomTypes(roomTypes.map(rt => rt.id === editingType.id ? {
        ...rt,
        name: typeName,
        baseRate: Number(baseRate),
        beds: Number(beds),
        capacity: Number(capacity),
        amenities: amenities
      } : rt));
      toast('Room type updated successfully', 'success');
    } else {
      const newRt = {
        id: `RT-00${roomTypes.length + 1}`,
        name: typeName,
        baseRate: Number(baseRate),
        beds: Number(beds),
        capacity: Number(capacity),
        amenities: amenities
      };
      setRoomTypes([...roomTypes, newRt]);
      toast('New room type created successfully', 'success');
    }
    setIsAddModalOpen(false);
  };

  const handleDelete = (id: string) => {
    setRoomTypes(roomTypes.filter(rt => rt.id !== id));
    toast('Room type deleted', 'success');
  };

  return (
    <div className="space-y-8 h-full flex flex-col overflow-y-auto no-scrollbar pb-10">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight text-[#F5F2ED]">Room Types & Rates</h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Manage configurations and amenities</p>
        </div>
        
        <button 
          onClick={openAddModal} 
          className="px-5 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center shrink-0 w-fit"
        >
          <Plus className="w-4 h-4 mr-1" /> Add Room Type
        </button>
      </header>

      {/* Grid View */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 pb-6">
        {roomTypes.map((rt, idx) => (
          <motion.div 
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: idx * 0.05 }}
            key={rt.id} 
            className="bg-[#111] border border-white/5 p-6 md:p-8 rounded-[2rem] flex flex-col justify-between hover:border-wotege-gold/20 transition-all group shadow-[0_4px_20px_rgba(0,0,0,0.2)]"
          >
            <div className="flex justify-between items-start mb-6">
              <div>
                <h2 className="text-2xl font-serif text-[#F5F2ED] mb-2">{rt.name}</h2>
                <div className="text-[10px] text-white/40 uppercase tracking-widest font-bold bg-[#141414] px-3 py-1 rounded inline-block border border-white/5">{rt.id}</div>
              </div>
              <div className="flex flex-col items-end">
                <span className="text-2xl font-serif text-wotege-gold block leading-none">LKR {rt.baseRate.toLocaleString()}</span>
                <span className="text-[10px] uppercase text-white/40 tracking-widest font-bold mt-1">Base Per Night</span>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 mb-6 border-y border-white/5 py-6">
              <div>
                <div className="text-sm font-bold uppercase tracking-widest text-white/40 mb-1">Capacity</div>
                <div className="text-[#F5F2ED] font-medium">{rt.capacity} Adults</div>
              </div>
              <div>
                <div className="text-sm font-bold uppercase tracking-widest text-white/40 mb-1">Beds</div>
                <div className="text-[#F5F2ED] font-medium">{rt.beds} Bed(s)</div>
              </div>
            </div>

            <div className="flex-1">
               <div className="text-sm font-bold uppercase tracking-widest text-white/40 mb-3">Included Amenities</div>
               <div className="flex flex-wrap gap-2">
                 {rt.amenities.map(amenity => (
                   <span key={amenity} className="px-3 py-1.5 bg-white/5 border border-white/5 rounded-full text-xs text-white/60 flex items-center">
                     <CheckCircle2 className="w-3 h-3 text-wotege-gold mr-1.5" />
                     {amenity}
                   </span>
                 ))}
               </div>
            </div>

            <div className="flex gap-3 justify-end mt-8 shrink-0">
              <button 
                onClick={() => openEditModal(rt)}
                className="p-3 bg-[#141414] border border-white/10 rounded-xl text-white/60 hover:text-wotege-gold hover:border-wotege-gold/30 transition-colors"
                title="Edit Configuration"
              >
                <Edit className="w-4 h-4" />
              </button>
              <button 
                onClick={() => handleDelete(rt.id)}
                className="p-3 bg-red-500/10 border border-red-500/20 rounded-xl text-red-400 hover:bg-red-500 hover:text-white transition-colors"
                title="Delete Room Type"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
          </motion.div>
        ))}
      </div>

      <Modal isOpen={isAddModalOpen} onClose={() => setIsAddModalOpen(false)} title={editingType ? 'Edit Room Type' : 'Create Room Type'}>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="md:col-span-2">
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Room Type Name <span className="text-red-500">*</span></label>
              <input type="text" value={typeName} onChange={(e) => setTypeName(e.target.value)} required placeholder="e.g. Executive Suite" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Base Rate (LKR) <span className="text-red-500">*</span></label>
              <input type="number" value={baseRate} onChange={(e) => setBaseRate(e.target.value)} min="0" required placeholder="e.g. 15000" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Bed Count <span className="text-red-500">*</span></label>
              <input type="number" value={beds} onChange={(e) => setBeds(e.target.value)} min="1" required placeholder="e.g. 2" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>

            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Max Capacity (Adults) <span className="text-red-500">*</span></label>
              <input type="number" value={capacity} onChange={(e) => setCapacity(e.target.value)} min="1" required placeholder="e.g. 4" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
          </div>
          
          <div className="pt-2 border-t border-white/5">
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Amenities</label>
            <div className="flex gap-2 mb-3">
              <input 
                type="text" 
                value={newAmenity} 
                onChange={(e) => setNewAmenity(e.target.value)} 
                onKeyDown={(e) => { if(e.key === 'Enter') { e.preventDefault(); handleAddAmenity(); } }}
                placeholder="e.g. Ocean View" 
                className="flex-1 bg-[#141414] border border-white/10 rounded-xl px-4 py-2 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" 
              />
              <button type="button" onClick={handleAddAmenity} className="px-4 bg-white/5 border border-white/10 rounded-xl text-white/60 hover:text-white transition-colors">Add</button>
            </div>
            <div className="flex flex-wrap gap-2">
              {amenities.map(amenity => (
                <div key={amenity} className="bg-wotege-gold/10 border border-wotege-gold/20 text-wotege-gold px-3 py-1.5 rounded-full text-xs flex items-center">
                  <span>{amenity}</span>
                  <button type="button" onClick={() => handleRemoveAmenity(amenity)} className="ml-2 hover:text-white transition-colors cursor-pointer">
                     &times;
                  </button>
                </div>
              ))}
              {amenities.length === 0 && <span className="text-xs text-white/30 italic">No amenities added</span>}
            </div>
          </div>
          
          <div className="flex justify-end pt-4 gap-3">
            <button 
              type="button" 
              onClick={() => setIsAddModalOpen(false)}
              className="px-6 py-4 bg-[#141414] border border-white/10 rounded-xl font-bold text-xs uppercase tracking-wider hover:bg-white/5 transition-all text-[#F5F2ED]"
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className="px-8 py-4 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]"
            >
              {editingType ? 'Update Type' : 'Save Room Type'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
