import { motion } from 'motion/react';
import { Search, Mail, Phone, MoreHorizontal, ShieldCheck, Download, Users } from 'lucide-react';
import { useState } from 'react';
import { customersData } from '@/data/mockData';

export default function Customers() {
  const [search, setSearch] = useState('');

  const filteredCustomers = customersData.filter(c =>
    !search || 
    c.name.toLowerCase().includes(search.toLowerCase()) || 
    c.email.toLowerCase().includes(search.toLowerCase()) ||
    c.id.toLowerCase().includes(search.toLowerCase())
  );

  const handleExport = () => {
    alert("Customer data exported to CSV!");
  };

  return (
    <div className="h-full flex flex-col gap-8">
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Loyalty</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Customer Database Control</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search by name, email, or ID..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button onClick={handleExport} className="px-5 py-2.5 bg-[#141414] border border-white/5 rounded-full text-xs font-bold uppercase tracking-wider text-white hover:border-wotege-gold/50 transition-all flex items-center w-full sm:w-auto justify-center">
            <Download className="w-4 h-4 mr-2" /> Export
          </button>
        </div>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 shrink-0">
         <div className="bg-[#111] border border-white/5 p-6 rounded-[2rem] flex items-center justify-between">
           <div>
             <p className="text-[10px] uppercase tracking-widest text-white/40 font-bold mb-2">Total Database</p>
             <h3 className="text-3xl font-serif text-[#F5F2ED]">8,492</h3>
           </div>
           <div className="w-12 h-12 bg-white/5 rounded-2xl flex items-center justify-center text-wotege-gold">
              <Users className="w-6 h-6" />
           </div>
         </div>
         <div className="bg-[#111] border border-white/5 p-6 rounded-[2rem] flex items-center justify-between">
           <div>
             <p className="text-[10px] uppercase tracking-widest text-white/40 font-bold mb-2">Active VIPs</p>
             <h3 className="text-3xl font-serif text-[#F5F2ED]">412</h3>
           </div>
           <div className="w-12 h-12 bg-wotege-gold/10 rounded-2xl flex items-center justify-center text-wotege-gold">
              <ShieldCheck className="w-6 h-6" />
           </div>
         </div>
         <div className="bg-wotege-gold border border-wotege-gold/50 p-6 rounded-[2rem] flex items-center justify-between text-black">
           <div>
             <p className="text-[10px] uppercase tracking-widest font-bold mb-2 opacity-60">Avg LTV</p>
             <h3 className="text-3xl font-serif">LKR 460K</h3>
           </div>
         </div>
      </div>

      <div className="flex-1 bg-[#111] border border-white/5 rounded-[2rem] p-6 flex flex-col overflow-hidden">
        <div className="flex-1 overflow-y-auto no-scrollbar pr-2">
           <div className="min-w-[800px]">
              <div className="grid grid-cols-12 gap-4 py-3 px-4 border-b border-white/5 text-[10px] uppercase tracking-widest font-bold text-white/40 mb-2">
                <div className="col-span-3">Customer</div>
                <div className="col-span-3">Contact</div>
                <div className="col-span-2">Loyalty Tier</div>
                <div className="col-span-2">Total Spent</div>
                <div className="col-span-1 text-center">Visits</div>
                <div className="col-span-1 text-right">Action</div>
              </div>

              <div className="space-y-2">
                {filteredCustomers.map((c, i) => (
                   <motion.div 
                     initial={{ opacity: 0, y: 10 }}
                     animate={{ opacity: 1, y: 0 }}
                     transition={{ delay: i * 0.05 }}
                     key={c.id} 
                     className="grid grid-cols-12 gap-4 py-4 px-4 bg-[#141414] rounded-2xl items-center hover:bg-white/5 transition-colors border border-transparent hover:border-white/5 cursor-pointer"
                   >
                     <div className="col-span-3 flex items-center gap-3">
                       <div className="w-10 h-10 rounded-full bg-[#080808] border border-white/5 flex items-center justify-center shrink-0 font-serif text-wotege-gold text-lg">
                          {c.name.charAt(0)}
                       </div>
                       <div className="min-w-0">
                         <div className="text-sm font-medium text-[#F5F2ED] truncate">{c.name}</div>
                         <div className="text-[10px] text-white/40 font-mono mt-0.5">{c.id}</div>
                       </div>
                     </div>

                     <div className="col-span-3 flex flex-col gap-1.5">
                       <div className="text-xs text-white/60 flex items-center gap-2 truncate">
                         <Mail className="w-3 h-3 text-white/30" /> {c.email}
                       </div>
                       <div className="text-xs text-white/60 flex items-center gap-2 truncate">
                         <Phone className="w-3 h-3 text-white/30" /> {c.phone}
                       </div>
                     </div>

                     <div className="col-span-2">
                        <span className={`px-3 py-1.5 text-[10px] uppercase font-bold tracking-widest rounded-full border bg-black ${
                          c.tier.includes('Platinum') ? 'text-blue-200 border-blue-200/30' :
                          c.tier.includes('Gold') ? 'text-wotege-gold border-wotege-gold/30' :
                          c.tier.includes('Silver') ? 'text-gray-300 border-gray-300/30' :
                          'text-white/60 border-white/10'
                        }`}>
                          {c.tier}
                        </span>
                     </div>

                     <div className="col-span-2 text-sm text-[#F5F2ED] font-serif">{c.spent}</div>
                     
                     <div className="col-span-1 text-center text-sm font-medium text-white/80">{c.visits}</div>

                     <div className="col-span-1 text-right">
                        <button className="p-2 text-white/30 hover:text-white transition-colors hover:bg-white/5 rounded-lg">
                          <MoreHorizontal className="w-5 h-5" />
                        </button>
                     </div>
                   </motion.div>
                ))}
              </div>
           </div>
        </div>
      </div>
    </div>
  );
}
