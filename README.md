# Banking Transaction API

## Overview

A REST API built to simulate core banking operations, account creation, deposits,
withdrawals, and transfers between accounts.

Built with financial data integrity in mind: every operation is logged as an immutable
transaction record, balances use exact decimal precision, and business logic is fully
validated before any data is mutated. Understanding how these things work, and how to
implement them correctly matters when handling real user funds.

---

## Tech Stack

**Java** — Industry standard for enterprise banking systems. Strongly typed,
mature ecosystem, and built for long-running server-side applications.

**Spring Boot** — Streamlines production-grade Java development by auto-configuring
the application based on included dependencies. Eliminates manual setup and complex
XML configuration. Starter packages bundle compatible libraries into single dependencies,
preventing version mismatch issues entirely.

**Spring Data JPA / Hibernate** — Maps Java classes directly to database tables,
eliminating boilerplate SQL. The entity class is the schema.

**H2 (In-Memory Database)** — Zero installation, zero external infrastructure.
Spins up fresh and isolated on every run, making testing fast and repeatable.

**BigDecimal** — Computers represent numbers in binary (base 2), but financial
figures are decimal (base 10). Floating point types like `double` cannot represent
most decimal fractions precisely. At banking scale, even a fraction of a cent lost
to rounding compounds into a serious integrity problem. BigDecimal stores exact
decimal values — non-negotiable for financial software.

---

## Architecture

This project follows the **Controller → Service → Repository** pattern. Each layer
has one job and only communicates with the layer directly below it.

**Controller** — Entry point of the application. Handles incoming HTTP requests,
parses user input, and returns HTTP responses. Contains no business logic.

**Service** — The brain of the application. All business logic lives here — balance
updates, transfer validation, transaction logging. The Service has no knowledge of
how data is stored.

**Repository** — Abstracts all database access. Provides methods like `save()` and
`findById()` without exposing underlying SQL or database implementation details.

**Model** — Represents the database schema. Each entity class maps directly to a
database table. The class is the schema.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven (included via `./mvnw` wrapper — no installation needed)
- Postman (for testing endpoints)

### Running the App

1. Clone the repository:

```bash
git clone https://github.com/Ishan-Khurram/Banking-Transaction-API
cd Banking-Transaction-API
```

2. Start the application:

```bash
./mvnw spring-boot:run
```

3. If successful, you will see:

```
Started BankingApiApplication in X seconds
```

### Viewing the Database

Navigate to `http://localhost:8080/h2-console` and connect with:

- **JDBC URL:** `jdbc:h2:mem:bankingdb`
- **Username:** `sa`
- **Password:** (leave empty)

To inspect your data, run queries directly in the console:

```sql
SELECT * FROM ACCOUNTS;
SELECT * FROM TRANSACTIONS;
```

---

## API Endpoints

### Create Account

**POST** `/accounts`

Creates a new account with a zero balance.

**Request Body**

```json
"Ishan"
```

**Example Response**

```json
{
  "id": 1,
  "ownerName": "Ishan",
  "balance": 0.0,
  "createdAt": "2026-05-04T16:46:53.238684"
}
```

---

### Get Account

**GET** `/accounts/{id}`

Retrieves a single account by ID.

**Path Variable**

- `id` — the account ID

**Example Request**

```
GET /accounts/1
```

**Example Response**

```json
{
  "id": 1,
  "ownerName": "Ishan",
  "balance": 300.0,
  "createdAt": "2026-05-04T16:46:53.238684"
}
```

**Error Response**

- `404` — Account not found

---

### Deposit

**POST** `/accounts/{id}/deposit`

Deposits funds into an account.

**Path Variable**

- `id` — the account ID

**Request Body**

```json
500.0
```

**Example Response**

```json
{
  "id": 1,
  "ownerName": "Ishan",
  "balance": 500.0,
  "createdAt": "2026-05-04T16:46:53.238684"
}
```

**Error Response**

- `404` — Account not found

---

### Withdraw

**POST** `/accounts/{id}/withdrawal`

Withdraws funds from an account. Requires sufficient balance.

**Path Variable**

- `id` — the account ID

**Request Body**

```json
200.0
```

**Example Response**

```json
{
  "id": 1,
  "ownerName": "Ishan",
  "balance": 300.0,
  "createdAt": "2026-05-04T16:46:53.238684"
}
```

**Error Responses**

- `404` — Account not found
- `400` — Insufficient funds

---

### Transfer

**POST** `/accounts/transfer`

Transfers funds between two accounts. Validates sufficient balance before executing.

**Request Body**

```json
{
  "sourceId": 1,
  "destinationId": 2,
  "amount": 100.0
}
```

**Example Response**

```json
[
  {
    "id": 1,
    "ownerName": "Ishan",
    "balance": 200.0,
    "createdAt": "2026-05-04T16:46:53.238684"
  },
  {
    "id": 2,
    "ownerName": "Samira",
    "balance": 100.0,
    "createdAt": "2026-05-04T16:51:29.276305"
  }
]
```

**Error Responses**

- `404` — Source or destination account not found
- `400` — Insufficient funds

---

### Get Transaction History

**GET** `/accounts/{id}/transaction-history`

Returns all transactions associated with an account.

**Path Variable**

- `id` — the account ID

**Example Request**

```
GET /accounts/1/transaction-history
```

**Example Response**

```json
[
  {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 500.0,
    "timestamp": "2026-05-04T16:50:10.164528"
  },
  {
    "id": 2,
    "type": "WITHDRAWAL",
    "amount": 200.0,
    "timestamp": "2026-05-04T16:50:25.98162"
  }
]
```

**Error Response**

- `404` — Account not found

---

## Design Decisions

**BigDecimal over double**
In the financial world, accuracy with numbers is everything. Floating point types
like `double` represent numbers in binary, which cannot precisely express most decimal
fractions. Even minor rounding errors compound into serious integrity problems at
banking scale. BigDecimal stores exact decimal values — non-negotiable when handling
user funds.

**Immutable Transaction Records**
Transactions are historical facts, they should never be editable. Every deposit,
withdrawal, and transfer is logged as a permanent record. Mutable logs allow for
inaccurate records, which in a financial context creates serious compliance and
auditing problems.

**Validate Before Mutating**
Before any operation touches a balance, the application first verifies the account
exists and that the operation is valid, sufficient funds for withdrawals and
transfers, valid account IDs before a transfer executes. Without these checks, funds
could be dropped into negative balances or lost entirely. The only exception would
be accounts with overdraft protection enabled.

**Audit Trail on Every Operation**
Every operation that touches a balance produces a corresponding Transaction record.
This creates a full history of account activity that can be used to reconstruct
exactly what happened, when, and by how much. This is essential for debugging, compliance,
and user transparency.

**Layered Architecture (Controller → Service → Repository)**
Each layer has one job and only communicates with the layer directly below it.
Business logic lives exclusively in the Service layer. If something breaks or needs
to change, you know exactly where to look. This also allows different parts of the
system to be developed, tested, and modified independently without breaking anything else.
