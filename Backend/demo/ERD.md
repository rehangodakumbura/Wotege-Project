# Frontend-Inferred ER Diagram

This diagram is inferred from the frontend mock data and page models.

```mermaid
erDiagram
    ROOM_TYPE ||--o{ ROOM : defines
    CUSTOMER ||--o{ RESERVATION : makes
    ROOM ||--o{ RESERVATION : booked_for
    ROLE ||--o{ STAFF : assigns
    MENU_CATEGORY ||--o{ MENU_ITEM : groups

    ROOM_TYPE {
        string id
        string name
        decimal baseRate
        int beds
        int capacity
        string[] amenities
    }

    ROOM {
        string id
        string type
        string status
        int floor
        decimal price
        int beds
        string[] amenities
        string guest
        string checkout
        string checkin
        string roomTypeId
    }

    CUSTOMER {
        string id
        string name
        string email
        string phone
        int visits
        string spent
        string tier
    }

    RESERVATION {
        string id
        string guest
        string room
        string checkIn
        string checkOut
        string amount
        string status
        string customerId
        string roomId
    }

    MENU_CATEGORY {
        string name
    }

    MENU_ITEM {
        string id
        string name
        decimal price
        string category
        string image
        string status
        int orders
        string categoryId
    }

    INVENTORY_ITEM {
        string id
        string item
        string category
        int stock
        string unit
        int minThreshold
        string status
        string lastRestock
    }

    ROLE {
        string id
        string name
        int users
        string description
        boolean isSystem
    }

    STAFF {
        string id
        string name
        string role
        string department
        string status
        string shift
        string roleId
    }
```

Notes:

- `roomTypeId`, `customerId`, `roomId`, `categoryId`, and `roleId` are inferred foreign keys that are not explicitly present in the current frontend mock data.
- The POS screen suggests order data, but there is no persisted order model in the frontend yet, so it is not included here.