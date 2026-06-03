export const roomsData = [
  { id: '101', type: 'Deluxe Suite', status: 'Available', floor: 1, price: 35000 },
  { id: '102', type: 'Deluxe Suite', status: 'Occupied', floor: 1, guest: 'Alex M.', checkout: 'Today' },
  { id: '201', type: 'Presidential', status: 'Cleaning', floor: 2, price: 120000 },
  { id: '202', type: 'Standard', status: 'Available', floor: 2, price: 15000 },
  { id: '203', type: 'Standard', status: 'Reserved', floor: 2, guest: 'Sarah W.', checkin: 'Tomorrow' },
  { id: '301', type: 'Penthouse', status: 'Maintenance', floor: 3, price: 250000 },
  { id: '302', type: 'Standard', status: 'Available', floor: 3, price: 15000 },
  { id: '303', type: 'Deluxe Suite', status: 'Occupied', floor: 3, guest: 'John D.', checkout: 'Tomorrow' },
];

export const reservationsData = [
  { id: 'RES-001', guest: 'Richard Thompson', room: 'Presidential Suite', checkIn: 'Oct 15, 2026', checkOut: 'Oct 18, 2026', amount: 'LKR 450,000', status: 'Confirmed' },
  { id: 'RES-002', guest: 'Sofia Al-Maktoum', room: 'Suite 602', checkIn: 'Oct 14, 2026', checkOut: 'Oct 16, 2026', amount: 'LKR 120,000', status: 'In House' },
  { id: 'RES-003', guest: 'Karin Lagerfeld', room: 'Penthouse', checkIn: 'Oct 18, 2026', checkOut: 'Oct 22, 2026', amount: 'LKR 580,000', status: 'Pending' },
  { id: 'RES-004', guest: 'Michael Chen', room: 'Standard 204', checkIn: 'Oct 12, 2026', checkOut: 'Oct 14, 2026', amount: 'LKR 30,000', status: 'Completed' },
];

export const menuCategories = ['All', 'Starters', 'Mains', 'Desserts', 'Beverages', 'Alcohol', 'Specials'];

export const menuItemsData = [
  { id: 'M-101', name: 'Wagyu Beef Steak', price: 8500, category: 'Mains', image: 'https://images.unsplash.com/photo-1546241072-48010ad168d5?q=80&w=300&auto=format&fit=crop', status: 'Available', orders: 124 },
  { id: 'M-102', name: 'Truffle Risotto', price: 4200, category: 'Mains', image: 'https://images.unsplash.com/photo-1626379616459-b2ce1d9decbc?q=80&w=300&auto=format&fit=crop', status: 'Available', orders: 89 },
  { id: 'M-103', name: 'Lobster Bisque', price: 2800, category: 'Starters', image: 'https://images.unsplash.com/photo-1548943487-a2e4143fa723?q=80&w=300&auto=format&fit=crop', status: 'Out of Stock', orders: 112 },
  { id: 'M-104', name: 'Gold-leaf Sushi Roll', price: 5500, category: 'Mains', image: 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=300&auto=format&fit=crop', status: 'Available', orders: 56 },
  { id: 'M-105', name: 'Dom Perignon 2012', price: 29000, category: 'Alcohol', image: 'https://images.unsplash.com/photo-1590740924976-5a415ffcc934?q=80&w=300&auto=format&fit=crop', status: 'Available', orders: 20 },
  { id: 'M-106', name: 'Matcha Tiramisu', price: 1800, category: 'Desserts', image: 'https://images.unsplash.com/photo-1571115177098-24ec42ed204d?q=80&w=300&auto=format&fit=crop', status: 'Available', orders: 201 },
];

export const customersData = [
  { id: 'C-001', name: 'Sofia Al-Maktoum', email: 'sofia.al@example.com', phone: '+971 50 123 4567', visits: 12, spent: 'LKR 1,250,000', tier: 'VIP Gold' },
  { id: 'C-002', name: 'Richard Thompson', email: 'r.thompson@corp.uk', phone: '+44 7911 123456', visits: 5, spent: 'LKR 450,000', tier: 'Silver' },
  { id: 'C-003', name: 'Karin Lagerfeld', email: 'karin.l@design.fr', phone: '+33 6 12 34 56 78', visits: 24, spent: 'LKR 3,890,000', tier: 'Platinum' },
  { id: 'C-004', name: 'Michael Chen', email: 'm.chen@tech.sg', phone: '+65 9123 4567', visits: 2, spent: 'LKR 85,000', tier: 'Member' },
];

export const staffData = [
  { id: 'EMP-001', name: 'Ahmed Silva', role: 'Hotel Manager', department: 'Management', status: 'Active', shift: 'Morning' },
  { id: 'EMP-002', name: 'Sarah Connor', role: 'Head Chef', department: 'Kitchen', status: 'Active', shift: 'Evening' },
  { id: 'EMP-003', name: 'David Lee', role: 'Receptionist', department: 'Front Desk', status: 'On Leave', shift: 'Night' },
  { id: 'EMP-004', name: 'Emma Wilson', role: 'Housekeeping', department: 'Maintenance', status: 'Active', shift: 'Morning' },
  { id: 'EMP-005', name: 'Jean Pierre', role: 'Sommelier', department: 'Restaurant', status: 'Active', shift: 'Evening' },
];

export const reportsData = {
  revenueOverTime: [
    { name: 'Jan', revenue: 4000000 },
    { name: 'Feb', revenue: 3000000 },
    { name: 'Mar', revenue: 5000000 },
    { name: 'Apr', revenue: 4500000 },
    { name: 'May', revenue: 6000000 },
    { name: 'Jun', revenue: 5500000 },
  ],
  occupancyRate: 82,
  totalRevenue: 'LKR 28,000,000',
  activeGuests: 145,
};
