import React, { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';

export function SplashScreen({ onComplete }: { onComplete: () => void }) {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      setTimeout(onComplete, 800); // Wait for exit animation
    }, 2500);

    return () => clearTimeout(timer);
  }, [onComplete]);

  return (
    <AnimatePresence>
      {isVisible && (
        <motion.div
          initial={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.8, ease: "easeInOut" }}
          className="fixed inset-0 z-[9999] bg-[#0a0a0a] flex flex-col items-center justify-center overflow-hidden"
        >
          {/* Ambient Glow */}
          <motion.div 
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 0.15, scale: 1 }}
            transition={{ duration: 2, ease: "easeOut" }}
            className="absolute w-[500px] h-[500px] bg-wotege-gold rounded-full blur-[120px] pointer-events-none"
          />

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 1, delay: 0.2 }}
            className="relative z-10 flex flex-col items-center"
          >
            <div className="w-24 h-24 bg-wotege-gold/10 rounded-full flex items-center justify-center mb-6 border border-wotege-gold/20 shadow-[0_0_50px_rgba(197,160,89,0.2)]">
              <span className="text-wotege-gold font-serif text-4xl font-bold">W</span>
            </div>
            <motion.h1 
              initial={{ letterSpacing: "0.1em" }}
              animate={{ letterSpacing: "0.2em" }}
              transition={{ duration: 2, ease: "easeOut" }}
              className="text-4xl font-serif text-[#F5F2ED] uppercase tracking-widest mb-3"
            >
              WOTEGE
            </motion.h1>
            <motion.p 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ duration: 1, delay: 0.8 }}
              className="text-wotege-gold/60 text-xs uppercase tracking-[0.3em] font-medium"
            >
              Hospitality Operating System
            </motion.p>
          </motion.div>

          <motion.div 
            initial={{ scaleX: 0, opacity: 0 }}
            animate={{ scaleX: 1, opacity: 1 }}
            transition={{ duration: 1.5, delay: 0.5, ease: "easeInOut" }}
            className="absolute bottom-20 w-48 h-[1px] bg-gradient-to-r from-transparent via-wotege-gold/50 to-transparent"
          />
        </motion.div>
      )}
    </AnimatePresence>
  );
}
