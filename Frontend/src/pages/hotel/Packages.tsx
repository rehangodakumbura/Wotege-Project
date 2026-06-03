import { motion } from 'motion/react';
import { Plus, Search, Filter, Package as PackageIcon, Edit, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { ImageUpload } from '@/components/ui/ImageUpload';

const initialPackages = [
  { id: 'PKG-001', name: 'Weekend Chill Package', price: 75000, category: 'Stay', status: 'Active', bookings: 45, image: 'https://images.unsplash.com/photo-1571896349842-33c89424de2d?q=80&w=600&auto=format&fit=crop' },
  { id: 'PKG-002', name: 'BBQ Night Special', price: 12000, category: 'Dining', status: 'Active', bookings: 128, image: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?q=80&w=600&auto=format&fit=crop' },
  { id: 'PKG-003', name: 'Romantic Honeymoon', price: 150000, category: 'Stay', status: 'Active', bookings: 12, image: 'https://images.unsplash.com/photo-1622396481328-9b1b78cdd9fd?q=80&w=600&auto=format&fit=crop' },
  { id: 'PKG-004', name: 'Family Combo Deal', price: 45000, category: 'Mixed', status: 'Inactive', bookings: 89, image: 'https://images.unsplash.com/photo-1542314831-c6a4d14faaf2?q=80&w=600&auto=format&fit=crop' },
];

export default function Packages() {
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('All');
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [packages] = useState(initialPackages);

  const filteredPackages = packages.filter(pkg => {
    if (filter !== 'All' && pkg.category !== filter) return false;
    if (search && !pkg.name.toLowerCase().includes(search.toLowerCase())) return false;
    return true;
  });

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Offers</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Package Management</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search packages..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button className="p-2.5 bg-[#141414] border border-white/5 rounded-full text-white/40 hover:text-white transition-colors">
            <Filter className="w-5 h-5" />
          </button>
          <button onClick={() => setIsAddOpen(true)} className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center w-full sm:w-auto justify-center">
            <Plus className="w-4 h-4 mr-1" /> Add Package
          </button>
        </div>
      </header>

      {/* Filters */}
      <div className="hidden md:flex items-center space-x-2 overflow-x-auto pb-2 shrink-0">
        {['All', 'Stay', 'Dining', 'Mixed', 'Events'].map((cat) => (
          <button 
            key={cat}
            onClick={() => setFilter(cat)}
            className={`px-5 py-2 rounded-full text-xs font-bold uppercase tracking-wider whitespace-nowrap transition-all ${
              filter === cat 
                ? 'bg-[#141414] border border-wotege-gold text-wotege-gold shadow-[0_0_10px_rgba(197,160,89,0.2)]' 
                : 'bg-[#111] border border-white/5 text-white/40 hover:text-wotege-gold'
            }`}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 overflow-y-auto pr-2 pb-4 no-scrollbar">
        {filteredPackages.map((pkg, i) => (
          <motion.div
            key={pkg.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="bg-[#111] border border-white/5 rounded-[2rem] overflow-hidden group hover:border-white/10 transition-colors flex flex-col"
          >
            <div className="relative h-48 overflow-hidden shrink-0">
              <img src={pkg.image} alt={pkg.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
              <div className="absolute inset-0 bg-gradient-to-t from-black/80 to-transparent"></div>
              <div className="absolute top-4 right-4 flex gap-2">
                <span className={`px-3 py-1 rounded-full text-[10px] uppercase font-bold tracking-widest backdrop-blur-md ${
                  pkg.status === 'Active' ? 'bg-green-500/20 text-green-300' : 'bg-red-500/20 text-red-300'
                }`}>
                  {pkg.status}
                </span>
              </div>
              <div className="absolute bottom-4 left-4">
                <span className="px-3 py-1 rounded-full bg-white/10 backdrop-blur-md text-white text-[10px] uppercase font-bold tracking-widest">
                  {pkg.category}
                </span>
              </div>
            </div>
            
            <div className="p-6 flex-1 flex flex-col">
              <h3 className="text-xl font-serif text-[#F5F2ED] mb-2">{pkg.name}</h3>
              <div className="text-wotege-gold font-serif text-2xl mb-4">
                LKR {pkg.price.toLocaleString()}
              </div>
              
              <div className="mt-auto pt-4 border-t border-white/5 flex items-center justify-between">
                <div className="text-xs text-white/40">
                  <span className="text-white">{pkg.bookings}</span> total bookings
                </div>
                <div className="flex gap-2">
                  <button className="p-2 bg-white/5 rounded-lg text-white/60 hover:text-wotege-gold transition-colors">
                    <Edit className="w-4 h-4" />
                  </button>
                  <button className="p-2 bg-white/5 rounded-lg text-white/60 hover:text-red-400 transition-colors">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>
          </motion.div>
        ))}
      </div>

      <Modal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} title="Create New Package">
         <form onSubmit={(e) => { e.preventDefault(); setIsAddOpen(false); }} className="space-y-6">
            <ImageUpload onUpload={(file) => console.log(file)} label="Package Cover Image" />
            
            <div className="grid grid-cols-2 gap-4">
               <div className="col-span-2">
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Package Name</label>
                 <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
               </div>
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Category</label>
                 <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                   <option>Stay</option>
                   <option>Dining</option>
                   <option>Events</option>
                   <option>Mixed</option>
                 </select>
               </div>
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Price (LKR)</label>
                 <input type="number" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
               </div>
               <div className="col-span-2">
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Included Items / Features (Press Enter to add)</label>
                 <textarea rows={3} className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" placeholder="- 1 Night Stay in Deluxe Suite&#10;- Dinner for Two&#10;- Spa Access" required></textarea>
               </div>
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Validity Starts</label>
                 <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
               </div>
               <div>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Validity Ends</label>
                 <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
               </div>
            </div>
            
            <button type="submit" className="w-full py-4 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all">Publish Package</button>
         </form>
      </Modal>
    </div>
  );
}
