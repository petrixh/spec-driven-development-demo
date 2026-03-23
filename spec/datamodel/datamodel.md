# Data Model

> Entity definitions and relationships. Evolves as features are added.

## Entities

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| **Ticket** | id, title, description, category, priority, status, createdDate, updatedDate | Belongs to Customer (createdBy), optionally assigned to Agent (assignedTo), has many Comments |
| **Comment** | id, text, createdDate | Belongs to Ticket, belongs to User (author) |
| **User** | id, name, email, role | Has many Tickets (created), has many Tickets (assigned), has many Comments |

## Enumerations

| Enum | Values |
|------|--------|
| **Category** | `GENERAL`, `TECHNICAL`, `BILLING`, `ACCESS` |
| **Priority** | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| **Status** | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` |
| **Role** | `CUSTOMER`, `ADMIN` |

## Status Transitions

```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
                   ↘ OPEN (reopen)
```

- New tickets start as `OPEN`
- Only agents can change status
- `RESOLVED` tickets can be reopened (→ `OPEN`) or closed (→ `CLOSED`)
- `CLOSED` is a terminal state
