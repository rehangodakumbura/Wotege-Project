import { motion } from 'motion/react';
import { useNavigate } from 'react-router-dom';
import { ShieldAlert, ArrowLeft } from 'lucide-react';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-[#0A0A0A] flex flex-col items-center justify-center p-4 relative overflow-hidden">
      {/* Background accents */}
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-wotege-gold/5 rounded-full blur-[100px] pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-wotege-gold/5 rounded-full blur-[100px] pointer-events-none" />
      
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="relative z-10 flex flex-col items-center text-center max-w-lg"
      >
        <div className="w-24 h-24 mb-8 bg-[#111] border border-white/10 rounded-3xl flex items-center justify-center shadow-[0_0_30px_rgba(197,160,89,0.1)]">
          <ShieldAlert className="w-10 h-10 text-wotege-gold" />
        </div>
        
        <h1 className="text-6xl font-serif text-[#F5F2ED] mb-4">404</h1>
        <h2 className="text-xl font-bold uppercase tracking-widest text-wotege-gold mb-6">Page Not Found</h2>
        
        <p className="text-white/40 font-sans text-sm mb-10 leading-relaxed">
          The requested resource could not be located in the WOTEGE operating system. Please verify the URL or return to your authorized dashboard.
        </p>
        
        <button 
          onClick={() => navigate('/dashboard')}
          className="flex items-center px-8 py-4 bg-[#111] border border-white/10 rounded-full text-xs font-bold uppercase tracking-widest text-[#F5F2ED] hover:border-wotege-gold/50 hover:text-wotege-gold transition-all group"
        >
          <ArrowLeft className="w-4 h-4 mr-3 group-hover:-translate-x-1 transition-transform" />
          Return to Dashboard
        </button>
      </motion.div>
    </div>
  );
}
