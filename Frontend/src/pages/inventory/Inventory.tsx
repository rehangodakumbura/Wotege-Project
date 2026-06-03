import { motion } from 'motion/react';
import { Package, AlertTriangle, TrendingUp, Filter, Search, Plus, ArrowDownUp } from 'lucide-react';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';

const mockInventory = [
  { id: 'INV-001', item: 'Wagyu Beef A5', category: 'Kitchen', stock: 12, unit: 'kg', minThreshold: 10, status: 'Low Stock', lastRestock: '2 Days ago' },
  { id: 'INV-002', item: 'Dom Perignon 2012', category: 'Bar', stock: 45, unit: 'bottles', minThreshold: 15, status: 'In Stock', lastRestock: '1 Week ago' },
  { id: 'INV-003', item: 'Premium Bed Linens', category: 'Housekeeping', stock: 120, unit: 'sets', minThreshold: 50, status: 'In Stock', lastRestock: '1 Month ago' },
  { id: 'INV-004', item: 'Truffle Oil', category: 'Kitchen', stock: 2, unit: 'liters', minThreshold: 5, status: 'Critical', lastRestock: '3 Weeks ago' },
  { id: 'INV-005', item: 'Spa Signature Oils', category: 'Spa', stock: 8, unit: 'bottles', minThreshold: 10, status: 'Low Stock', lastRestock: '2 Weeks ago' },
];

export default function Inventory() {
  const { toast } = useToast();
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('All');
  const [isAddStockOpen, setIsAddStockOpen] = useState(false);

  const handleAddStock = (e: React.FormEvent) => {
    e.preventDefault();
    setIsAddStockOpen(false);
    toast('New stock added successfully to inventory', 'success');
  };

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Stock</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Inventory Management</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search inventory..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button className="px-4 py-2.5 bg-[#141414] text-[#F5F2ED] border border-white/10 rounded-full text-xs font-bold uppercase tracking-wider hover:border-wotege-gold/50 transition-all flex items-center w-full sm:w-auto justify-center">
            <ArrowDownUp className="w-4 h-4 mr-1.5" /> Purchase Order
          </button>
          <button 
            onClick={() => setIsAddStockOpen(true)}
            className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center w-full sm:w-auto justify-center shrink-0"
          >
            <Plus className="w-4 h-4 mr-1" /> Add Stock
          </button>
        </div>
      </header>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 shrink-0">
        <div className="bg-[#111] border border-white/5 rounded-[2rem] p-6">
           <div className="flex items-center justify-between mb-4">
              <div className="p-3 bg-white/5 rounded-2xl text-white/60"><Package className="w-5 h-5" /></div>
           </div>
           <div className="text-3xl font-serif text-[#F5F2ED]">1,248</div>
           <div className="text-xs text-white/40 uppercase tracking-widest font-bold mt-2">Total Items</div>
        </div>
        <div className="bg-[#111] border border-red-500/20 rounded-[2rem] p-6 relative overflow-hidden">
           <div className="absolute top-0 right-0 p-6 opacity-10"><AlertTriangle className="w-16 h-16 text-red-500" /></div>
           <div className="flex items-center justify-between mb-4">
              <div className="p-3 bg-red-500/10 rounded-2xl text-red-400"><AlertTriangle className="w-5 h-5" /></div>
           </div>
           <div className="text-3xl font-serif text-[#F5F2ED]">12</div>
           <div className="text-xs text-red-400 uppercase tracking-widest font-bold mt-2">Low Stock Alerts</div>
        </div>
        <div className="bg-[#111] border border-orange-500/20 rounded-[2rem] p-6">
           <div className="flex items-center justify-between mb-4">
              <div className="p-3 bg-orange-500/10 rounded-2xl text-orange-400"><TrendingUp className="w-5 h-5" /></div>
           </div>
           <div className="text-3xl font-serif text-[#F5F2ED]">LKR 4.2M</div>
           <div className="text-xs text-white/40 uppercase tracking-widest font-bold mt-2">Inventory Value</div>
        </div>
        <div className="bg-[#111] border border-white/5 rounded-[2rem] p-6">
           <div className="flex items-center justify-between mb-4">
              <div className="p-3 bg-white/5 rounded-2xl text-white/60"><ArrowDownUp className="w-5 h-5" /></div>
           </div>
           <div className="text-3xl font-serif text-[#F5F2ED]">8</div>
           <div className="text-xs text-white/40 uppercase tracking-widest font-bold mt-2">Pending Orders</div>
        </div>
      </div>

      <div className="bg-[#111] border border-white/5 rounded-[2rem] flex-1 flex flex-col overflow-hidden">
         <div className="p-6 border-b border-white/5 flex items-center justify-between shrink-0">
            <h3 className="text-lg font-serif text-[#F5F2ED]">Current Stock</h3>
            <div className="flex gap-2">
              {['All', 'Kitchen', 'Bar', 'Housekeeping', 'Spa'].map(cat => (
                <button
                  key={cat}
                  onClick={() => setFilter(cat)}
                  className={`px-4 py-2 rounded-full text-xs font-bold uppercase tracking-wider transition-colors ${
                    filter === cat ? 'bg-white/10 text-white' : 'text-white/40 hover:text-white/80'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>
         </div>
         <div className="overflow-x-auto flex-1">
            <table className="w-full text-left border-collapse min-w-[800px]">
              <thead>
                <tr className="border-b border-white/5 bg-[#141414]">
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40">Item ID & Name</th>
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40">Category</th>
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40">Stock Level</th>
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40">Status</th>
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40">Last Restock</th>
                  <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {mockInventory.map((item, i) => (
                  <motion.tr 
                    key={item.id}
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: i * 0.05 }}
                    className="border-b border-white/5 hover:bg-white/5 transition-colors"
                  >
                    <td className="px-6 py-4">
                      <div className="flex flex-col">
                        <span className="text-[#F5F2ED] font-medium">{item.item}</span>
                        <span className="text-xs text-white/40 font-mono mt-1">{item.id}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className="text-sm text-white/60">{item.category}</span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-end gap-2">
                        <span className="text-lg font-serif text-[#F5F2ED]">{item.stock}</span>
                        <span className="text-xs text-white/40 mb-1">{item.unit}</span>
                      </div>
                      <div className="w-24 h-1.5 bg-white/10 rounded-full mt-2 overflow-hidden">
                         <div 
                           className={`h-full rounded-full ${item.stock <= item.minThreshold ? 'bg-red-500' : 'bg-green-500'}`}
                           style={{ width: `${Math.min(100, (item.stock / (item.minThreshold * 3)) * 100)}%` }}
                         />
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className={`px-2.5 py-1 text-[10px] uppercase font-bold tracking-widest rounded-full border inline-block ${
                        item.status === 'In Stock' ? 'text-green-400 border-green-400/20 bg-green-400/10' : 
                        item.status === 'Low Stock' ? 'text-orange-400 border-orange-400/20 bg-orange-400/10' : 
                        'text-red-400 border-red-400/20 bg-red-400/10'
                      }`}>
                        {item.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-white/40">
                      {item.lastRestock}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button className="text-xs font-bold uppercase tracking-widest text-wotege-gold hover:text-white transition-colors">Adjust</button>
                    </td>
                  </motion.tr>
                ))}
              </tbody>
            </table>
         </div>
      </div>

      <Modal isOpen={isAddStockOpen} onClose={() => setIsAddStockOpen(false)} title="Add New Stock Entry">
        <form onSubmit={handleAddStock} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="md:col-span-2">
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Item Name <span className="text-red-500">*</span></label>
              <input type="text" placeholder="e.g. Fresh Salmon" required className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Category <span className="text-red-500">*</span></label>
              <select required className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                <option>Kitchen</option>
                <option>Bar & Beverage</option>
                <option>Housekeeping</option>
                <option>Spa</option>
                <option>Maintenance</option>
              </select>
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Unit of Measurement <span className="text-red-500">*</span></label>
              <select required className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
                <option>kg</option>
                <option>liters</option>
                <option>bottles</option>
                <option>pieces</option>
                <option>boxes</option>
                <option>sets</option>
              </select>
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Initial Stock Quantity <span className="text-red-500">*</span></label>
              <input type="number" min="0" required className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Minimum Threshold <span className="text-red-500">*</span></label>
              <input type="number" min="0" required placeholder="Alert level" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Supplier</label>
              <input type="text" placeholder="Optional" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Unit Cost (LKR)</label>
              <input type="number" min="0" step="0.01" placeholder="Optional" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" />
            </div>
          </div>
          
          <div className="flex justify-end pt-4">
            <button 
              type="submit" 
              className="w-full py-4 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]"
            >
              Add To Inventory
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
