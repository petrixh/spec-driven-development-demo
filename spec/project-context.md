# Project Context

A staff-facing book return processing station. A librarian scans a returned book and the system displays all relevant details, allowing them to process the return with the appropriate action.

## 1. Vision

Stateful single-screen workflow (scan → react → process)

A proof of concept example implmentation. Initially we will have a mocked data in a H2 database, later we might use real data. This part of the system focuses just on the returns of books, the lend out is done in a separate system (mocked data used for testing). 

## 2. Users

Librarians using a barcode scanner to scan the barcode of a returned book

In the first version, the librarian inserts the barcode, gets presented by the details of the book, it's lending history. The Librarian can mark the book returned from a button in the UI. Internally the system keeps track of books being returned. 


## 3. Constraints

- H2 in-memory database with mocked data (no external DB required)
- Java 21+ runtime (Vaadin 25 requirement)
- No external system integrations — lending data is mocked for demo purposes
- No formal compliance requirements; standard best practices apply

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
