**Banking Transaction Processor**

A simple Spring Boot service for processing banking transactions across multiple accounts.

**Requirements**

The service supports:

- Creating accounts with unique IDs
- Depositing money
- Withdrawing money
- Transferring money between accounts
- Preventing overdrafts
- Rejecting invalid transaction amounts
- Maintaining a transaction ledger with timestamps
- Querying account balances
- Querying transaction history

**Technology**

- Java 17
- Spring Boot
- Spring Web
- Bean Validation
- JUnit 5
- Mockito
- Maven

**Architecture**

The application follows a simple layered architecture:

Controller
    ↓
Service
    ↓
Repository
    ↓
Domain

**Domain**

`Account` owns its balance and transaction history.

Business invariants such as:

- amount must be greater than zero
- withdrawal cannot exceed balance

are enforced inside the domain model.

**Service**

Services coordinate application operations such as
deposit, withdrawal and transfer.

**Repository**

The current implementation uses an in-memory repository
backed by `ConcurrentHashMap`.

**API**

Create account

POST /accounts

Response:

```json
{
  "id": "account-id",
  "balance": 0
}

**Get balance**

GET /accounts/{accountId}/balance
Deposit
POST /accounts/{accountId}/deposits

**Design Decisions**
**In-memory persistence**

The exercise was timeboxed to 3-4 hours, so the implementation uses an in-memory repository rather than introducing a database.
This keeps the focus on the required transaction behaviour,domain modelling and testing.

**Transaction atomicity**

For a production implementation backed by a relational database,transfers should execute within a database transaction so that
both account updates succeed or fail together.

The current in-memory implementation does not provide the same transactional guarantees as a database.

**Concurrency**

ConcurrentHashMap provides thread-safe access to the accountcollection, but it does not by itself make a transfer between
two accounts atomic.

A production implementation would require appropriate database transaction/concurrency control.

**What I Would Improve With More Time**

With additional time I would consider:
1. Persistent storage using PostgreSQL.
2. Database transactions for atomic transfers.
3. Optimistic/pessimistic locking for concurrent transfers.
4. Database migrations using Liquibase or Flyway.
5. API documentation using OpenAPI.
6. More integration tests.
7. Authentication and authorization.
8. Observability and structured logging.
