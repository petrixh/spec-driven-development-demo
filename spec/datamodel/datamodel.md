# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Book | id, title, author, isbn, callNumber, lateFeePerDay | Has many LendingRecord |
| Patron | id, name, patronId, accountBalance | Has many LendingRecord |
| LendingRecord | id, checkoutDate, dueDate, returnDate, status, lateFee, damageAssessment, notes | Belongs to Book, belongs to Patron |

### Status values for LendingRecord

- `CHECKED_OUT` — book is currently lent out
- `RETURNED` — book returned on time
- `RETURNED_LATE` — book returned after due date
- `RETURNED_DAMAGED` — book returned with damage noted

### Late fee calculation

- Late fee rate is per book (`Book.lateFeePerDay`) — some books have higher fees than others
- Fee calculated as `lateFeePerDay × days overdue` (from dueDate to return date)
