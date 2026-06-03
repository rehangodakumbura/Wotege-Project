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
