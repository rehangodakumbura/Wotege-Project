import { motion, AnimatePresence } from 'motion/react';
import { ChefHat, CreditCard, Search, Plus, Minus, ShoppingBag } from 'lucide-react';
import { useState, useEffect, useMemo } from 'react';
import { useAuth } from '@/lib/auth-context';
import { getMenuCategories, getMenuItems, type MenuItem, type MenuCategory } from '@/lib/api';

interface CartItem extends MenuItem {
  quantity: number;
}

const FALLBACK_CATEGORIES: MenuCategory[] = [
  { id: 0, code: 'ALL', name: 'All', description: '', displayOrder: 0, active: true },
  { id: 1, code: 'STA', name: 'Starters', description: '', displayOrder: 1, active: true },
  { id: 2, code: 'MNS', name: 'Mains', description: '', displayOrder: 2, active: true },
  { id: 3, code: 'DST', name: 'Desserts', description: '', displayOrder: 3, active: true },
  { id: 4, code: 'BVG', name: 'Beverages', description: '', displayOrder: 4, active: true },
  { id: 5, code: 'ALC', name: 'Alcohol', description: '', displayOrder: 5, active: true },
];

const FALLBACK_ITEMS: MenuItem[] = [
  { id: 1, code: 'MNS-001', name: 'Wagyu Beef Steak', description: 'A5 Japanese wagyu with truffle jus', price: 8500, costPrice: 3500, imageUrl: 'https://images.unsplash.com/photo-1546241072-48010ad168d5?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 25, category: FALLBACK_CATEGORIES[2], createdAt: '', updatedAt: '' },
  { id: 2, code: 'MNS-002', name: 'Truffle Risotto', description: 'Arborio rice with black truffle', price: 4200, costPrice: 1600, imageUrl: 'https://images.unsplash.com/photo-1626379616459-b2ce1d9decbc?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 20, category: FALLBACK_CATEGORIES[2], createdAt: '', updatedAt: '' },
  { id: 3, code: 'STA-001', name: 'Lobster Bisque', description: 'Creamy lobster soup with cognac', price: 2800, costPrice: 1100, imageUrl: 'https://images.unsplash.com/photo-1548943487-a2e4143fa723?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 12, category: FALLBACK_CATEGORIES[1], createdAt: '', updatedAt: '' },
  { id: 4, code: 'MNS-003', name: 'Gold-leaf Sushi Roll', description: 'Premium sushi with edible gold leaf', price: 5500, costPrice: 2200, imageUrl: 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 15, category: FALLBACK_CATEGORIES[2], createdAt: '', updatedAt: '' },
  { id: 5, code: 'ALC-001', name: 'Dom Perignon 2012', description: 'Prestige cuvée champagne', price: 29000, costPrice: 12000, imageUrl: 'https://images.unsplash.com/photo-1590740924976-5a415ffcc934?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 2, category: FALLBACK_CATEGORIES[5], createdAt: '', updatedAt: '' },
  { id: 6, code: 'DST-001', name: 'Matcha Tiramisu', description: 'Japanese twist on classic tiramisu', price: 1800, costPrice: 600, imageUrl: 'https://images.unsplash.com/photo-1571115177098-24ec42ed204d?q=80&w=300&auto=format&fit=crop', available: true, isSignature: false, preparationTime: 5, category: FALLBACK_CATEGORIES[3], createdAt: '', updatedAt: '' },
];

const TAX_RATE = 0.08;

export default function POS() {
  const { token } = useAuth();

  const [categories, setCategories] = useState<MenuCategory[]>(FALLBACK_CATEGORIES);
  const [items, setItems] = useState<MenuItem[]>(FALLBACK_ITEMS);
  const [activeCategory, setActiveCategory] = useState<number | 'ALL'>('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }
    setLoading(true);
    getMenuCategories(token)
      .then((data) => {
        if (data && data.length > 0) {
          setCategories([{ id: 0, code: 'ALL', name: 'All', description: '', displayOrder: 0, active: true }, ...data]);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [token]);

  useEffect(() => {
    if (!token) return;
    const opts: { categoryId?: number; search?: string } = {};
    if (activeCategory !== 'ALL') opts.categoryId = activeCategory;
    if (searchQuery.trim()) opts.search = searchQuery.trim();
    getMenuItems(token, opts)
      .then(setItems)
      .catch(() => {});
  }, [token, activeCategory, searchQuery]);

  const filteredItems = useMemo(() => {
    if (!searchQuery.trim()) return items;
    const q = searchQuery.toLowerCase();
    return items.filter(
      (i) => i.name.toLowerCase().includes(q) || (i.description || '').toLowerCase().includes(q)
    );
  }, [items, searchQuery]);

  const addToCart = (item: MenuItem) => {
    setCart((prev) => {
      const existing = prev.find((c) => c.id === item.id);
      if (existing) {
        return prev.map((c) => (c.id === item.id ? { ...c, quantity: c.quantity + 1 } : c));
      }
      return [...prev, { ...item, quantity: 1 }];
    });
  };

  const updateQuantity = (id: number, delta: number) => {
    setCart((prev) =>
      prev
        .map((c) => (c.id === id ? { ...c, quantity: c.quantity + delta } : c))
        .filter((c) => c.quantity > 0)
    );
  };

  const subtotal = cart.reduce((sum, c) => sum + c.price * c.quantity, 0);
  const tax = subtotal * TAX_RATE;
  const total = subtotal + tax;

  return (
    <div className="h-full flex gap-6">
      <div className="flex-1 flex flex-col h-full overflow-hidden">
        <header className="flex justify-between items-center mb-6 shrink-0">
          <div>
            <h1 className="text-2xl font-serif tracking-tight">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Point of Sale</span></h1>
            <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Restaurant Terminal 1</p>
          </div>
          <div className="relative group w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/30 group-focus-within:text-wotege-gold" />
            <input
              type="text"
              placeholder="Search menu..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full bg-[#141414] border border-white/5 rounded-full pl-10 pr-4 py-2.5 text-sm text-[#F5F2ED] placeholder:text-white/20 focus:outline-none focus:border-wotege-gold/50"
            />
          </div>
        </header>

        <div className="flex space-x-2 overflow-x-auto pb-4 shrink-0 no-scrollbar">
          {categories.map((cat) => {
            const isActive = activeCategory === cat.id;
            return (
              <button
                key={cat.id}
                onClick={() => setActiveCategory(cat.id)}
                className={`px-6 py-2.5 rounded-full text-xs font-bold uppercase tracking-wider whitespace-nowrap transition-all ${
                  isActive
                    ? 'bg-[#141414] border border-wotege-gold text-wotege-gold shadow-[0_4px_20px_rgba(197,160,89,0.2)]'
                    : 'bg-[#111] border border-white/5 text-white/40 hover:text-wotege-gold'
                }`}
              >
                {cat.name}
              </button>
            );
          })}
        </div>

        <div className="flex-1 overflow-y-auto pr-2 pb-2 mt-2">
          {loading && items === FALLBACK_ITEMS ? (
            <div className="flex items-center justify-center h-64 text-white/40 text-sm">Loading menu...</div>
          ) : filteredItems.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-64 text-white/40 text-sm">
              <ShoppingBag className="w-8 h-8 mb-2 opacity-30" />
              No items found
            </div>
          ) : (
            <div className="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {filteredItems.map((item, idx) => (
                <motion.div
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: idx * 0.04 }}
                  key={item.id}
                  onClick={() => addToCart(item)}
                  className="bg-[#111] rounded-[2rem] overflow-hidden border border-white/5 hover:border-wotege-gold/30 transition-all cursor-pointer group shadow-[0_4px_20px_rgba(0,0,0,0.2)] flex flex-col"
                >
                  <div className="h-40 w-full overflow-hidden relative shrink-0">
                    <div className="absolute inset-0 bg-gradient-to-t from-[#111] to-transparent z-10" />
                    {item.imageUrl ? (
                      <img
                        src={item.imageUrl}
                        alt={item.name}
                        className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
                        onError={(e) => {
                          (e.currentTarget as HTMLImageElement).style.display = 'none';
                        }}
                      />
                    ) : (
                      <div className="w-full h-full bg-gradient-to-br from-wotege-gold/10 to-transparent flex items-center justify-center text-wotege-gold/40 text-3xl font-serif">
                        {item.name.charAt(0)}
                      </div>
                    )}
                    <div className="absolute top-3 right-3 z-20 font-serif text-lg bg-black/50 backdrop-blur-md px-3 py-1 rounded-full text-wotege-gold">
                      LKR {item.price.toLocaleString()}
                    </div>
                  </div>
                  <div className="p-5 flex flex-col flex-1">
                    <div className="text-[10px] text-white/40 font-bold uppercase tracking-widest mb-2">
                      {item.category?.name || 'Menu'}
                    </div>
                    <h3 className="text-base font-medium text-[#F5F2ED] leading-tight">{item.name}</h3>
                  </div>
                </motion.div>
              ))}
            </div>
          )}
        </div>
      </div>

      <div className="w-96 shrink-0 flex flex-col h-full bg-[#111] rounded-[2rem] border border-white/5 overflow-hidden shadow-[0_0_30px_rgba(0,0,0,0.5)]">
        <div className="p-6 border-b border-white/5 flex justify-between items-center bg-[#141414]">
          <div>
            <h2 className="text-xl font-serif text-[#F5F2ED]">Current Order</h2>
            <p className="text-[10px] text-white/40 uppercase tracking-widest mt-1">Table 12 • Dine In</p>
          </div>
          <button className="px-3 py-1.5 rounded-full bg-wotege-gold/10 text-wotege-gold text-[10px] uppercase font-bold tracking-wider hover:bg-wotege-gold/20 transition-colors">
            Change Table
          </button>
        </div>

        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          <AnimatePresence>
            {cart.length === 0 ? (
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="flex flex-col items-center justify-center h-full text-center py-12"
              >
                <ShoppingBag className="w-10 h-10 text-white/10 mb-3" />
                <p className="text-sm text-white/30">Cart is empty</p>
                <p className="text-xs text-white/20 mt-1">Tap an item to add it</p>
              </motion.div>
            ) : (
              cart.map((item) => (
                <motion.div
                  key={item.id}
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: -20 }}
                  className="flex gap-4 items-center bg-white/5 p-3 rounded-2xl border border-transparent hover:border-white/5 transition-colors"
                >
                  <div className="w-14 h-14 rounded-xl overflow-hidden shrink-0 border border-white/5">
                    {item.imageUrl ? (
                      <img src={item.imageUrl} alt={item.name} className="w-full h-full object-cover" />
                    ) : (
                      <div className="w-full h-full bg-wotege-gold/10 flex items-center justify-center text-wotege-gold font-serif">
                        {item.name.charAt(0)}
                      </div>
                    )}
                  </div>
                  <div className="flex-1 min-w-0">
                    <h4 className="text-sm font-medium text-[#F5F2ED] truncate">{item.name}</h4>
                    <div className="text-xs text-wotege-gold font-serif mt-1">
                      LKR {(item.price * item.quantity).toLocaleString()}
                    </div>
                  </div>
                  <div className="flex items-center space-x-2 bg-[#080808] rounded-full p-1 border border-white/5">
                    <button
                      onClick={() => updateQuantity(item.id, -1)}
                      className="w-6 h-6 rounded-full hover:bg-white/10 flex items-center justify-center text-white/40 shrink-0 transform active:scale-95 transition-colors"
                    >
                      <Minus className="w-3 h-3" />
                    </button>
                    <span className="text-xs font-bold w-4 text-center">{item.quantity}</span>
                    <button
                      onClick={() => updateQuantity(item.id, 1)}
                      className="w-6 h-6 rounded-full bg-wotege-gold/20 text-wotege-gold flex items-center justify-center shrink-0 transform active:scale-95 transition-colors"
                    >
                      <Plus className="w-3 h-3" />
                    </button>
                  </div>
                </motion.div>
              ))
            )}
          </AnimatePresence>
        </div>

        <div className="p-6 bg-[#141414] border-t border-white/5 mt-auto">
          <div className="space-y-3 mb-6">
            <div className="flex justify-between text-xs font-medium tracking-wide text-white/40">
              <span>Subtotal</span>
              <span>LKR {subtotal.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
            </div>
            <div className="flex justify-between text-xs font-medium tracking-wide text-white/40">
              <span>Tax (8%)</span>
              <span>LKR {tax.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
            </div>
            <div className="flex justify-between text-xl font-serif text-[#F5F2ED] pt-3 border-t border-white/5 mt-3">
              <span>Total</span>
              <span className="text-wotege-gold">LKR {total.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <button
              disabled={cart.length === 0}
              className="py-4 bg-[#111] border border-white/10 rounded-2xl text-white/80 font-bold text-xs uppercase tracking-wider hover:bg-white/5 flex items-center justify-center transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
            >
              <ChefHat className="w-4 h-4 mr-2" /> Kitchen
            </button>
            <button
              disabled={cart.length === 0}
              className="py-4 bg-wotege-gold rounded-2xl text-black font-bold text-xs uppercase tracking-wider shadow-[0_10px_20px_rgba(197,160,89,0.2)] hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              <CreditCard className="w-4 h-4 mr-2" /> Pay
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
