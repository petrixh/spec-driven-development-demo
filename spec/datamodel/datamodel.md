# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| **Ticket** | id, name, description, transitMode (BUS/TRAIN/METRO/FERRY), ticketType (SINGLE_RIDE/DAY_PASS), price | Has many PurchaseOrder |
| **PurchaseOrder** | id, confirmationCode (UUID), quantity, totalPrice, cardLastFour, purchasedAt | Belongs to Ticket |

## Entity Details

### Ticket

Represents a purchasable transit ticket. Seeded on application startup.

| Field | Type | Notes |
|-------|------|-------|
| id | Long | Auto-generated primary key |
| name | String | Display name (e.g., "Bus Single Ride") |
| description | String | Short description shown on detail page |
| transitMode | Enum | BUS, TRAIN, METRO, FERRY |
| ticketType | Enum | SINGLE_RIDE, DAY_PASS |
| price | BigDecimal | Ticket price |

### PurchaseOrder

Represents a completed (simulated) ticket purchase.

| Field | Type | Notes |
|-------|------|-------|
| id | Long | Auto-generated primary key |
| confirmationCode | UUID | Unique validation code shown on confirmation page |
| quantity | Integer | Number of tickets purchased |
| totalPrice | BigDecimal | quantity × ticket price |
| cardLastFour | String | Last 4 digits of the entered credit card (for display only) |
| purchasedAt | LocalDateTime | Timestamp of purchase |
| ticket_id | Long (FK) | References the purchased Ticket |
