const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  fullName: string;
  email: string;
  role: string | null;
}

export interface UserAccount {
  id: number;
  username: string;
  fullName: string;
  email: string;
  active: boolean;
  role: { id: number; code: string; name: string } | null;
  theme: string;
  language: string;
  notificationsEnabled: boolean;
  emailNotifications: boolean;
}

export interface UpdateProfileRequest {
  fullName: string;
  email: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface UpdatePreferencesRequest {
  theme?: string;
  language?: string;
  notificationsEnabled?: boolean;
  emailNotifications?: boolean;
}

function getHeaders(token?: string): Record<string, string> {
  const headers: Record<string, string> = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  return headers;
}

async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const text = await res.text().catch(() => 'Unknown error');
    throw new Error(text || `HTTP ${res.status}`);
  }
  return res.json();
}

export async function login(req: LoginRequest): Promise<LoginResponse> {
  const res = await fetch(`${API_URL}/api/auth/login`, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify(req),
  });
  return handleResponse<LoginResponse>(res);
}

export async function getMe(token: string): Promise<UserAccount> {
  const res = await fetch(`${API_URL}/api/auth/me`, {
    headers: getHeaders(token),
  });
  return handleResponse<UserAccount>(res);
}

export async function updateProfile(req: UpdateProfileRequest, token: string): Promise<UserAccount> {
  return apiPut<UserAccount>('/api/auth/me', req, token);
}

export async function changePassword(req: ChangePasswordRequest, token: string): Promise<UserAccount> {
  return apiPut<UserAccount>('/api/auth/me/password', req, token);
}

export async function updatePreferences(req: UpdatePreferencesRequest, token: string): Promise<UserAccount> {
  return apiPut<UserAccount>('/api/auth/me/preferences', req, token);
}

export async function apiGet<T>(path: string, token: string): Promise<T> {
  const res = await fetch(`${API_URL}${path}`, {
    headers: getHeaders(token),
  });
  return handleResponse<T>(res);
}

export async function apiPost<T>(path: string, body: unknown, token: string): Promise<T> {
  const res = await fetch(`${API_URL}${path}`, {
    method: 'POST',
    headers: getHeaders(token),
    body: JSON.stringify(body),
  });
  return handleResponse<T>(res);
}

export async function apiPut<T>(path: string, body: unknown, token: string): Promise<T> {
  const res = await fetch(`${API_URL}${path}`, {
    method: 'PUT',
    headers: getHeaders(token),
    body: JSON.stringify(body),
  });
  return handleResponse<T>(res);
}

export async function apiDelete(path: string, token: string): Promise<void> {
  const res = await fetch(`${API_URL}${path}`, {
    method: 'DELETE',
    headers: getHeaders(token),
  });
  if (!res.ok) {
    const text = await res.text().catch(() => 'Unknown error');
    throw new Error(text || `HTTP ${res.status}`);
  }
}

// Dashboard types
export interface DashboardSummary {
  totalGrossRevenue: number;
  revenueChangePercent: number;
  roomOccupancyPercent: number;
  occupancyChangePercent: number;
  restaurantLoadPercent: number;
  restaurantLoadSeats: number;
  restaurantTotalSeats: number;
  restaurantAvgWaitMinutes: number;
  activeGuestsCount: number;
  activeGuestChangePercent: number;
  dailyRestaurantSales: number;
  dailyRestaurantSalesChangePercent: number;
}

export interface RevenueChartDataPoint {
  label: string;
  hotelRevenue: number;
  restaurantRevenue: number;
}

export interface ActivityItem {
  time: string;
  action: string;
  detail: string;
  amount: string | null;
  type: 'hotel' | 'restaurant' | 'info' | 'alert';
  initials: string;
}

// Report types
export interface RevenueReport {
  grossRevenueYtd: number;
  netProfitMargin: number;
  avgOccupancy: number;
  revPAR: number;
  monthlyData: RevenueReportDataPoint[];
}

export interface RevenueReportDataPoint {
  month: string;
  rooms: number;
  restaurant: number;
  events: number;
}

export interface RevenueBreakdownItem {
  name: string;
  percentage: number;
}

export interface BookingChannelItem {
  channel: string;
  percentage: number;
  revenue: number;
}

export interface TopInventoryItem {
  itemName: string;
  category: string;
  trend: string;
  status: 'optimal' | 'reorder';
}

export async function getDashboardSummary(token: string): Promise<DashboardSummary> {
  return apiGet<DashboardSummary>('/api/dashboard/summary', token);
}

export async function getRevenueChart(period: 'weekly' | 'monthly', token: string): Promise<RevenueChartDataPoint[]> {
  return apiGet<RevenueChartDataPoint[]>(`/api/dashboard/revenue-chart?period=${period}`, token);
}

export async function getRecentActivity(token: string): Promise<ActivityItem[]> {
  return apiGet<ActivityItem[]>('/api/dashboard/recent-activity', token);
}

export async function getRevenueReport(start: string, end: string, token: string): Promise<RevenueReport> {
  return apiGet<RevenueReport>(`/api/reports/revenue?start=${start}&end=${end}`, token);
}

export async function getRevenueBreakdown(token: string): Promise<RevenueBreakdownItem[]> {
  return apiGet<RevenueBreakdownItem[]>('/api/reports/revenue-breakdown', token);
}

export async function getBookingChannels(token: string): Promise<BookingChannelItem[]> {
  return apiGet<BookingChannelItem[]>('/api/reports/booking-channels', token);
}

export async function getTopInventory(token: string): Promise<TopInventoryItem[]> {
  return apiGet<TopInventoryItem[]>('/api/reports/top-inventory', token);
}

export interface Branch {
  id: number;
  name: string;
  code: string;
  address: string;
  city: string;
  country: string;
  phone: string;
  email: string;
  active: boolean;
  status: string;
}

export async function getBranches(token: string): Promise<Branch[]> {
  return apiGet<Branch[]>('/api/branches', token);
}

export interface MenuCategory {
  id: number;
  code: string;
  name: string;
  description: string;
  displayOrder: number;
  active: boolean;
}

export interface MenuItem {
  id: number;
  code: string;
  name: string;
  description: string;
  price: number;
  costPrice: number;
  imageUrl: string;
  available: boolean;
  isSignature: boolean;
  preparationTime: number;
  category: MenuCategory;
  createdAt: string;
  updatedAt: string;
}

export async function getMenuCategories(token: string): Promise<MenuCategory[]> {
  return apiGet<MenuCategory[]>('/api/menu/categories', token);
}

export async function getMenuItems(
  token: string,
  options?: { categoryId?: number; search?: string }
): Promise<MenuItem[]> {
  const params = new URLSearchParams();
  if (options?.categoryId) params.set('categoryId', String(options.categoryId));
  if (options?.search && options.search.trim()) params.set('search', options.search.trim());
  const qs = params.toString();
  return apiGet<MenuItem[]>(`/api/menu/items${qs ? `?${qs}` : ''}`, token);
}

// Room & Reservation types
export interface RoomTypeEntity {
  id: number;
  code: string;
  name: string;
  baseRate: number;
  beds: number;
  capacity: number;
  amenities: string[];
}

export type RoomStatus =
  | 'AVAILABLE'
  | 'OCCUPIED'
  | 'CLEANING'
  | 'RESERVED'
  | 'MAINTENANCE';

export type ReservationStatus =
  | 'PENDING'
  | 'CONFIRMED'
  | 'IN_HOUSE'
  | 'COMPLETED'
  | 'CANCELLED';

export type BookingType = 'STANDARD' | 'COUPLE' | 'EVENT';

export interface Room {
  id: number;
  roomNumber: string;
  floor: number;
  price: number;
  beds: number;
  status: RoomStatus;
  guestName: string | null;
  roomType: RoomTypeEntity | null;
  amenities: string[];
}

export interface Reservation {
  id: number;
  bookingCode: string;
  guestName: string;
  guestEmail: string | null;
  guestPhone: string | null;
  checkInDate: string;     // YYYY-MM-DD
  checkOutDate: string;    // YYYY-MM-DD
  checkInTime: string | null;  // HH:mm:ss or HH:mm
  checkOutTime: string | null;
  status: ReservationStatus;
  bookingType: BookingType;
  amount: number | null;
  notes: string | null;
  room: Room | null;
}

export interface RoomView {
  room: Room;
  currentReservation: Reservation | null;
}

export interface CreateRoomRequest {
  roomNumber: string;
  floor: number;
  price: number;
  beds: number;
  status?: RoomStatus;
  guestName?: string | null;
  amenities?: string[];
}

export interface CreateReservationRequest {
  bookingCode?: string | null;
  guestName: string;
  guestEmail?: string | null;
  guestPhone?: string | null;
  checkInDate: string;
  checkOutDate: string;
  checkInTime?: string | null;
  checkOutTime?: string | null;
  status?: ReservationStatus;
  bookingType?: BookingType;
  amount?: number | null;
  notes?: string | null;
}

export async function getRooms(token: string): Promise<RoomView[]> {
  return apiGet<RoomView[]>('/api/rooms', token);
}

export async function getRoomTypes(token: string): Promise<RoomTypeEntity[]> {
  return apiGet<RoomTypeEntity[]>('/api/room-types', token);
}

export async function createRoom(
  payload: CreateRoomRequest,
  roomTypeId: number | null,
  token: string
): Promise<Room> {
  return apiPost<Room>(
    `/api/rooms${roomTypeId ? `?roomTypeId=${roomTypeId}` : ''}`,
    payload,
    token
  );
}

export async function updateRoomStatus(
  id: number,
  status: RoomStatus,
  token: string
): Promise<Room> {
  return apiPost<Room>(`/api/rooms/${id}/status?status=${status}`, {}, token);
}

export async function getReservations(token: string): Promise<Reservation[]> {
  return apiGet<Reservation[]>('/api/reservations', token);
}

export async function createReservation(
  payload: CreateReservationRequest,
  roomId: number | null,
  token: string
): Promise<Reservation> {
  return apiPost<Reservation>(
    `/api/reservations${roomId ? `?roomId=${roomId}` : ''}`,
    payload,
    token
  );
}

export async function updateReservationStatus(
  id: number,
  status: ReservationStatus,
  token: string
): Promise<Reservation> {
  return apiPost<Reservation>(`/api/reservations/${id}/status?status=${status}`, {}, token);
}
