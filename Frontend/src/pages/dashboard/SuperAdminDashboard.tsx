import { motion } from 'motion/react';
import { 
  TrendingUp, 
  Users, 
  BedDouble, 
  Utensils, 
  ArrowUpRight, 
  ArrowDownRight,
  MoreHorizontal
} from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useNavigate } from 'react-router-dom';

const revenueData = [
  { name: 'Mon', hotel: 400000, restaurant: 240000 },
  { name: 'Tue', hotel: 300000, restaurant: 139800 },
  { name: 'Wed', hotel: 200000, restaurant: 980000 },
  { name: 'Thu', hotel: 278000, restaurant: 390800 },
  { name: 'Fri', hotel: 189000, restaurant: 480000 },
  { name: 'Sat', hotel: 239000, restaurant: 380000 },
  { name: 'Sun', hotel: 349000, restaurant: 430000 },
];

function StatCard({ title, value, trend, isPositive, icon: Icon, delay, isDark }: any) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay, duration: 0.5, ease: 'easeOut' }}
      className={`rounded-[2rem] p-6 relative overflow-hidden group transition-all flex flex-col justify-between ${
        isDark ? 'bg-[#111] border border-white/5' : 'bg-wotege-gold text-black border border-wotege-gold/50'
      }`}
    >
      <div className="flex justify-between items-start mb-6">
        <div>
          <p className={`text-xs uppercase tracking-[0.2em] mb-2 ${isDark ? 'text-white/40' : 'text-black/60 font-bold'}`}>{title}</p>
          <div className="flex items-center gap-2">
             <div className="text-4xl font-serif font-bold">{value}</div>
          </div>
        </div>
        <div className={`p-2 rounded-2xl transition-colors ${isDark ? 'bg-white/5 text-wotege-gold' : 'bg-black/10 text-black'}`}>
          <Icon className="w-5 h-5" />
        </div>
      </div>
      
      <div className={`flex items-end justify-between text-sm ${isDark ? 'text-gray-500' : 'text-black/80 font-medium'}`}>
        <span className={`flex items-center ${
          isDark 
            ? (isPositive ? 'text-green-400' : 'text-red-400') 
            : (isPositive ? 'text-black font-bold' : 'text-red-800')
        }`}>
          {isPositive ? <ArrowUpRight className="w-4 h-4 mr-1" /> : <ArrowDownRight className="w-4 h-4 mr-1" />}
          {trend} Today
        </span>
        <span className={isDark ? 'text-white/30' : 'text-black/50'}>vs last week</span>
      </div>
      
      {isDark && <div className="absolute top-0 right-0 w-32 h-32 bg-wotege-gold/5 rounded-full blur-2xl group-hover:bg-wotege-gold/10 transition-colors pointer-events-none" />}
    </motion.div>
  );
}

export default function SuperAdminDashboard() {
  const [isNewBookingOpen, setIsNewBookingOpen] = useState(false);
  const navigate = useNavigate();

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Executive Suite</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Super Admin • Dubai Marina Branch</p>
        </div>
        
        <div className="flex flex-wrap items-center gap-3 mt-4 md:mt-0 w-full md:w-auto">
          <button onClick={() => alert("Report downloaded!")} className="flex-1 md:flex-none px-5 py-2.5 bg-[#141414] border border-white/5 rounded-full text-xs font-bold uppercase tracking-wider hover:border-wotege-gold/50 transition-colors whitespace-nowrap">
            Download Report
          </button>
          <button onClick={() => setIsNewBookingOpen(true)} className="flex-1 md:flex-none px-5 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all whitespace-nowrap">
            New Booking
          </button>
        </div>
      </header>

      {/* Bento Dashboard Grid */}
      <div className="flex-1 grid grid-cols-1 md:grid-cols-12 gap-6 auto-rows-min">
        
        {/* Main Revenue Analytics */}
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1, duration: 0.5 }}
          className="md:col-span-12 xl:col-span-8 bg-[#111] rounded-[2rem] border border-white/5 p-8 relative flex flex-col"
        >
          <div className="absolute top-0 right-0 w-1/2 h-full bg-gradient-to-l from-wotege-gold/5 to-transparent pointer-events-none" />
          <div className="flex justify-between items-start z-10">
            <div>
              <p className="text-xs uppercase tracking-[0.2em] text-white/40 mb-2">Total Gross Revenue</p>
              <h2 className="text-5xl font-light font-serif">LKR 2,425,590.<span className="opacity-40">00</span></h2>
              <div className="flex items-center gap-2 mt-4">
                <span className="text-wotege-gold text-xs font-bold px-2 py-0.5 bg-wotege-gold/10 rounded-full">+12.4%</span>
                <span className="text-white/30 text-xs">vs. previous quarter</span>
              </div>
            </div>
            <div className="flex gap-2">
              <button className="px-4 py-1.5 rounded-full border border-white/10 text-xs hover:border-wotege-gold/50 transition-all font-medium">Weekly</button>
              <button className="px-4 py-1.5 rounded-full bg-white/5 border border-white/20 text-xs font-medium">Monthly</button>
            </div>
          </div>

          <div className="h-[250px] w-full mt-8 z-10 w-full overflow-hidden">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={revenueData} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorHotel" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#C5A059" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#C5A059" stopOpacity={0}/>
                  </linearGradient>
                  <linearGradient id="colorRest" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#ffffff" stopOpacity={0.1}/>
                    <stop offset="95%" stopColor="#ffffff" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" vertical={false} />
                <XAxis dataKey="name" stroke="#6b7280" tick={{fill: '#6b7280', fontSize: 12}} axisLine={false} tickLine={false} />
                <YAxis stroke="#6b7280" tick={{fill: '#6b7280', fontSize: 12}} axisLine={false} tickLine={false} tickFormatter={(value) => `LKR ${value/1000}k`} />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#141414', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '16px' }}
                  itemStyle={{ color: '#F5F2ED' }}
                />
                <Area type="monotone" dataKey="hotel" stroke="#C5A059" strokeWidth={3} fillOpacity={1} fill="url(#colorHotel)" />
                <Area type="monotone" dataKey="restaurant" stroke="#ffffff" strokeWidth={2} fillOpacity={1} fill="url(#colorRest)" strokeDasharray="4 4" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </motion.div>

        {/* Key Metrics Right Panel */}
        <div className="md:col-span-12 xl:col-span-4 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-1 gap-6">
          <StatCard title="Room Occupancy" value="92.4%" trend="+4.2%" isPositive={true} icon={BedDouble} delay={0.2} isDark={false} />
          
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3, duration: 0.5, ease: 'easeOut' }}
            className="bg-[#111] border border-white/5 rounded-[2rem] p-6 flex flex-col justify-between"
          >
           <div className="flex justify-between items-start mb-6">
             <p className="text-xs uppercase tracking-widest text-white/40 font-bold">Restaurant Load</p>
             <span className="px-2 py-0.5 bg-red-500/10 text-red-400 text-[10px] rounded uppercase font-bold">Peak Hour</span>
           </div>
           <div>
             <div className="flex items-center gap-4">
               <h3 className="text-4xl font-serif font-light">42 <span className="text-lg opacity-40 font-sans">/ 50</span></h3>
               <div className="flex-1 h-1.5 bg-white/10 rounded-full overflow-hidden">
                 <div className="h-full bg-red-400 w-[84%]"></div>
               </div>
             </div>
             <p className="text-xs text-white/30 mt-4 italic">Average wait time: 12 mins</p>
           </div>
          </motion.div>
        </div>

        {/* Live Activity Feed */}
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4, duration: 0.5 }}
          className="md:col-span-12 xl:col-span-5 bg-[#111] rounded-[2rem] border border-white/5 p-6 flex flex-col h-[400px]"
        >
          <div className="flex justify-between items-center mb-6">
            <h4 className="text-sm font-bold uppercase tracking-widest text-white/80">Recent Activity</h4>
            <span className="text-wotege-gold text-[10px] border-b border-wotege-gold/20 cursor-pointer uppercase tracking-widest font-bold">View History</span>
          </div>
          
          <div className="flex-1 overflow-y-auto pr-2 space-y-3 no-scrollbar">
            {[
              { time: 'Just now', action: 'New Room Booking', detail: 'Suite 402 - 3 Nights', amount: '+LKR 120,000', type: 'hotel', init: 'NB' },
              { time: '2 min ago', action: 'Restaurant POS', detail: 'Table 14 settled bill', amount: '+LKR 14,500', type: 'restaurant', init: 'RP' },
              { time: '15 min ago', action: 'Room Service', detail: 'Order #482 to Room 205', amount: '+LKR 4,500', type: 'restaurant', init: 'RS' },
              { time: '1 hr ago', action: 'Check-out', detail: 'Room 304 - Mr. Smith', type: 'info', init: 'CO' },
              { time: '2 hrs ago', action: 'Stock Alert', detail: 'Premium Champagne low', type: 'alert', init: 'SA' },
            ].map((item, i) => (
              <div key={i} className="flex items-center gap-4 p-3 bg-white/5 rounded-2xl hover:bg-white/10 transition-colors cursor-pointer group border border-transparent hover:border-white/5">
                <div className={`w-10 h-10 rounded-full flex items-center justify-center text-xs font-bold shrink-0 ${
                  item.type === 'hotel' ? 'bg-wotege-gold/20 text-wotege-gold' : 
                  item.type === 'restaurant' ? 'bg-blue-400/20 text-blue-400' :
                  item.type === 'alert' ? 'bg-red-400/20 text-red-400' : 'bg-white/10 text-white/60'
                }`}>
                  {item.init}
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex justify-between items-start">
                    <p className="text-sm font-medium text-[#F5F2ED] truncate">{item.action}</p>
                    {item.amount && <span className="text-sm font-medium text-wotege-gold ml-2">{item.amount}</span>}
                  </div>
                  <p className="text-[10px] text-white/40 uppercase tracking-tighter mt-1">{item.detail} • {item.time}</p>
                </div>
              </div>
            ))}
          </div>
        </motion.div>

        {/* Existing StatCards remapped */}
        <div className="md:col-span-12 xl:col-span-7 grid grid-cols-1 md:grid-cols-2 gap-6">
          <StatCard title="Active Guests" value="234" trend="+5.2%" isPositive={true} icon={Users} delay={0.5} isDark={true} />
          <StatCard title="Daily Rest. Sales" value="LKR 824,000" trend="+18.4%" isPositive={true} icon={Utensils} delay={0.6} isDark={true} />
          
          {/* Quick Action Bento Box */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.7, duration: 0.5 }}
            onClick={() => navigate('/pos')}
            className="md:col-span-2 bg-gradient-to-br from-[#1A1A1A] to-[#111] border border-wotege-gold/20 rounded-[2rem] p-6 flex items-center justify-between group cursor-pointer"
          >
             <div>
               <h4 className="text-2xl font-serif mb-1">Launch Quick POS</h4>
               <p className="text-xs text-white/40 font-sans">Open terminal for walk-in orders</p>
             </div>
             <div className="w-14 h-14 bg-wotege-gold/10 rounded-full flex items-center justify-center group-hover:scale-110 transition-transform">
               <ArrowUpRight className="w-6 h-6 text-wotege-gold" />
             </div>
          </motion.div>
        </div>

      </div>

      <Modal isOpen={isNewBookingOpen} onClose={() => setIsNewBookingOpen(false)} title="New Booking">
         <form onSubmit={(e) => { e.preventDefault(); setIsNewBookingOpen(false); }} className="space-y-4">
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
            <button type="submit" className="w-full py-4 mt-6 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all">Confirm Booking</button>
         </form>
      </Modal>
    </div>
  );
}
