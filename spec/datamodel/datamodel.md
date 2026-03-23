# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Product | id, name, sku, category, unitPrice, reorderPoint, currentStock | Has many StockEvent |
| StockEvent | id, type (RECEIVED, ADJUSTMENT), quantity, reason, timestamp | Belongs to Product |
