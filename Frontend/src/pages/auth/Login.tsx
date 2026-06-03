import { useState, type FormEvent } from 'react';
import { motion } from 'motion/react';
import { LogIn, Loader2, Eye, EyeOff } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { GoldParticles } from '@/components/ui/GoldParticles';
import { useAuth } from '@/lib/auth-context';

export default function Login() {
  const navigate = useNavigate();
  const { login, loading, error } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [localError, setLocalError] = useState<string | null>(null);

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();
    setLocalError(null);
    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setLocalError(err instanceof Error ? err.message : 'Invalid email or password');
    }
  };

  const displayError = localError || error;

  return (
    <div className="relative min-h-screen bg-[#080808] flex items-center justify-center overflow-hidden">
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-[#111] via-[#080808] to-[#080808]"></div>
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
          {displayError && (
            <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-xs rounded-2xl px-5 py-3 text-center">
              {displayError}
            </div>
          )}

          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-white/40 font-bold ml-1">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-2xl px-5 py-4 text-[#F5F2ED] text-sm focus:outline-none focus:border-wotege-gold/50 transition-all"
              placeholder="rehan2003@gmail.com"
              required
            />
          </div>

          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-white/40 font-bold ml-1">Password</label>
            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-[#141414] border border-white/5 rounded-2xl px-5 py-4 pr-12 text-[#F5F2ED] text-sm focus:outline-none focus:border-wotege-gold/50 transition-all"
                placeholder="••••••••"
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-white/40 hover:text-wotege-gold transition-colors"
              >
                {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
              </button>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-wotege-gold text-black font-bold text-xs uppercase tracking-widest rounded-2xl px-4 py-4 flex items-center justify-center space-x-2 shadow-[0_10px_20px_rgba(197,160,89,0.2)] hover:scale-[1.02] active:scale-[0.98] transition-all transform relative overflow-hidden group disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span className="relative z-10 flex items-center space-x-2">
              {loading ? (
                <Loader2 className="w-4 h-4 animate-spin" />
              ) : (
                <>
                  <span>Sign In</span>
                  <LogIn className="w-4 h-4" />
                </>
              )}
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
