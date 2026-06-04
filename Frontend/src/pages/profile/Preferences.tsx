import { useState, useEffect, type FormEvent } from 'react';
import { motion } from 'motion/react';
import { Save, Loader2, Bell, Palette, Globe, Moon, Sun, Monitor } from 'lucide-react';
import { useAuth } from '@/lib/auth-context';
import { useToast } from '@/components/ui/Toast';
import { updatePreferences } from '@/lib/api';

type Theme = 'dark' | 'light';
type Language = 'en' | 'fr' | 'sw' | 'ar';

const LANGUAGES: { code: Language; label: string; native: string }[] = [
  { code: 'en', label: 'English', native: 'English' },
  { code: 'fr', label: 'French', native: 'Français' },
  { code: 'sw', label: 'Swahili', native: 'Kiswahili' },
  { code: 'ar', label: 'Arabic', native: 'العربية' },
];

export default function Preferences() {
  const { user, token, updateUser } = useAuth();
  const { toast } = useToast();

  const [theme, setTheme] = useState<Theme>((user?.theme as Theme) ?? 'dark');
  const [language, setLanguage] = useState<Language>((user?.language as Language) ?? 'en');
  const [notificationsEnabled, setNotificationsEnabled] = useState<boolean>(user?.notificationsEnabled ?? true);
  const [emailNotifications, setEmailNotifications] = useState<boolean>(user?.emailNotifications ?? true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (user) {
      setTheme((user.theme as Theme) ?? 'dark');
      setLanguage((user.language as Language) ?? 'en');
      setNotificationsEnabled(user.notificationsEnabled);
      setEmailNotifications(user.emailNotifications);
    }
  }, [user]);

  if (!user || !token) return null;

  const handleSave = async (e: FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      const updated = await updatePreferences(
        { theme, language, notificationsEnabled, emailNotifications },
        token
      );
      updateUser(updated);
      toast('Preferences saved', 'success');
    } catch (err) {
      toast(err instanceof Error ? err.message : 'Failed to save preferences', 'error');
    } finally {
      setSaving(false);
    }
  };

  const dirty =
    theme !== user.theme ||
    language !== user.language ||
    notificationsEnabled !== user.notificationsEnabled ||
    emailNotifications !== user.emailNotifications;

  return (
    <div className="h-full flex flex-col gap-8 pb-4 overflow-y-auto no-scrollbar">
      <header className="flex items-center justify-between shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight">
            WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Preferences</span>
          </h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">
            Customize your workspace
          </p>
        </div>
      </header>

      <form onSubmit={handleSave} className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <motion.section
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-[#111] border border-white/5 rounded-[2rem] p-8 space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-white/5 pb-4">
            <div className="w-10 h-10 rounded-xl bg-wotege-gold/10 text-wotege-gold flex items-center justify-center">
              <Palette className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-serif text-[#F5F2ED]">Appearance</h3>
              <p className="text-xs text-white/40">Choose how WOTEGE looks for you</p>
            </div>
          </div>

          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-4">
              Theme
            </label>
            <div className="grid grid-cols-2 gap-4">
              <button
                type="button"
                onClick={() => setTheme('dark')}
                className={`bg-[#141414] rounded-2xl p-5 flex flex-col items-center gap-3 transition-all border ${
                  theme === 'dark'
                    ? 'border-wotege-gold text-wotege-gold shadow-[0_4px_20px_rgba(197,160,89,0.15)]'
                    : 'border-white/10 text-white/40 hover:text-white hover:border-white/20'
                }`}
              >
                <div className="w-14 h-14 rounded-full bg-black border border-white/10 flex items-center justify-center">
                  <Moon className="w-6 h-6" />
                </div>
                <div className="text-center">
                  <div className="text-xs font-bold uppercase tracking-widest">Dark Luxury</div>
                  <div className="text-[10px] opacity-60 mt-1">Default</div>
                </div>
              </button>

              <button
                type="button"
                onClick={() => setTheme('light')}
                className={`bg-[#141414] rounded-2xl p-5 flex flex-col items-center gap-3 transition-all border ${
                  theme === 'light'
                    ? 'border-wotege-gold text-wotege-gold shadow-[0_4px_20px_rgba(197,160,89,0.15)]'
                    : 'border-white/10 text-white/40 hover:text-white hover:border-white/20'
                }`}
              >
                <div className="w-14 h-14 rounded-full bg-[#F5F2ED] border border-black/10 flex items-center justify-center text-black">
                  <Sun className="w-6 h-6" />
                </div>
                <div className="text-center">
                  <div className="text-xs font-bold uppercase tracking-widest">Light Luxury</div>
                  <div className="text-[10px] opacity-60 mt-1">Coming soon</div>
                </div>
              </button>
            </div>
          </div>
        </motion.section>

        <motion.section
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.05 }}
          className="bg-[#111] border border-white/5 rounded-[2rem] p-8 space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-white/5 pb-4">
            <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-400 flex items-center justify-center">
              <Globe className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-serif text-[#F5F2ED]">Language</h3>
              <p className="text-xs text-white/40">Select your preferred interface language</p>
            </div>
          </div>

          <div className="space-y-2">
            {LANGUAGES.map((lang) => (
              <button
                key={lang.code}
                type="button"
                onClick={() => setLanguage(lang.code)}
                className={`w-full text-left px-5 py-4 rounded-2xl flex items-center justify-between transition-all border ${
                  language === lang.code
                    ? 'bg-wotege-gold/5 border-wotege-gold/50 text-wotege-gold'
                    : 'bg-[#141414] border-white/5 text-white/60 hover:text-white hover:border-white/10'
                }`}
              >
                <div>
                  <div className="text-sm font-medium">{lang.native}</div>
                  <div className="text-[10px] opacity-60 uppercase tracking-widest">{lang.label}</div>
                </div>
                <div
                  className={`w-4 h-4 rounded-full border-2 ${
                    language === lang.code
                      ? 'border-wotege-gold bg-wotege-gold'
                      : 'border-white/20'
                  }`}
                />
              </button>
            ))}
          </div>
        </motion.section>

        <motion.section
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="lg:col-span-2 bg-[#111] border border-white/5 rounded-[2rem] p-8 space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-white/5 pb-4">
            <div className="w-10 h-10 rounded-xl bg-purple-500/10 text-purple-400 flex items-center justify-center">
              <Bell className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-serif text-[#F5F2ED]">Notifications</h3>
              <p className="text-xs text-white/40">Decide how you want to be notified</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-[#141414] border border-white/5 rounded-2xl p-5 flex items-start justify-between gap-4">
              <div className="flex-1">
                <div className="text-sm font-medium text-[#F5F2ED]">In-app notifications</div>
                <p className="text-xs text-white/40 mt-1">
                  Reservation alerts, new orders, low-stock warnings and other system events.
                </p>
              </div>
              <button
                type="button"
                onClick={() => setNotificationsEnabled(!notificationsEnabled)}
                className={`relative w-12 h-7 rounded-full transition-colors shrink-0 ${
                  notificationsEnabled ? 'bg-wotege-gold' : 'bg-white/10'
                }`}
              >
                <div
                  className={`absolute top-1 w-5 h-5 bg-black rounded-full transition-transform ${
                    notificationsEnabled ? 'translate-x-6' : 'translate-x-1'
                  }`}
                />
              </button>
            </div>

            <div className="bg-[#141414] border border-white/5 rounded-2xl p-5 flex items-start justify-between gap-4">
              <div className="flex-1">
                <div className="text-sm font-medium text-[#F5F2ED]">Email notifications</div>
                <p className="text-xs text-white/40 mt-1">
                  Daily reports, payment receipts, and weekly summaries sent to your inbox.
                </p>
              </div>
              <button
                type="button"
                onClick={() => setEmailNotifications(!emailNotifications)}
                className={`relative w-12 h-7 rounded-full transition-colors shrink-0 ${
                  emailNotifications ? 'bg-wotege-gold' : 'bg-white/10'
                }`}
              >
                <div
                  className={`absolute top-1 w-5 h-5 bg-black rounded-full transition-transform ${
                    emailNotifications ? 'translate-x-6' : 'translate-x-1'
                  }`}
                />
              </button>
            </div>
          </div>
        </motion.section>

        <div className="lg:col-span-2 flex justify-end">
          <button
            type="submit"
            disabled={saving || !dirty}
            className="px-8 py-3.5 bg-wotege-gold text-black font-bold text-xs uppercase tracking-widest rounded-xl flex items-center space-x-2 shadow-[0_8px_20px_rgba(197,160,89,0.2)] hover:scale-[1.01] active:scale-[0.99] transition-all disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:scale-100"
          >
            {saving ? (
              <Loader2 className="w-4 h-4 animate-spin" />
            ) : (
              <>
                <Save className="w-4 h-4" />
                <span>Save Preferences</span>
              </>
            )}
          </button>
        </div>
      </form>
    </div>
  );
}
