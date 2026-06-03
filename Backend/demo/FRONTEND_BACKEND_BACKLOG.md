# Backend backlog from frontend screens

The current frontend is still mostly mock-driven, so these backend tasks should be built to replace local state and `mockData.ts` with real APIs.

## 1. Foundation first

- Add authentication and session handling.
- Add user, role, and permission models.
- Add branch/hotel context if the app will support more than one property.
- Add file upload support for images and documents.
- Add a shared API response and error format.

Suggested tables:

- `users`
- `roles`
- `permissions`
- `role_permissions`
- `user_roles`
- `branches`
- `media_files`

Suggested APIs:

- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/me`
- `GET /api/users`
- `POST /api/users`
- `GET /api/roles`
- `POST /api/roles`
- `PUT /api/roles/{id}`
- `DELETE /api/roles/{id}`

## 2. Hotel core

Build the hotel module next because the room grid, reservations flow, and room type settings are central to the app.

Suggested entities:

- Room types
- Rooms
- Reservations / bookings
- Guests / customers
- Room status history
- Room maintenance and housekeeping events

Suggested tables:

- `room_types`
- `rooms`
- `reservations`
- `reservation_guests`
- `room_status_history`
- `room_tasks`

Suggested APIs:

- `GET /api/room-types`
- `POST /api/room-types`
- `PUT /api/room-types/{id}`
- `DELETE /api/room-types/{id}`
- `GET /api/rooms`
- `POST /api/rooms`
- `PUT /api/rooms/{id}`
- `PATCH /api/rooms/{id}/status`
- `POST /api/rooms/{id}/check-in`
- `POST /api/rooms/{id}/check-out`
- `GET /api/reservations`
- `POST /api/reservations`
- `GET /api/reservations/{id}`
- `PATCH /api/reservations/{id}`
- `POST /api/reservations/{id}/cancel`

## 3. Restaurant and POS

The menu and POS screens need a proper order model, not just a static menu list.

Suggested entities:

- Menu categories
- Menu items
- Orders
- Order items
- Payment records
- Dining tables or POS terminals

Suggested tables:

- `menu_categories`
- `menu_items`
- `orders`
- `order_items`
- `payments`
- `tables`

Suggested APIs:

- `GET /api/menu-categories`
- `POST /api/menu-categories`
- `GET /api/menu-items`
- `POST /api/menu-items`
- `PUT /api/menu-items/{id}`
- `DELETE /api/menu-items/{id}`
- `GET /api/orders`
- `POST /api/orders`
- `GET /api/orders/{id}`
- `PATCH /api/orders/{id}/status`
- `POST /api/orders/{id}/items`
- `POST /api/orders/{id}/pay`

## 4. Inventory

Inventory should track stock changes over time, not just current quantities.

Suggested entities:

- Inventory items
- Stock movements
- Suppliers
- Purchase orders
- Reorder thresholds

Suggested tables:

- `inventory_items`
- `inventory_movements`
- `suppliers`
- `purchase_orders`
- `purchase_order_items`

Suggested APIs:

- `GET /api/inventory-items`
- `POST /api/inventory-items`
- `PUT /api/inventory-items/{id}`
- `PATCH /api/inventory-items/{id}/adjust-stock`
- `GET /api/inventory-movements`
- `POST /api/purchase-orders`
- `GET /api/purchase-orders`

## 5. Customers and staff

The customer and staff screens need persisted profiles, not only display data.

Suggested entities:

- Customers / guests
- Loyalty tiers
- Staff members
- Departments
- Shifts
- Employment documents

Suggested tables:

- `customers`
- `customer_loyalty`
- `staff`
- `departments`
- `shifts`
- `staff_documents`

Suggested APIs:

- `GET /api/customers`
- `POST /api/customers`
- `PUT /api/customers/{id}`
- `GET /api/staff`
- `POST /api/staff`
- `PUT /api/staff/{id}`
- `PATCH /api/staff/{id}/status`

## 6. Reports and dashboard data

The dashboard and reports pages should come from aggregated endpoints rather than frontend-calculated mock values.

Suggested endpoints:

- `GET /api/dashboard/summary`
- `GET /api/dashboard/recent-activity`
- `GET /api/dashboard/revenue-chart`
- `GET /api/reports/revenue`
- `GET /api/reports/occupancy`
- `GET /api/reports/restaurant-sales`
- `GET /api/reports/inventory-alerts`
- `POST /api/reports/export`

## 7. Settings and auditability

The settings screens imply a few configuration tables that should be stored server-side.

Suggested entities:

- App settings
- Notification settings
- Localization settings
- Audit log

Suggested tables:

- `app_settings`
- `notification_settings`
- `localization_settings`
- `audit_logs`

Suggested APIs:

- `GET /api/settings`
- `PUT /api/settings`
- `GET /api/audit-logs`

## Recommended build order

1. Auth, users, roles, and permissions.
2. Room types, rooms, and reservations.
3. Customers and staff.
4. Menu, POS, orders, and payments.
5. Inventory and purchase orders.
6. Reports and dashboard aggregates.
7. Settings and audit logs.

## Notes from the frontend

- `RoomManagement`, `Reservations`, `MenuManagement`, `POS`, `Customers`, `Staff`, `Inventory`, `Reports`, `Roles`, `RoomTypes`, and `Settings` are all currently UI-driven and need real backend endpoints.
- `mockData.ts` is the clearest replacement target for server data.
- The current frontend already suggests image uploads for menu items and staff profiles, so the backend should support media uploads early.