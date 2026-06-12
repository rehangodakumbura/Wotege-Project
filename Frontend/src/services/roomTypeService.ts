import api from './api';

export interface RoomType {
  id: number;
  code: string;
  name: string;
  baseRate: number;
  beds: number;
  capacity: number;
  amenities: string[];
}

export interface RoomTypePayload {
  code: string;
  name: string;
  baseRate: number;
  beds: number;
  capacity: number;
  amenities: string[];
}

export async function getRoomTypes(): Promise<RoomType[]> {
  const res = await api.get('/api/room-types');
  return res.data;
}

export async function createRoomType(data: RoomTypePayload): Promise<RoomType> {
  const res = await api.post('/api/room-types', data);
  return res.data;
}

export async function updateRoomType(id: number, data: RoomTypePayload): Promise<RoomType> {
  const res = await api.put(`/api/room-types/${id}`, data);
  return res.data;
}

export async function deleteRoomType(id: number): Promise<void> {
  await api.delete(`/api/room-types/${id}`);
}
