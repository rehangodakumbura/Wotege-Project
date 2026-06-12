import React, { useState, useRef } from 'react';
import { Upload, X, File as FileIcon } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

interface FileUploadProps {
  onUpload: (file: File) => void;
  fileName?: string | null;
  label?: string;
  accept?: string;
}

export function FileUpload({ onUpload, fileName = null, label = "Upload File", accept = "*/*" }: FileUploadProps) {
  const [currentFile, setCurrentFile] = useState<string | null>(fileName);
  const [isHovered, setIsHovered] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setCurrentFile(file.name);
      onUpload(file);
    }
  };

  const clearFile = (e: React.MouseEvent) => {
    e.stopPropagation();
    setCurrentFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="w-full">
      <span className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">{label}</span>
      <div 
        className={`relative w-full p-4 border-2 border-dashed rounded-xl flex items-center justify-between cursor-pointer transition-all duration-300 ${isHovered ? 'border-wotege-gold bg-wotege-gold/5' : 'border-white/10 bg-[#141414] hover:border-white/20'}`}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={() => fileInputRef.current?.click()}
      >
        <input 
          type="file" 
          ref={fileInputRef} 
          onChange={handleFileChange} 
          accept={accept} 
          className="hidden" 
        />
        
        <div className="flex items-center gap-3">
            <div className={`p-2 rounded-lg ${currentFile ? 'bg-wotege-gold/20 text-wotege-gold' : 'bg-white/5 text-white/40'}`}>
                <FileIcon className="w-5 h-5" />
            </div>
            <div>
                <p className={`text-sm font-medium ${currentFile ? 'text-[#F5F2ED]' : 'text-white/40'}`}>
                    {currentFile ? currentFile : 'Select a file to upload'}
                </p>
                {!currentFile && <p className="text-[10px] text-white/20 mt-0.5">PDF, DOC, DOCX up to 10MB</p>}
            </div>
        </div>

        {currentFile && (
            <button 
              onClick={clearFile}
              className="p-1.5 hover:bg-white/10 text-white/60 hover:text-white rounded-md transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
        )}
      </div>
    </div>
  );
}
