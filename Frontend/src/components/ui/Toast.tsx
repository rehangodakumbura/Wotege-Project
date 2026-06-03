import React, { createContext, useContext, useState, ReactNode } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { CheckCircle2, AlertTriangle, Info, X } from 'lucide-react';

export type ToastType = 'success' | 'error' | 'info';

interface Toast {
  id: string;
  message: string;
  type: ToastType;
}

interface ToastContextType {
  toast: (message: string, type?: ToastType) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const addToast = (message: string, type: ToastType = 'info') => {
    const id = Math.random().toString(36).substring(2, 9);
    setToasts((prev) => [...prev, { id, message, type }]);
    
    // Auto remove
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 4000);
  };

  const removeToast = (id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  };

  return (
    <ToastContext.Provider value={{ toast: addToast }}>
      {children}
      
      <div className="fixed bottom-6 right-6 z-[100] flex flex-col gap-3 pointer-events-none">
        <AnimatePresence>
          {toasts.map((t) => (
            <motion.div
              key={t.id}
              initial={{ opacity: 0, y: 50, scale: 0.9 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              exit={{ opacity: 0, scale: 0.9, transition: { duration: 0.2 } }}
              className="pointer-events-auto flex items-center gap-3 bg-[#111] border border-white/10 shadow-2xl rounded-xl p-4 pr-12 min-w-[300px] relative overflow-hidden"
            >
              {/* Type indicator line */}
              <div className={`absolute left-0 top-0 bottom-0 w-1 ${
                t.type === 'success' ? 'bg-green-500' : 
                t.type === 'error' ? 'bg-red-500' : 'bg-wotege-gold'
              }`} />
              
              {t.type === 'success' && <CheckCircle2 className="w-5 h-5 text-green-500 shrink-0" />}
              {t.type === 'error' && <AlertTriangle className="w-5 h-5 text-red-500 shrink-0" />}
              {t.type === 'info' && <Info className="w-5 h-5 text-wotege-gold shrink-0" />}
              
              <p className="text-sm text-[#F5F2ED] font-medium">{t.message}</p>
              
              <button 
                onClick={() => removeToast(t.id)}
                className="absolute right-3 top-1/2 -translate-y-1/2 p-1 text-white/40 hover:text-white transition-colors rounded-lg hover:bg-white/5"
              >
                <X className="w-4 h-4" />
              </button>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>
    </ToastContext.Provider>
  );
}

export const useToast = () => {
  const context = useContext(ToastContext);
  if (context === undefined) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
