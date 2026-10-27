# Online Banking System - Spring Boot

A secure backend banking application built with Spring Boot to manage users, accounts, and financial transactions.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL**
- **Spring Security** (JWT Authentication)
- **REST APIs**
- **Maven**

## Features

- ✅ User registration and login with secure authentication
- ✅ Account creation (SAVINGS/CHECKING)
- ✅ Fund transfer between accounts
- ✅ Balance inquiry
- ✅ Transaction history tracking
- ✅ Transaction management for data consistency
- ✅ Spring Security with JWT tokens
- ✅ Input validation
- ✅ RESTful API design
- ✅ Global exception handling

## Project Structure

```
online-banking-system/
├── src/
│   ├── main/
│   │   ├── java/com/banking/
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── service/         # Business Logic
│   │   │   ├── repository/      # Data Access Layer
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── security/        # Security Configuration
│   │   │   ├── exception/       # Exception Handling
│   │   │   └── util/            # Utility Classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE banking_db;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Navigate to the project directory:
```bash
cd online-banking-system
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

- **POST** `/api/auth/register` - Register a new user
- **POST** `/api/auth/login` - Login and get JWT token

### Accounts

- **POST** `/api/accounts` - Create a new account (Requires Authentication)
- **GET** `/api/accounts` - Get all accounts for current user (Requires Authentication)
- **GET** `/api/accounts/{accountNumber}` - Get account details (Requires Authentication)
- **GET** `/api/accounts/{accountNumber}/balance` - Get account balance (Requires Authentication)

### Transactions

- **POST** `/api/transactions/transfer` - Transfer funds between accounts (Requires Authentication)
- **GET** `/api/transactions/history/{accountNumber}` - Get transaction history (Requires Authentication)
- **GET** `/api/transactions/{transactionId}` - Get transaction details (Requires Authentication)

### User Profile

- **GET** `/api/users/profile` - Get current user profile (Requires Authentication)

## Authentication

The application uses JWT (JSON Web Token) for authentication. After successful login, include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Example API Requests

### Register User
```json
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "fullName": "John Doe"
}
```

### Login
```json
POST /api/auth/login
{
  "username": "john_doe",
  "password": "password123"
}
```

### Create Account
```json
POST /api/accounts
Authorization: Bearer <token>
{
  "accountType": "SAVINGS"
}
```

### Transfer Funds
```json
POST /api/transactions/transfer
Authorization: Bearer <token>
{
  "fromAccountNumber": "ACC1234567890",
  "toAccountNumber": "ACC0987654321",
  "amount": 1000.00,
  "description": "Payment for services"
}
```

## Security Features

- Password encryption using BCrypt
- JWT-based authentication
- Role-based access control
- Input validation
- SQL injection prevention (JPA)
- CSRF protection disabled for stateless JWT authentication

## Transaction Management

All fund transfers are wrapped in `@Transactional` to ensure:
- Atomicity: All operations succeed or fail together
- Consistency: Database remains in a valid state
- Isolation: Concurrent transactions don't interfere
- Durability: Committed transactions persist

## Error Handling

The application includes comprehensive exception handling:
- `ResourceNotFoundException` - 404 Not Found
- `InsufficientBalanceException` - 400 Bad Request
- `BadRequestException` - 400 Bad Request
- Validation errors - 400 Bad Request with field details
- Global exception handler for all other exceptions

## License

This project is for educational purposes.

