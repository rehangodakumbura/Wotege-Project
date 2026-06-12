import { motion } from 'motion/react';
import { LogIn, Loader2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useState, type FormEvent } from 'react';
import { GoldParticles } from '@/components/ui/GoldParticles';
import { useAuth } from '@/context/AuthContext';

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(username, password);
      navigate('/dashboard');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen bg-[#080808] flex items-center justify-center overflow-hidden">
      {/* Background ambient lighting */}
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-[#111] via-[#080808] to-[#080808]"></div>
      
      {/* Particles */}
      <GoldParticles />

      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.8, ease: [0.16, 1, 0.3, 1] }}
        className="relative z-10 w-full max-w-md p-8 md:p-12 bg-[#111] border border-white/5 rounded-[2rem] shadow-[0_20px_50px_rgba(0,0,0,0.5)]"
      >
        <div className="text-center mb-10">
          <motion.div
            initial={{ scale: 0.9, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ delay: 0.2, duration: 0.6 }}
            className="w-16 h-16 mx-auto mb-6 bg-gradient-to-br from-wotege-gold to-[#f0c953] rounded-2xl flex items-center justify-center shadow-[0_0_30px_rgba(197,160,89,0.3)]"
          >
            <span className="text-black text-2xl font-serif font-bold">W</span>
          </motion.div>
          <h2 className="text-3xl font-serif text-[#F5F2ED] mb-2 tracking-tight">
            WOTEGE <span className="font-light italic text-wotege-gold opacity-60">System</span>
          </h2>
          <p className="text-[10px] uppercase tracking-widest font-bold text-white/40">
            Sign in to access your dashboard
          </p>
        </div>

        <form onSubmit={handleLogin} className="space-y-6">
          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-white/40 font-bold ml-1">Email / Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-2xl px-5 py-4 text-[#F5F2ED] text-sm focus:outline-none focus:border-wotege-gold/50 transition-all"
              placeholder="admin"
            />
          </div>

          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-white/40 font-bold ml-1">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-2xl px-5 py-4 text-[#F5F2ED] text-sm focus:outline-none focus:border-wotege-gold/50 transition-all"
              placeholder="••••••••"
            />
          </div>

          <div className="flex items-center justify-between text-xs font-medium">
            <label className="flex items-center space-x-3 cursor-pointer group">
              <input type="checkbox" className="w-4 h-4 rounded bg-[#141414] border-white/10 text-wotege-gold focus:ring-wotege-gold/50 focus:ring-offset-[#111] accent-wotege-gold transition-colors" />
              <span className="text-white/40 group-hover:text-white/60 transition-colors">Remember me</span>
            </label>
            <button type="button" className="text-wotege-gold hover:text-[#f0c953] transition-colors">
              Forgot Password?
            </button>
          </div>

          {error && (
            <div className="text-red-400 text-xs text-center bg-red-500/10 border border-red-500/20 rounded-xl px-4 py-3">
              {error}
            </div>
          )}
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-wotege-gold text-black font-bold text-xs uppercase tracking-widest rounded-2xl px-4 py-4 flex items-center justify-center space-x-2 shadow-[0_10px_20px_rgba(197,160,89,0.2)] hover:scale-[1.02] active:scale-[0.98] transition-all transform relative overflow-hidden group disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span className="relative z-10 flex items-center space-x-2">
              {loading ? <Loader2 className="w-4 h-4 animate-spin" /> : <LogIn className="w-4 h-4" />}
              <span>{loading ? 'Signing in...' : 'Sign In'}</span>
            </span>
            <div className="absolute inset-0 bg-white/20 translate-y-full group-hover:translate-y-0 transition-transform duration-300" />
          </button>
        </form>

        <div className="mt-8 text-center text-[10px] uppercase tracking-widest font-bold text-white/20">
          Secure, Enterprise-Grade Management
        </div>
      </motion.div>
    </div>
  );
}
