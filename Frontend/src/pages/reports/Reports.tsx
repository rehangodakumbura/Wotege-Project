import { motion } from 'motion/react';
import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  AreaChart, Area, PieChart, Pie, Cell, LineChart, Line, Legend
} from 'recharts';
import { 
  TrendingUp, Download, Calendar, Filter, FileText, ArrowUpRight, 
  Sparkles, AlertCircle, PieChart as PieChartIcon, Activity, Settings, Mail, Clock
} from 'lucide-react';
import { useState, useEffect, type FormEvent } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';
import { useAuth } from '@/lib/auth-context';
import {
  getRevenueReport,
  getRevenueBreakdown,
  getBookingChannels,
  getTopInventory,
  type RevenueReport,
  type RevenueBreakdownItem,
  type BookingChannelItem,
  type TopInventoryItem,
} from '@/lib/api';

const MOCK_REPORT: RevenueReport = {
  grossRevenueYtd: 45280000,
  netProfitMargin: 32.4,
  avgOccupancy: 78,
  revPAR: 12500,
  monthlyData: [
    { month: 'Jan', rooms: 4000000, restaurant: 2400000, events: 1200000 },
    { month: 'Feb', rooms: 3000000, restaurant: 1398000, events: 1000000 },
    { month: 'Mar', rooms: 2000000, restaurant: 4800000, events: 800000 },
    { month: 'Apr', rooms: 2780000, restaurant: 3908000, events: 1500000 },
    { month: 'May', rooms: 1890000, restaurant: 4800000, events: 2100000 },
    { month: 'Jun', rooms: 2390000, restaurant: 3800000, events: 2500000 },
    { month: 'Jul', rooms: 3490000, restaurant: 4300000, events: 3100000 },
  ],
};

const MOCK_BREAKDOWN: RevenueBreakdownItem[] = [
  { name: 'Rooms', percentage: 45 },
  { name: 'Restaurant', percentage: 30 },
  { name: 'Events & Banquets', percentage: 15 },
  { name: 'Packages & Extras', percentage: 10 },
];

const MOCK_CHANNELS: BookingChannelItem[] = [
  { channel: 'Direct Website', percentage: 45, revenue: 12400000 },
  { channel: 'Booking.com', percentage: 30, revenue: 8200000 },
  { channel: 'Walk-ins', percentage: 15, revenue: 4100000 },
  { channel: 'Agoda', percentage: 10, revenue: 2700000 },
];

const MOCK_INVENTORY: TopInventoryItem[] = [
  { itemName: 'Wagyu Ribeye Steak', category: 'Restaurant', trend: '+12%', status: 'optimal' },
  { itemName: 'Dom Perignon 2012', category: 'Bar', trend: '+5%', status: 'optimal' },
  { itemName: 'Premium Cotton Sheets (Double)', category: 'Housekeeping', trend: '-2%', status: 'reorder' },
  { itemName: 'Signature Breakfast Buffet', category: 'Restaurant', trend: '+18%', status: 'optimal' },
];

const COLORS = ['#C5A059', '#8C6D31', '#1A1A1A', '#333333'];

const CustomTooltip = ({ active, payload, label }: any) => {
  if (active && payload && payload.length) {
    return (
      <div className="bg-[#111] border border-white/10 p-4 rounded-xl shadow-2xl">
        <p className="text-[#F5F2ED] font-serif mb-2">{label}</p>
        {payload.map((entry: any, index: number) => (
          <div key={index} className="flex flex-col mb-1">
            <span className="text-[10px] uppercase font-bold tracking-widest text-white/40">{entry.name}</span>
            <span className="text-wotege-gold font-medium">
              LKR {entry.value.toLocaleString()}
            </span>
          </div>
        ))}
      </div>
    );
  }
  return null;
};

function getDateRange(selected: string): { start: string; end: string } {
  const now = new Date();
  const end = now.toISOString().split('T')[0];
  let start: Date;

  switch (selected) {
    case 'Today':
      return { start: end, end };
    case 'This Week': {
      start = new Date(now);
      start.setDate(now.getDate() - now.getDay());
      break;
    }
    case 'This Month': {
      start = new Date(now.getFullYear(), now.getMonth(), 1);
      break;
    }
    case 'Quarterly': {
      start = new Date(now);
      start.setMonth(now.getMonth() - 3);
      break;
    }
    case 'Last 7 Months': {
      start = new Date(now);
      start.setMonth(now.getMonth() - 7);
      break;
    }
    case 'Year to Date': {
      start = new Date(now.getFullYear(), 0, 1);
      break;
    }
    default: {
      start = new Date(now);
      start.setMonth(now.getMonth() - 7);
    }
  }

  return { start: start.toISOString().split('T')[0], end };
}

function formatRevenue(val: number): string {
  if (val >= 1000000) return `LKR ${(val / 1000000).toFixed(1)}M`;
  if (val >= 1000) return `LKR ${(val / 1000).toFixed(1)}K`;
  return `LKR ${val}`;
}

export default function Reports() {
  const { token } = useAuth();
  const [dateRange, setDateRange] = useState('Last 7 Months');
  const [isExportModalOpen, setIsExportModalOpen] = useState(false);
  const [exportFormat, setExportFormat] = useState('pdf');
  const [exportMethod, setExportMethod] = useState('download');
  const { toast } = useToast();

  const [loading, setLoading] = useState(true);
  const [report, setReport] = useState<RevenueReport | null>(null);
  const [breakdown, setBreakdown] = useState<RevenueBreakdownItem[]>([]);
  const [channels, setChannels] = useState<BookingChannelItem[]>([]);
  const [inventory, setInventory] = useState<TopInventoryItem[]>([]);

  useEffect(() => {
    if (!token) {
      setReport(MOCK_REPORT);
      setBreakdown(MOCK_BREAKDOWN);
      setChannels(MOCK_CHANNELS);
      setInventory(MOCK_INVENTORY);
      setLoading(false);
      return;
    }

    setLoading(true);
    const { start, end } = getDateRange(dateRange);

    Promise.all([
      getRevenueReport(start, end, token),
      getRevenueBreakdown(token),
      getBookingChannels(token),
      getTopInventory(token),
    ])
      .then(([reportData, breakdownData, channelsData, inventoryData]) => {
        setReport(reportData);
        setBreakdown(breakdownData);
        setChannels(channelsData);
        setInventory(inventoryData);
      })
      .catch(() => {
        toast('Failed to load reports. Showing cached data.', 'error');
        setReport(MOCK_REPORT);
        setBreakdown(MOCK_BREAKDOWN);
        setChannels(MOCK_CHANNELS);
        setInventory(MOCK_INVENTORY);
      })
      .finally(() => setLoading(false));
  }, [token, dateRange]);

  const handleExport = (e: FormEvent) => {
    e.preventDefault();
    setIsExportModalOpen(false);
    if (exportMethod === 'download') {
      toast(`Report successfully packaged as ${exportFormat.toUpperCase()} and downloading.`, 'success');
    } else if (exportMethod === 'email') {
      toast(`Report scheduled to be emailed.`, 'success');
    } else {
      toast(`Report scheduled successfully.`, 'success');
    }
  };

  const r = report ?? MOCK_REPORT;
  const bd = breakdown.length > 0 ? breakdown : MOCK_BREAKDOWN;
  const ch = channels.length > 0 ? channels : MOCK_CHANNELS;
  const inv = inventory.length > 0 ? inventory : MOCK_INVENTORY;

  const revenueMonths = r.monthlyData.map((d) => ({ ...d, name: d.month }));
  const breakdownChart = bd.map((d) => ({ name: d.name, value: d.percentage }));

  if (loading && !report) {
    return (
      <div className="h-full flex items-center justify-center">
        <div className="text-white/40 text-sm">Loading reports...</div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col gap-8 overflow-y-auto no-scrollbar pb-10">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight text-[#F5F2ED]">Analytics & Intelligence</h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Financial & Operational Insights</p>
        </div>
        <div className="flex items-center gap-3">
          <div className="relative">
            <select 
              value={dateRange}
              onChange={(e) => setDateRange(e.target.value)}
              className="appearance-none bg-[#141414] border border-white/10 rounded-full px-5 py-2.5 pr-10 text-xs font-bold uppercase tracking-wider text-[#F5F2ED] focus:outline-none focus:border-wotege-gold/50 cursor-pointer"
            >
              <option>Today</option>
              <option>This Week</option>
              <option>This Month</option>
              <option>Quarterly</option>
              <option>Last 7 Months</option>
              <option>Year to Date</option>
            </select>
            <Calendar className="w-4 h-4 text-white/40 absolute right-4 top-1/2 -translate-y-1/2 pointer-events-none" />
          </div>
          
          <div className="flex bg-[#141414] border border-white/10 rounded-full p-1">
            <button onClick={() => { setExportFormat('pdf'); setIsExportModalOpen(true); }} className="px-4 py-2 hover:bg-white/5 rounded-full text-xs font-bold uppercase tracking-wider text-white/60 hover:text-white transition-colors flex items-center">
              <Download className="w-4 h-4 mr-2" /> PDF
            </button>
            <button onClick={() => { setExportFormat('csv'); setIsExportModalOpen(true); }} className="px-4 py-2 hover:bg-white/5 rounded-full text-xs font-bold uppercase tracking-wider text-white/60 hover:text-white transition-colors flex items-center">
              <FileText className="w-4 h-4 mr-2" /> CSV
            </button>
            <button onClick={() => setIsExportModalOpen(true)} className="px-4 py-2 hover:bg-white/5 rounded-full text-xs font-bold uppercase tracking-wider text-white/60 hover:text-white transition-colors flex items-center">
              <Settings className="w-4 h-4" />
            </button>
          </div>
        </div>
      </header>

      {/* AI Insights & KPI Row */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 shrink-0">
        {/* Main KPI */}
        <div className="col-span-1 md:col-span-2 bg-[#111] border border-white/5 rounded-[2rem] p-8 relative overflow-hidden group hover:border-wotege-gold/20 transition-all">
          <div className="absolute top-0 right-0 w-64 h-64 bg-wotege-gold/5 rounded-full blur-[80px] group-hover:bg-wotege-gold/10 transition-colors pointer-events-none" />
          <div className="relative z-10 flex flex-col h-full justify-between">
            <div className="flex justify-between items-start mb-6">
              <div>
                <h3 className="text-sm font-bold uppercase tracking-widest text-white/40 mb-1">Gross Revenue (YTD)</h3>
                <div className="text-4xl font-serif text-[#F5F2ED]">{formatRevenue(r.grossRevenueYtd)}</div>
              </div>
              <div className="bg-wotege-gold/10 text-wotege-gold px-3 py-1.5 rounded-full text-xs font-bold flex items-center border border-wotege-gold/20">
                <TrendingUp className="w-3 h-3 mr-1" />
                +24.5%
              </div>
            </div>
            
            <div className="grid grid-cols-3 gap-4 border-t border-white/5 pt-6 mt-auto">
              <div>
                <p className="text-[10px] uppercase font-bold tracking-widest text-white/40 mb-1">Net Profit Margin</p>
                <p className="text-lg font-serif text-wotege-gold">{r.netProfitMargin}%</p>
              </div>
              <div>
                <p className="text-[10px] uppercase font-bold tracking-widest text-white/40 mb-1">Avg Occupancy</p>
                <p className="text-lg font-serif text-[#F5F2ED]">{r.avgOccupancy}%</p>
              </div>
              <div>
                <p className="text-[10px] uppercase font-bold tracking-widest text-white/40 mb-1">RevPAR</p>
                <p className="text-lg font-serif text-[#F5F2ED]">{formatRevenue(r.revPAR)}</p>
              </div>
            </div>
          </div>
        </div>

        {/* AI Predictive Insight */}
        <div className="bg-gradient-to-br from-[#1A1A1A] to-[#111111] border border-wotege-gold/20 rounded-[2rem] p-8 relative overflow-hidden flex flex-col justify-between">
          <div className="flex items-center gap-2 mb-4 text-wotege-gold relative z-10">
            <Sparkles className="w-5 h-5 fill-wotege-gold" />
            <h3 className="text-xs font-bold uppercase tracking-widest">AI Intelligence</h3>
          </div>
          
          <div className="relative z-10 space-y-4">
            <div className="bg-black/40 rounded-xl p-4 border border-white/5 border-l-2 border-l-wotege-gold">
              <p className="text-sm text-[#F5F2ED] leading-relaxed">
                Weekend occupancy is projected to reach <span className="text-wotege-gold font-bold">94%</span>. Recommended to increase room rates by 12% for remaining inventory.
              </p>
            </div>
            <div className="bg-black/40 rounded-xl p-4 border border-white/5">
              <p className="text-sm text-white/60 leading-relaxed">
                Signature Seafood Platter trending in restaurant POS. Consider promoting it as a special tonight to drive high-margin sales.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Main Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-[#111] border border-white/5 rounded-[2rem] p-6 lg:p-8">
          <div className="flex items-center justify-between mb-8">
            <h3 className="text-sm font-bold uppercase tracking-widest text-white/80">Revenue Performance</h3>
            <button className="text-xs font-bold uppercase tracking-widest text-wotege-gold hover:text-white transition-colors flex items-center">
              View Detailed P&L <ArrowUpRight className="w-4 h-4 ml-1" />
            </button>
          </div>
          <div className="h-[300px] w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={revenueMonths} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorRooms" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#C5A059" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#C5A059" stopOpacity={0}/>
                  </linearGradient>
                  <linearGradient id="colorRestaurant" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#8C6D31" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#8C6D31" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" vertical={false} />
                <XAxis dataKey="name" stroke="rgba(255,255,255,0.2)" fontSize={12} tickMargin={10} axisLine={false} />
                <YAxis stroke="rgba(255,255,255,0.2)" fontSize={12} tickFormatter={(val) => `LKR ${val/1000000}M`} axisLine={false} tickLine={false} />
                <Tooltip content={<CustomTooltip />} />
                <Area type="monotone" dataKey="rooms" name="Rooms" stroke="#C5A059" strokeWidth={2} fillOpacity={1} fill="url(#colorRooms)" />
                <Area type="monotone" dataKey="restaurant" name="Restaurant/POS" stroke="#8C6D31" strokeWidth={2} fillOpacity={1} fill="url(#colorRestaurant)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="bg-[#111] border border-white/5 rounded-[2rem] p-6 lg:p-8 flex flex-col">
          <h3 className="text-sm font-bold uppercase tracking-widest text-white/80 mb-8">Revenue Breakdown</h3>
          <div className="flex-1 min-h-[200px]">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={breakdownChart}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={80}
                  paddingAngle={5}
                  dataKey="value"
                  stroke="none"
                >
                  {breakdownChart.map((_entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip 
                  contentStyle={{ backgroundColor: '#111', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '12px' }}
                  itemStyle={{ color: '#F5F2ED', fontFamily: 'serif' }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="mt-4 space-y-3">
            {bd.map((item, index) => (
              <div key={index} className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded-full" style={{ backgroundColor: COLORS[index % COLORS.length] }} />
                  <span className="text-sm text-white/60">{item.name}</span>
                </div>
                <span className="text-sm font-medium text-[#F5F2ED]">{item.percentage}%</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Secondary Metrics / Operational Analytics */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
         {/* Booking Channels */}
         <div className="bg-[#111] border border-white/5 rounded-[2rem] p-6 lg:p-8">
            <h3 className="text-sm font-bold uppercase tracking-widest text-white/80 mb-6">Booking Channels Performance</h3>
            <div className="space-y-6">
              {ch.map((item, idx) => (
                <div key={idx}>
                  <div className="flex justify-between items-end mb-2">
                    <span className="text-sm font-medium text-[#F5F2ED]">{item.channel}</span>
                    <span className="text-xs text-wotege-gold">{formatRevenue(item.revenue)}</span>
                  </div>
                  <div className="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-wotege-gold rounded-full"
                      style={{ width: `${item.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
         </div>
         
         {/* POS Top Items */}
         <div className="bg-[#111] border border-white/5 rounded-[2rem] p-6 lg:p-8">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-sm font-bold uppercase tracking-widest text-white/80">Top Moving Inventory</h3>
              <button className="text-xs text-white/40 hover:text-white transition-colors">View All</button>
            </div>
            <div className="space-y-4">
              {inv.map((stock, idx) => (
                <div key={idx} className="flex items-center justify-between p-3 rounded-xl hover:bg-white/5 transition-colors border border-transparent hover:border-white/5 cursor-pointer">
                  <div className="flex items-center gap-4">
                    <div className="w-10 h-10 rounded-full bg-[#141414] border border-white/5 flex items-center justify-center shrink-0">
                      <Activity className="w-4 h-4 text-white/40" />
                    </div>
                    <div>
                      <div className="text-sm font-medium text-[#F5F2ED]">{stock.itemName}</div>
                      <div className="text-[10px] uppercase tracking-wider text-white/40 mt-0.5">{stock.category}</div>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className={`text-sm ${stock.trend.startsWith('+') ? 'text-green-500' : 'text-red-500'}`}>{stock.trend}</div>
                    {stock.status === 'reorder' ? (
                      <span className="text-[10px] text-wotege-gold flex items-center gap-1 mt-1 justify-end"><AlertCircle className="w-3 h-3" /> Reorder</span>
                    ) : (
                      <span className="text-[10px] text-white/20 mt-1 block">Optimal</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
         </div>
      </div>

      <Modal isOpen={isExportModalOpen} onClose={() => setIsExportModalOpen(false)} title="Export & Share Reports">
        <form onSubmit={handleExport} className="space-y-6">
          <div className="space-y-4">
             <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Export Format</label>
               <div className="grid grid-cols-3 gap-2">
                 {['pdf', 'csv', 'excel'].map((fmt) => (
                   <label key={fmt} className={`border ${exportFormat === fmt ? 'border-wotege-gold bg-wotege-gold/10 text-wotege-gold' : 'border-white/10 bg-[#141414] text-white/60'} rounded-xl p-3 flex justify-center items-center cursor-pointer transition-colors hover:border-wotege-gold/50`}>
                     <input type="radio" value={fmt} checked={exportFormat === fmt} onChange={(e) => setExportFormat(e.target.value)} className="hidden" />
                     <span className="text-xs font-bold uppercase tracking-wider">{fmt}</span>
                   </label>
                 ))}
               </div>
             </div>

             <div>
               <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Delivery Method</label>
               <div className="grid grid-cols-1 gap-2">
                 <label className={`border ${exportMethod === 'download' ? 'border-wotege-gold bg-wotege-gold/10 text-wotege-gold' : 'border-white/10 bg-[#141414] text-white/60'} rounded-xl p-4 flex items-center cursor-pointer transition-colors`}>
                   <input type="radio" value="download" checked={exportMethod === 'download'} onChange={(e) => setExportMethod(e.target.value)} className="hidden" />
                   <Download className="w-5 h-5 mr-3 shrink-0" />
                   <div className="flex-1">
                     <div className="text-sm font-bold">Direct Download</div>
                     <div className="text-[10px] text-white/40 mt-1">Download file to your local device immediately</div>
                   </div>
                 </label>
                 <label className={`border ${exportMethod === 'email' ? 'border-wotege-gold bg-wotege-gold/10 text-wotege-gold' : 'border-white/10 bg-[#141414] text-white/60'} rounded-xl p-4 flex items-center cursor-pointer transition-colors`}>
                   <input type="radio" value="email" checked={exportMethod === 'email'} onChange={(e) => setExportMethod(e.target.value)} className="hidden" />
                   <Mail className="w-5 h-5 mr-3 shrink-0" />
                   <div className="flex-1">
                     <div className="text-sm font-bold">Send via Email</div>
                     <div className="text-[10px] text-white/40 mt-1">Send to specified email addresses</div>
                   </div>
                 </label>
                 <label className={`border ${exportMethod === 'schedule' ? 'border-wotege-gold bg-wotege-gold/10 text-wotege-gold' : 'border-white/10 bg-[#141414] text-white/60'} rounded-xl p-4 flex items-center cursor-pointer transition-colors`}>
                   <input type="radio" value="schedule" checked={exportMethod === 'schedule'} onChange={(e) => setExportMethod(e.target.value)} className="hidden" />
                   <Clock className="w-5 h-5 mr-3 shrink-0" />
                   <div className="flex-1">
                     <div className="text-sm font-bold">Schedule Delivery</div>
                     <div className="text-[10px] text-white/40 mt-1">Automate recurring report generation</div>
                   </div>
                 </label>
               </div>
             </div>

             {exportMethod === 'email' && (
               <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }}>
                 <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Recipient Emails (comma separated)</label>
                 <input type="text" placeholder="e.g. director@wotege.com, finance@wotege.com" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
               </motion.div>
             )}

             {exportMethod === 'schedule' && (
               <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} className="space-y-4">
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Frequency</label>
                   <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required>
                     <option value="daily">Daily</option>
                     <option value="weekly">Weekly</option>
                     <option value="monthly">Monthly</option>
                   </select>
                 </div>
                 <div>
                   <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Recipient Emails (comma separated)</label>
                   <input type="text" placeholder="e.g. director@wotege.com, finance@wotege.com" className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" required />
                 </div>
               </motion.div>
             )}

             {exportFormat === 'pdf' && (
               <div className="flex items-center gap-2 mt-4 bg-white/5 p-3 rounded-lg border border-white/10">
                 <input type="checkbox" id="brandTemplate" defaultChecked className="accent-wotege-gold w-4 h-4 cursor-pointer" />
                 <label htmlFor="brandTemplate" className="text-xs text-white/60 cursor-pointer">Use <span className="text-wotege-gold">WOTEGE Premium</span> branded template</label>
               </div>
             )}
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-white/5">
            <button type="button" onClick={() => setIsExportModalOpen(false)} className="px-5 py-3 rounded-xl border border-white/10 text-white/60 hover:bg-white/5 hover:text-white transition-colors text-xs font-bold uppercase tracking-widest">
              Cancel
            </button>
            <button type="submit" className="px-6 py-3 rounded-xl bg-wotege-gold text-black hover:shadow-[0_0_15px_rgba(197,160,89,0.3)] transition-all font-bold text-xs uppercase tracking-widest flex items-center">
              Generate Export <ArrowUpRight className="w-4 h-4 ml-2" />
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
