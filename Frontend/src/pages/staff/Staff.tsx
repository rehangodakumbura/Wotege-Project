import { motion } from 'motion/react';
import { Search, Filter, Plus, MoreHorizontal, Mail, Phone, Sun, Moon } from 'lucide-react';
import { useState } from 'react';
import { staffData } from '@/data/mockData';
import { Modal } from '@/components/ui/Modal';
import { ImageUpload } from '@/components/ui/ImageUpload';
import { FileUpload } from '@/components/ui/FileUpload';

export default function Staff() {
  const [search, setSearch] = useState('');
  const [filterRole, setFilterRole] = useState('All Staff');
  const [isAddStaffOpen, setIsAddStaffOpen] = useState(false);

  const roles = ['All Staff', 'Management', 'Kitchen', 'Front Desk', 'Maintenance', 'Restaurant'];

  const filteredStaff = staffData.filter(s => {
    if (filterRole !== 'All Staff' && s.department !== filterRole) return false;
    if (search && !s.name.toLowerCase().includes(search.toLowerCase()) && !s.role.toLowerCase().includes(search.toLowerCase())) return false;
    return true;
  });

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Human Resources</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Staff & Roles Management</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0">
          <div className="relative group flex items-center flex-1 min-w-[200px]">
            <Search className="absolute left-3 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold transition-colors" />
            <input 
              type="text" 
              placeholder="Search staff..." 
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
          <button className="p-2.5 bg-[#141414] border border-white/5 rounded-full text-white/40 hover:text-white transition-colors">
            <Filter className="w-5 h-5" />
          </button>
          <button onClick={() => setIsAddStaffOpen(true)} className="px-4 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center w-full sm:w-auto justify-center">
            <Plus className="w-4 h-4 mr-1" /> Add Staff
          </button>
        </div>
      </header>
      
      {/* Filters */}
      <div className="hidden md:flex items-center space-x-2 overflow-x-auto pb-2 shrink-0">
        {roles.map((role) => (
          <button 
            key={role}
            onClick={() => setFilterRole(role)}
            className={`px-5 py-2 rounded-full text-xs font-bold uppercase tracking-wider whitespace-nowrap transition-all ${
              filterRole === role 
                ? 'bg-[#141414] border border-wotege-gold text-wotege-gold shadow-[0_0_10px_rgba(197,160,89,0.2)]' 
                : 'bg-[#111] border border-white/5 text-white/40 hover:text-wotege-gold'
            }`}
          >
            {role}
          </button>
        ))}
      </div>

      {/* Staff Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6 auto-rows-min overflow-y-auto pr-2 no-scrollbar">
        {filteredStaff.map((person, i) => (
          <motion.div 
            key={person.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="bg-[#111] border border-white/5 rounded-[2rem] p-6 relative group hover:border-white/10 transition-colors"
          >
            <div className="absolute top-4 right-4 text-white/30 hover:text-white cursor-pointer p-2 rounded-full hover:bg-white/5 transition-colors">
              <MoreHorizontal className="w-5 h-5" />
            </div>

            <div className="flex items-center gap-4 mb-6">
              <div className="w-16 h-16 rounded-full bg-[#141414] border border-white/10 overflow-hidden shrink-0 flex items-center justify-center font-serif text-2xl text-wotege-gold">
                {person.name.charAt(0)}
              </div>
              <div>
                <h3 className="text-lg font-medium text-[#F5F2ED]">{person.name}</h3>
                <p className="text-xs text-wotege-gold uppercase tracking-widest font-bold mt-1">{person.role}</p>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 mb-6">
               <div>
                  <p className="text-[10px] text-white/40 uppercase tracking-widest mb-1">Status</p>
                  <span className={`px-2 py-0.5 text-[10px] uppercase font-bold tracking-widest rounded bg-[#141414] border inline-block ${
                    person.status === 'Active' ? 'text-green-400 border-green-400/20' : 'text-orange-400 border-orange-400/20'
                  }`}>
                    {person.status}
                  </span>
               </div>
               <div>
                  <p className="text-[10px] text-white/40 uppercase tracking-widest mb-1">Shift</p>
                  <div className="flex items-center text-sm text-[#F5F2ED]">
                    {person.shift === 'Night' ? <Moon className="w-4 h-4 mr-1.5 text-blue-400" /> : <Sun className="w-4 h-4 mr-1.5 text-wotege-gold" />}
                    {person.shift}
                  </div>
               </div>
            </div>

            <div className="flex gap-2">
              <button className="flex-1 py-2.5 bg-[#141414] border border-white/5 hover:border-white/20 transition-colors rounded-xl flex items-center justify-center text-white/60 hover:text-white">
                <Mail className="w-4 h-4" />
              </button>
              <button className="flex-1 py-2.5 bg-[#141414] border border-white/5 hover:border-white/20 transition-colors rounded-xl flex items-center justify-center text-white/60 hover:text-white">
                <Phone className="w-4 h-4" />
              </button>
            </div>
          </motion.div>
        ))}
      </div>

      <Modal isOpen={isAddStaffOpen} onClose={() => setIsAddStaffOpen(false)} title="Staff Profile Details">
         <form onSubmit={(e) => { e.preventDefault(); setIsAddStaffOpen(false); }} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-4">
                <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Personal Information</h4>
                <ImageUpload onUpload={(f) => console.log(f)} label="Profile Photo" />
                <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Full Name</label>
                   <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
                </div>
                <div className="grid grid-cols-2 gap-4">
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Phone</label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
                   </div>
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Email</label>
                     <input type="email" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
                   </div>
                   <div className="col-span-2">
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">NIC / Passport</label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
                   </div>
                   <div className="col-span-2">
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Emergency Contact</label>
                     <input type="text" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
                   </div>
                </div>
              </div>

              <div className="space-y-4">
                <h4 className="text-lg font-serif text-wotege-gold border-b border-white/5 pb-2">Employment Details</h4>
                <div className="grid grid-cols-2 gap-4">
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Role</label>
                     <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                       <option>Receptionist</option>
                       <option>Chef</option>
                       <option>Manager</option>
                       <option>Housekeeping</option>
                     </select>
                   </div>
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Department</label>
                     <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                       {roles.filter(r => r !== 'All Staff').map(r => <option key={r}>{r}</option>)}
                     </select>
                   </div>
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Branch</label>
                     <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none">
                       <option>Main Branch</option>
                       <option>Beach Resort</option>
                     </select>
                   </div>
                   <div>
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Base Salary (LKR)</label>
                     <input type="number" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
                   </div>
                   <div className="col-span-2">
                     <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Joining Date</label>
                     <input type="date" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" required />
                   </div>
                   <div className="col-span-2">
                      <FileUpload onUpload={(f) => console.log(f)} label="Upload Contract / Docs" />
                   </div>
                </div>
              </div>
            </div>

            <button type="submit" className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all">Save Staff Member</button>
         </form>
      </Modal>
    </div>
  );
}
