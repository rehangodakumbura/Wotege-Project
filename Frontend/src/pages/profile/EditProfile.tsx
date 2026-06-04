import { useState, type FormEvent } from 'react';
import { motion } from 'motion/react';
import { Save, Loader2, Lock, User as UserIcon, Eye, EyeOff, CheckCircle2 } from 'lucide-react';
import { useAuth } from '@/lib/auth-context';
import { useToast } from '@/components/ui/Toast';
import { updateProfile, changePassword } from '@/lib/api';

export default function EditProfile() {
  const { user, token, updateUser } = useAuth();
  const { toast } = useToast();

  const [fullName, setFullName] = useState(user?.fullName ?? '');
  const [email, setEmail] = useState(user?.email ?? '');
  const [savingProfile, setSavingProfile] = useState(false);

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showCurrent, setShowCurrent] = useState(false);
  const [showNew, setShowNew] = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);

  if (!user || !token) return null;

  const handleProfileSave = async (e: FormEvent) => {
    e.preventDefault();
    setSavingProfile(true);
    try {
      const updated = await updateProfile({ fullName, email }, token);
      updateUser(updated);
      toast('Profile updated successfully', 'success');
    } catch (err) {
      toast(err instanceof Error ? err.message : 'Failed to update profile', 'error');
    } finally {
      setSavingProfile(false);
    }
  };

  const handlePasswordSave = async (e: FormEvent) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      toast('New passwords do not match', 'error');
      return;
    }
    if (newPassword.length < 6) {
      toast('New password must be at least 6 characters', 'error');
      return;
    }

    setSavingPassword(true);
    try {
      await changePassword({ currentPassword, newPassword }, token);
      toast('Password changed successfully', 'success');
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      toast(err instanceof Error ? err.message : 'Failed to change password', 'error');
    } finally {
      setSavingPassword(false);
    }
  };

  const initials = (user.fullName || user.username)
    .split(' ')
    .map((s) => s.charAt(0))
    .slice(0, 2)
    .join('')
    .toUpperCase();

  return (
    <div className="h-full flex flex-col gap-8 pb-4 overflow-y-auto no-scrollbar">
      <header className="flex items-center justify-between shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">
            WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">My Profile</span>
          </h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">
            Manage your account information
          </p>
        </div>
      </header>

      <div className="flex items-center gap-5 bg-[#111] border border-white/5 rounded-[2rem] p-6">
        <div className="w-20 h-20 rounded-full bg-gradient-to-br from-wotege-gold to-[#f0c953] flex items-center justify-center text-black text-2xl font-serif font-bold shadow-[0_0_30px_rgba(197,160,89,0.3)]">
          {initials}
        </div>
        <div className="flex-1 min-w-0">
          <h2 className="text-lg font-medium text-[#F5F2ED]">{user.fullName}</h2>
          <p className="text-sm text-white/40">{user.email}</p>
          <div className="mt-2 flex items-center gap-2">
            <span className="px-3 py-1 bg-wotege-gold/10 text-wotege-gold text-[10px] uppercase tracking-widest font-bold rounded-full border border-wotege-gold/30">
              {user.role?.code ?? 'Admin'}
            </span>
            {user.active && (
              <span className="px-3 py-1 bg-green-500/10 text-green-400 text-[10px] uppercase tracking-widest font-bold rounded-full border border-green-500/30 flex items-center gap-1">
                <CheckCircle2 className="w-3 h-3" /> Active
              </span>
            )}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <motion.section
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-[#111] border border-white/5 rounded-[2rem] p-8 space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-white/5 pb-4">
            <div className="w-10 h-10 rounded-xl bg-wotege-gold/10 text-wotege-gold flex items-center justify-center">
              <UserIcon className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-serif text-[#F5F2ED]">Personal Information</h3>
              <p className="text-xs text-white/40">Update your display name and email</p>
            </div>
          </div>

          <form onSubmit={handleProfileSave} className="space-y-5">
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                Full Name
              </label>
              <input
                type="text"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"
                placeholder="Your full name"
                required
              />
            </div>

            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                Email Address
              </label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"
                placeholder="you@example.com"
                required
              />
            </div>

            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                Username
              </label>
              <input
                type="text"
                value={user.username}
                disabled
                className="w-full bg-[#141414]/50 border border-white/5 rounded-xl px-4 py-3 text-sm text-white/40 cursor-not-allowed"
              />
              <p className="text-[10px] text-white/30 mt-1">Username cannot be changed</p>
            </div>

            <button
              type="submit"
              disabled={savingProfile || (fullName === user.fullName && email === user.email)}
              className="w-full bg-wotege-gold text-black font-bold text-xs uppercase tracking-widest rounded-xl px-4 py-3.5 flex items-center justify-center space-x-2 shadow-[0_8px_20px_rgba(197,160,89,0.2)] hover:scale-[1.01] active:scale-[0.99] transition-all disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              {savingProfile ? (
                <Loader2 className="w-4 h-4 animate-spin" />
              ) : (
                <>
                  <Save className="w-4 h-4" />
                  <span>Save Profile</span>
                </>
              )}
            </button>
          </form>
        </motion.section>

        <motion.section
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.05 }}
          className="bg-[#111] border border-white/5 rounded-[2rem] p-8 space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-white/5 pb-4">
            <div className="w-10 h-10 rounded-xl bg-red-500/10 text-red-400 flex items-center justify-center">
              <Lock className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-serif text-[#F5F2ED]">Change Password</h3>
              <p className="text-xs text-white/40">Update your password to keep your account secure</p>
            </div>
          </div>

          <form onSubmit={handlePasswordSave} className="space-y-5">
            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                Current Password
              </label>
              <div className="relative">
                <input
                  type={showCurrent ? 'text' : 'password'}
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                  className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 pr-12 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"
                  placeholder="Enter current password"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowCurrent(!showCurrent)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-white/40 hover:text-wotege-gold transition-colors"
                >
                  {showCurrent ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                New Password
              </label>
              <div className="relative">
                <input
                  type={showNew ? 'text' : 'password'}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 pr-12 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"
                  placeholder="At least 6 characters"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowNew(!showNew)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-white/40 hover:text-wotege-gold transition-colors"
                >
                  {showNew ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <div>
              <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">
                Confirm New Password
              </label>
              <input
                type={showNew ? 'text' : 'password'}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors"
                placeholder="Re-enter new password"
                required
              />
              {confirmPassword && newPassword !== confirmPassword && (
                <p className="text-[10px] text-red-400 mt-1">Passwords do not match</p>
              )}
            </div>

            <button
              type="submit"
              disabled={savingPassword || !currentPassword || !newPassword || !confirmPassword}
              className="w-full bg-red-500/90 text-white font-bold text-xs uppercase tracking-widest rounded-xl px-4 py-3.5 flex items-center justify-center space-x-2 shadow-[0_8px_20px_rgba(239,68,68,0.2)] hover:bg-red-500 hover:scale-[1.01] active:scale-[0.99] transition-all disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              {savingPassword ? (
                <Loader2 className="w-4 h-4 animate-spin" />
              ) : (
                <>
                  <Lock className="w-4 h-4" />
                  <span>Update Password</span>
                </>
              )}
            </button>
          </form>
        </motion.section>
      </div>
    </div>
  );
}
