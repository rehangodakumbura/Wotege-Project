/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';
import Login from './pages/auth/Login';
import { MainLayout } from './components/layout/MainLayout';
import SuperAdminDashboard from './pages/dashboard/SuperAdminDashboard';
import RoomManagement from './pages/hotel/RoomManagement';
import POS from './pages/pos/POS';
import Reservations from './pages/hotel/Reservations';
import MenuManagement from './pages/restaurant/MenuManagement';
import Customers from './pages/customers/Customers';
import Staff from './pages/staff/Staff';
import Reports from './pages/reports/Reports';
import Settings from './pages/settings/Settings';
import Packages from './pages/hotel/Packages';
import Inventory from './pages/inventory/Inventory';
import Roles from './pages/settings/Roles';
import RoomTypes from './pages/settings/RoomTypes';
import NotFound from './pages/NotFound';
import { ToastProvider } from './components/ui/Toast';
import { SplashScreen } from './components/ui/SplashScreen';
import { AuthProvider } from './context/AuthContext';

export default function App() {
  const [showSplash, setShowSplash] = useState(true);

  if (showSplash) {
    return <SplashScreen onComplete={() => setShowSplash(false)} />;
  }

  return (
    <AuthProvider>
    <ToastProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          
          <Route path="/" element={<Navigate to="/login" replace />} />
          
          <Route element={<MainLayout />}>
            <Route path="/dashboard" element={<SuperAdminDashboard />} />
            <Route path="/hotel/rooms" element={<RoomManagement />} />
            <Route path="/hotel/reservations" element={<Reservations />} />
            <Route path="/hotel/packages" element={<Packages />} />
            
            <Route path="/restaurant/menu" element={<MenuManagement />} />
            <Route path="/restaurant/pos" element={<POS />} />
            <Route path="/pos" element={<Navigate to="/restaurant/pos" replace />} />
            <Route path="/reservations" element={<Navigate to="/hotel/reservations" replace />} />
            
            <Route path="/inventory" element={<Inventory />} />
            
            <Route path="/customers" element={<Customers />} />
            <Route path="/staff" element={<Staff />} />
            <Route path="/reports" element={<Reports />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/settings/roles" element={<Roles />} />
            <Route path="/settings/room-types" element={<RoomTypes />} />
          </Route>
          
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </ToastProvider>
    </AuthProvider>
  );
}
