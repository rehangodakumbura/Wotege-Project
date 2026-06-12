import { motion } from 'motion/react';
import { useState } from 'react';
import { Settings as SettingsIcon, Save, Bell, Lock, Palette, Globe } from 'lucide-react';

export default function Settings() {
  const [activeTab, setActiveTab] = useState('general');

  const tabs = [
    { id: 'general', label: 'General', icon: SettingsIcon },
    { id: 'appearance', label: 'Appearance', icon: Palette },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'security', label: 'Security', icon: Lock },
    { id: 'localization', label: 'Localization', icon: Globe },
  ];

  return (
    <div className="h-full flex flex-col gap-8 pb-4">
      <header className="flex items-center justify-between shrink-0">
        <div>
           <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Settings</span></h1>
           <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">System Configuration</p>
        </div>
        <button className="px-6 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center">
            <Save className="w-4 h-4 mr-2" /> Save Changes
        </button>
      </header>
      
      <div className="flex gap-8 flex-1 overflow-hidden">
         <div className="w-64 shrink-0 flex flex-col gap-2">
            {tabs.map(tab => (
               <button
                 key={tab.id}
                 onClick={() => setActiveTab(tab.id)}
                 className={`flex items-center gap-3 w-full text-left px-5 py-4 rounded-2xl text-sm font-medium transition-all ${
                    activeTab === tab.id
                      ? 'bg-[#141414] border border-wotege-gold/50 text-wotege-gold shadow-[0_4px_20px_rgba(197,160,89,0.1)]'
                      : 'border border-transparent text-white/40 hover:text-[#F5F2ED] hover:bg-white/5'
                 }`}
               >
                 <tab.icon className="w-5 h-5 shrink-0" />
                 {tab.label}
               </button>
            ))}
         </div>
         
         <div className="flex-1 bg-[#111] border border-white/5 rounded-[2rem] p-8 overflow-y-auto no-scrollbar">
            {activeTab === 'general' && (
              <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6 max-w-2xl">
                 <h3 className="text-lg font-serif text-[#F5F2ED] border-b border-white/5 pb-4">General Settings</h3>
                 
                 <div className="space-y-4">
                    <div>
                       <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Hotel Name</label>
                       <input type="text" defaultValue="WOTEGE Hotel & Restaurant" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
                    </div>
                    <div>
                       <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Branch</label>
                       <input type="text" defaultValue="Dubai Marina" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
                    </div>
                    <div>
                       <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Contact Email</label>
                       <input type="email" defaultValue="admin@wotege.com" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none" />
                    </div>
                 </div>
              </motion.div>
            )}

            {activeTab === 'appearance' && (
              <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6 max-w-2xl">
                 <h3 className="text-lg font-serif text-[#F5F2ED] border-b border-white/5 pb-4">Appearance</h3>
                 <div>
                    <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-4">Theme</label>
                    <div className="flex gap-4">
                       <button className="flex-1 bg-[#141414] border border-wotege-gold rounded-xl p-4 flex flex-col items-center gap-2 text-wotege-gold">
                          <div className="w-10 h-10 rounded-full bg-black border border-white/10"></div>
                          <span className="text-xs font-bold uppercase">Dark Luxury</span>
                       </button>
                       <button className="flex-1 bg-[#141414] border border-white/10 rounded-xl p-4 flex flex-col items-center gap-2 text-white/40 hover:text-white transition-colors opacity-50 cursor-not-allowed hidden" title="Coming soon">
                          <div className="w-10 h-10 rounded-full bg-[#F5F2ED] border border-black/10"></div>
                          <span className="text-xs font-bold uppercase">Light Luxury</span>
                       </button>
                    </div>
                 </div>
              </motion.div>
            )}

            {/* Placeholder for other tabs */}
            {(activeTab !== 'general' && activeTab !== 'appearance') && (
              <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="text-white/40 text-sm">
                 Settings for {tabs.find(t => t.id === activeTab)?.label} will be available in future updates.
              </motion.div>
            )}
         </div>
      </div>
    </div>
  );
}
