import { motion } from 'motion/react';
import { Plus, Search, Image as ImageIcon, Filter, Edit, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { menuItemsData, menuCategories } from '@/data/mockData';
import { Modal } from '@/components/ui/Modal';
import { ImageUpload } from '@/components/ui/ImageUpload';

export default function MenuManagement() {
  const [filterCat, setFilterCat] = useState('All');
  const [search, setSearch] = useState('');
  const [isAddItemOpen, setIsAddItemOpen] = useState(false);

  const filteredItems = menuItemsData.filter(item => {
    if (filterCat !== 'All' && filterCat !== 'All Items' && item.category !== filterCat) return false;
    if (search && !item.name.toLowerCase().includes(search.toLowerCase()) && !item.id.toLowerCase().includes(search.toLowerCase())) return false;
    return true;
  });

  return (
    <div className="h-full flex flex-col gap-8">
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Kitchen</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Menu & Inventory Creation</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search items..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button onClick={() => setIsAddItemOpen(true)} className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center w-full sm:w-auto justify-center">
            <Plus className="w-4 h-4 mr-1" /> Add Menu Item
          </button>
        </div>
      </header>

      <div className="flex flex-col md:flex-row gap-6 h-full min-h-0">
        {/* Categories Sidebar */}
        <div className="w-full md:w-64 bg-[#111] border border-white/5 rounded-[2rem] p-6 shrink-0 flex flex-col">
          <h4 className="text-sm font-bold uppercase tracking-widest text-white/80 mb-6">Categories</h4>
          <div className="space-y-2 overflow-y-auto no-scrollbar pb-4 flex-1">
            {menuCategories.map((cat) => (
              <button 
                key={cat}
                onClick={() => setFilterCat(cat)}
                className={`w-full text-left px-4 py-3 rounded-xl text-sm font-medium transition-all ${
                  (filterCat === cat || (cat === 'All' && filterCat === 'All Items'))
                    ? 'bg-wotege-gold/10 text-wotege-gold border border-wotege-gold/20' 
                    : 'text-white/60 hover:text-[#F5F2ED] hover:bg-white/5'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
          <button className="w-full mt-4 py-3 border border-dashed border-white/10 rounded-xl text-[10px] font-bold uppercase tracking-widest text-white/40 hover:text-wotege-gold hover:border-wotege-gold/30 transition-all flex items-center justify-center">
             <Plus className="w-3 h-3 mr-1" /> New Category
          </button>
        </div>

        {/* Menu Items List */}
        <div className="flex-1 bg-[#111] border border-white/5 rounded-[2rem] p-6 flex flex-col overflow-hidden">
          <div className="flex justify-between items-center mb-6">
            <h4 className="text-sm font-bold uppercase tracking-widest text-white/80">Menu Items</h4>
            <div className="flex space-x-2">
               <button className="p-2 bg-white/5 rounded-lg text-white/40 hover:text-white transition-colors">
                  <Filter className="w-4 h-4" />
               </button>
            </div>
          </div>

          <div className="flex-1 overflow-y-auto pr-2 no-scrollbar">
            <div className="space-y-3">
              {filteredItems.map((item, i) => (
                <motion.div 
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: i * 0.05 }}
                  key={item.id}
                  className="flex items-center gap-4 p-4 bg-white/5 rounded-2xl border border-transparent hover:border-white/10 transition-colors group"
                >
                  <div className="w-16 h-16 rounded-xl bg-[#141414] border border-white/5 flex items-center justify-center shrink-0">
                    <ImageIcon className="w-6 h-6 text-white/20" />
                  </div>
                  
                  <div className="flex-1 min-w-0 flex flex-col justify-center">
                    <div className="flex items-center gap-3 mb-1">
                      <h4 className="text-base font-medium text-[#F5F2ED] truncate">{item.name}</h4>
                      <span className={`px-2 py-0.5 text-[10px] uppercase font-bold tracking-widest rounded bg-[#141414] border ${
                        item.status === 'Available' ? 'text-green-400 border-green-400/20' : 'text-red-400 border-red-400/20'
                      }`}>
                        {item.status}
                      </span>
                    </div>
                    <div className="text-[10px] text-white/40 uppercase tracking-widest flex items-center gap-3">
                      <span>{item.category}</span>
                      <span className="w-1 h-1 rounded-full bg-white/20"></span>
                      <span>{item.id}</span>
                      <span className="w-1 h-1 rounded-full bg-white/20"></span>
                      <span>{item.orders} Orders</span>
                    </div>
                  </div>

                  <div className="text-lg font-serif text-wotege-gold shrink-0">
                    LKR {item.price.toLocaleString()}
                  </div>

                  <div className="flex items-center gap-2 ml-4 opacity-0 group-hover:opacity-100 transition-opacity shrink-0">
                     <button className="p-2 text-white/40 hover:text-wotege-gold transition-colors bg-[#141414] rounded-lg border border-white/5">
                        <Edit className="w-4 h-4" />
                     </button>
                     <button className="p-2 text-white/40 hover:text-red-400 transition-colors bg-[#141414] rounded-lg border border-white/5">
                        <Trash2 className="w-4 h-4" />
                     </button>
                  </div>
                </motion.div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <Modal isOpen={isAddItemOpen} onClose={() => setIsAddItemOpen(false)} title="Add Menu Item">
         <form onSubmit={(e) => { e.preventDefault(); setIsAddItemOpen(false); }} className="space-y-4">
            <ImageUpload onUpload={(file) => console.log(file)} label="Item Photo" />
            
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Item Name</label>
               <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Category</label>
               <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                 {menuCategories.filter(c => c !== 'All').map(c => <option key={c}>{c}</option>)}
               </select>
            </div>
            <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Price (LKR)</label>
               <input type="number" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
            </div>
            <button type="submit" className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all">Add Item</button>
         </form>
      </Modal>
    </div>
  );
}
