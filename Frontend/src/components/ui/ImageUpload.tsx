import React, { useState, useRef } from 'react';
import { Upload, X, Image as ImageIcon } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

interface ImageUploadProps {
  onUpload: (file: File) => void;
  imageUrl?: string | null;
  label?: string;
}

export function ImageUpload({ onUpload, imageUrl = null, label = "Upload Image" }: ImageUploadProps) {
  const [preview, setPreview] = useState<string | null>(imageUrl);
  const [isHovered, setIsHovered] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result as string);
      };
      reader.readAsDataURL(file);
      onUpload(file);
    }
  };

  const clearImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    setPreview(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="w-full">
      <span className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">{label}</span>
      <div 
        className={`relative w-full h-40 border-2 border-dashed rounded-2xl flex flex-col items-center justify-center cursor-pointer overflow-hidden transition-all duration-300 ${isHovered ? 'border-wotege-gold bg-wotege-gold/5' : 'border-white/10 bg-[#141414] hover:border-white/20'}`}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={() => fileInputRef.current?.click()}
      >
        <input 
          type="file" 
          ref={fileInputRef} 
          onChange={handleFileChange} 
          accept="image/*" 
          className="hidden" 
        />
        
        <AnimatePresence>
          {preview ? (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="absolute inset-0 w-full h-full"
            >
              <img src={preview} alt="Preview" className="w-full h-full object-cover" />
              <div className="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 hover:opacity-100 transition-opacity duration-300">
                <button 
                  onClick={clearImage}
                  className="bg-red-500/80 text-white p-2 rounded-full hover:bg-red-500 transition-colors"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>
            </motion.div>
          ) : (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="flex flex-col items-center text-white/40"
            >
              <Upload className={`w-8 h-8 mb-3 transition-colors duration-300 ${isHovered ? 'text-wotege-gold' : 'text-white/20'}`} />
              <p className="text-xs font-medium">Drag & drop or click to upload</p>
              <p className="text-[10px] opacity-60 mt-1">PNG, JPG up to 5MB</p>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}
