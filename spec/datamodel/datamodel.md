# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Patient | id, firstName, lastName, dateOfBirth, gender, phone, email, address | Has many Visit |
| Visit | id, date, reason, doctorName, notes | Belongs to Patient |
