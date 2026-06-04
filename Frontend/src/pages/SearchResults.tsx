import { useSearchParams, useNavigate } from 'react-router-dom';
import { Search, ArrowLeft } from 'lucide-react';
import { motion } from 'motion/react';

export default function SearchResults() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const query = searchParams.get('q') || '';

  return (
    <div className="h-full flex flex-col gap-6">
      <header className="flex items-center gap-4 shrink-0">
        <button
          onClick={() => navigate(-1)}
          className="p-2 bg-[#141414] border border-white/5 rounded-xl text-white/60 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div>
          <h1 className="text-xl font-serif tracking-tight text-[#F5F2ED]">Search Results</h1>
          <p className="text-xs text-white/40 mt-1">Results for &ldquo;{query}&rdquo;</p>
        </div>
      </header>

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex-1 flex flex-col items-center justify-center text-center"
      >
        <div className="w-16 h-16 rounded-full bg-wotege-gold/10 flex items-center justify-center mb-4">
          <Search className="w-6 h-6 text-wotege-gold" />
        </div>
        <h3 className="text-lg font-serif text-[#F5F2ED] mb-2">No results found</h3>
        <p className="text-sm text-white/40 max-w-md">
          Use the sidebar navigation to browse through different modules, or try a different search term.
        </p>
      </motion.div>
    </div>
  );
}
