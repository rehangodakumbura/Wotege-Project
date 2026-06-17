import api from '@/services/api';

export interface Category {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export const getCategories = async (): Promise<Category[]> => {
  const response = await api.get<ApiResponse<Category[]>>('/api/categories');
  return response.data.data;
};

export const getCategoryById = async (id: number): Promise<Category> => {
  const response = await api.get<ApiResponse<Category>>(`/api/categories/${id}`);
  return response.data.data;
};

export const createCategory = async (name: string): Promise<Category> => {
  const response = await api.post<ApiResponse<Category>>('/api/categories', { name });
  return response.data.data;
};

export const updateCategory = async (id: number, name: string): Promise<Category> => {
  const response = await api.put<ApiResponse<Category>>(`/api/categories/${id}`, { name });
  return response.data.data;
};

export const deleteCategory = async (id: number): Promise<void> => {
  await api.delete(`/api/categories/${id}`);
};
