import React, { ReactNode } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { X } from 'lucide-react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
}

export function Modal({ isOpen, onClose, title, children }: ModalProps) {
  return (
    <AnimatePresence>
      {isOpen && (
        <>
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
            className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center pt-20 pb-10" // added padding to not overlap with fixed headers if any, though z-index handles it mostly
            style={{ zIndex: 100 }}
          />
          <motion.div
            initial={{ opacity: 0, scale: 0.95, y: 20 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.95, y: 20 }}
            className="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full max-w-lg bg-[#111] border border-white/10 rounded-[2rem] shadow-[0_20px_50px_rgba(0,0,0,0.5)] z-50 overflow-hidden"
            style={{ zIndex: 101, maxHeight: '90vh', display: 'flex', flexDirection: 'column' }}
          >
            <div className="flex justify-between items-center p-6 border-b border-white/5 bg-[#141414] shrink-0">
              <h3 className="text-xl font-serif text-[#F5F2ED]">{title}</h3>
              <button onClick={onClose} className="p-2 text-white/40 hover:text-white transition-colors bg-white/5 rounded-full">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="p-6 overflow-y-auto no-scrollbar">
              {children}
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
