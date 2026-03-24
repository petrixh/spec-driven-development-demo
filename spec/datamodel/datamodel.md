# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Book | id, title, author, isbn, callNumber | Has many LendingRecord |
| Patron | id, name, patronId, accountBalance | Has many LendingRecord |
| LendingRecord | id, checkoutDate, dueDate, returnDate, status, lateFee, damageAssessment, notes | Belongs to Book, belongs to Patron |

### Status values for LendingRecord

- `CHECKED_OUT` — book is currently lent out
- `RETURNED` — book returned on time
- `RETURNED_LATE` — book returned after due date
- `RETURNED_DAMAGED` — book returned with damage noted

### Late fee calculation

- $0.25 per day overdue (as shown in mockup)
- Fee calculated from dueDate to return date
