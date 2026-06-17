import api from '@/services/api';

export interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  available: boolean;
  status: string;
  orderCount: number;
  categoryId: number;
  categoryName: string;
  createdAt: string;
  updatedAt: string;
}

export interface MenuItemRequest {
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
  available?: boolean;
  categoryId: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export const getMenuItems = async (): Promise<MenuItem[]> => {
  const response = await api.get<ApiResponse<MenuItem[]>>('/api/menu-items');
  return response.data.data;
};

export const getMenuItemById = async (id: number): Promise<MenuItem> => {
  const response = await api.get<ApiResponse<MenuItem>>(`/api/menu-items/${id}`);
  return response.data.data;
};

export const getMenuItemsByCategory = async (categoryId: number): Promise<MenuItem[]> => {
  const response = await api.get<ApiResponse<MenuItem[]>>(`/api/menu-items/category/${categoryId}`);
  return response.data.data;
};

export const searchMenuItems = async (keyword: string): Promise<MenuItem[]> => {
  const response = await api.get<ApiResponse<MenuItem[]>>('/api/menu-items/search', {
    params: { keyword },
  });
  return response.data.data;
};

export const createMenuItem = async (request: MenuItemRequest): Promise<MenuItem> => {
  const response = await api.post<ApiResponse<MenuItem>>('/api/menu-items', request);
  return response.data.data;
};

export const updateMenuItem = async (id: number, request: MenuItemRequest): Promise<MenuItem> => {
  const response = await api.put<ApiResponse<MenuItem>>(`/api/menu-items/${id}`, request);
  return response.data.data;
};

export const deleteMenuItem = async (id: number): Promise<void> => {
  await api.delete(`/api/menu-items/${id}`);
};

export const toggleAvailability = async (id: number): Promise<MenuItem> => {
  const response = await api.patch<ApiResponse<MenuItem>>(`/api/menu-items/${id}/status`);
  return response.data.data;
};

export const increaseOrderCount = async (id: number): Promise<MenuItem> => {
  const response = await api.patch<ApiResponse<MenuItem>>(`/api/menu-items/${id}/order-count`);
  return response.data.data;
};

export const uploadMenuImage = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append('file', file);
  const response = await api.post<ApiResponse<{ imageUrl: string }>>('/api/menu-items/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data.data.imageUrl;
};
