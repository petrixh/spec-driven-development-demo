# Data Model

> Entity definitions and relationships. Evolves as features are added.

## Entities

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| **Movie** | id, title, description, durationMinutes, posterFileName | Has many Shows |
| **ScreeningRoom** | id, name, rows, seatsPerRow | Has many Shows |
| **Show** | id, dateTime | Belongs to Movie, belongs to ScreeningRoom, has many Tickets |
| **Ticket** | id, seatRow, seatNumber, customerName, customerEmail, purchasedAt | Belongs to Show |

## Entity Details

### Movie
| Field | Type | Constraints |
|-------|------|-------------|
| id | Long | PK, auto-generated |
| title | String | Required, max 200 |
| description | String | Optional, max 2000 |
| durationMinutes | Integer | Required, > 0 |
| posterFileName | String | Optional — file name referencing an image in `posters/` |

### ScreeningRoom
| Field | Type | Constraints |
|-------|------|-------------|
| id | Long | PK, auto-generated |
| name | String | Required (e.g., "Room 1") |
| rows | Integer | Required, > 0 |
| seatsPerRow | Integer | Required, > 0 |

### Show
| Field | Type | Constraints |
|-------|------|-------------|
| id | Long | PK, auto-generated |
| dateTime | LocalDateTime | Required, must be in the future when created |
| movie | Movie | Required (FK) |
| screeningRoom | ScreeningRoom | Required (FK) |

### Ticket
| Field | Type | Constraints |
|-------|------|-------------|
| id | Long | PK, auto-generated |
| seatRow | Integer | Required, 1-based |
| seatNumber | Integer | Required, 1-based |
| customerName | String | Required |
| customerEmail | String | Required, valid email |
| purchasedAt | LocalDateTime | Auto-set on creation |
| show | Show | Required (FK) |
| Unique constraint: (show, seatRow, seatNumber) — no double-booking |

## Sample Data

On startup, seed the database with:

- **8 movies** using poster images from `posters/`:
  - "AI Developer 2" (`ai-developer-2.png`)
  - "The Gardening Incident" (`gardening-incident.png`)
  - "Living in the Forest" (`living-in-the-forest.png`)
  - "The Null Mistake" (`null-mistake-java.png`)
  - "Pink Elephants" (`pink-elephants.png`)
  - "Reindeer Hunter" (`reindeer-hunter.png`)
  - "Sleeping with the Fishes" (`sleeping-with-the-fishes.png`)
  - "Threading a Needle" (`threading-a-needle.png`)
- **3 screening rooms**: Room 1 (8 rows x 10 seats), Room 2 (6 rows x 8 seats), Room 3 (5 rows x 6 seats)
- **Shows** scheduled over the next 7 days, spread across rooms and movies
