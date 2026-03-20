# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Product | id (Long), barcode (String, unique), name (String), price (BigDecimal) | Referenced by TransactionItem |
| Customer | id (Long), name (String), customerNumber (String, unique) | Referenced by Transaction |
| Transaction | id (Long), status (PENDING/COMPLETED/CANCELLED), paymentMethod (CARD/CASH), createdAt (LocalDateTime), completedAt (LocalDateTime) | Has many TransactionItems, optionally belongs to Customer |
| TransactionItem | id (Long), quantity (int), priceAtScan (BigDecimal) | Belongs to Transaction, references Product |
| User | id (Long), username (String, unique), password (String, encoded), role (ADMIN) | Spring Security user for admin login |
