# Postman API Collection Setup Guide

This guide will help you import and use the Postman collection for testing the Online Banking System API.

## Files Included

1. **Online_Banking_System.postman_collection.json** - Complete API collection
2. **Postman_Environment.postman_environment.json** - Environment variables

## Setup Instructions

### Step 1: Import Collection

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `Online_Banking_System.postman_collection.json`
5. Click **Import**

### Step 2: Import Environment

1. Click **Import** button again
2. Select **File** tab
3. Choose `Postman_Environment.postman_environment.json`
4. Click **Import**
5. Select the environment from the dropdown (top right): **Online Banking System Environment**

### Step 3: Verify Base URL

The default base URL is set to `http://localhost:8080`. If your server runs on a different port, update the `base_url` variable in the environment.

## Using the Collection

### Authentication Flow

1. **Register User** (Optional)
   - Endpoint: `POST /api/auth/register`
   - This will automatically save the JWT token to environment variables
   - Update the username/email if the user already exists

2. **Login**
   - Endpoint: `POST /api/auth/login`
   - This will automatically save the JWT token
   - All subsequent requests will use this token automatically

### Testing Workflow

#### Complete Test Flow:

1. **Register a User**
   ```
   POST /api/auth/register
   Body: {
     "username": "john_doe",
     "email": "john@example.com",
     "password": "password123",
     "fullName": "John Doe"
   }
   ```
   - Token is automatically saved

2. **Create Account**
   ```
   POST /api/accounts
   Body: {
     "accountType": "SAVINGS"
   }
   ```
   - Account number is automatically saved

3. **Register Another User** (for transfer testing)
   - Register a second user with different credentials
   - Create an account for this user
   - Note the account number and set it as `to_account_number` in environment

4. **Get Account Balance**
   ```
   GET /api/accounts/{account_number}/balance
   ```

5. **Transfer Funds**
   ```
   POST /api/transactions/transfer
   Body: {
     "fromAccountNumber": "{{account_number}}",
     "toAccountNumber": "{{to_account_number}}",
     "amount": 1000.00,
     "description": "Payment for services"
   }
   ```

6. **Get Transaction History**
   ```
   GET /api/transactions/history/{account_number}
   ```

## Environment Variables

The collection uses these variables (automatically set by test scripts):

- `base_url` - API base URL (default: http://localhost:8080)
- `jwt_token` - JWT authentication token (auto-set after login/register)
- `account_number` - Account number (auto-set after account creation)
- `to_account_number` - Destination account for transfers (set manually)
- `transaction_id` - Transaction ID (set manually if needed)
- `user_id` - User ID (auto-set after login/register)

## Features

✅ **Auto Token Management** - JWT tokens are automatically saved after login/register
✅ **Auto Account Number** - Account numbers are saved after account creation
✅ **Bearer Token Auth** - All authenticated endpoints use Bearer token automatically
✅ **Pre-configured Requests** - All endpoints are ready to use with example data
✅ **Test Scripts** - Automatic variable extraction from responses

## Example Test Scenarios

### Scenario 1: New User Registration and Account Creation

1. Register User → Get token
2. Create SAVINGS Account → Get account number
3. Check Balance (should be 0)
4. Create CHECKING Account
5. Get All Accounts

### Scenario 2: Fund Transfer

1. Login as User 1
2. Create Account 1
3. Login as User 2 (or register new)
4. Create Account 2
5. Set `to_account_number` = Account 2 number
6. Transfer funds from Account 1 to Account 2
7. Check balances of both accounts
8. View transaction history

### Scenario 3: Transaction History

1. Login
2. Get account number
3. Perform multiple transfers
4. Get transaction history
5. Verify all transactions are listed

## Troubleshooting

### Token Not Working
- Make sure you've run Login or Register first
- Check that the environment is selected
- Verify token is saved in environment variables

### Account Number Not Found
- Create an account first
- Check that account_number variable is set
- Use the account number from "Get All User Accounts" response

### Connection Refused
- Ensure Spring Boot application is running
- Check the port (default: 8080)
- Verify base_url in environment variables

### 401 Unauthorized
- Token might be expired (default: 24 hours)
- Re-login to get a new token
- Check Authorization header is set correctly

## Notes

- All authenticated endpoints require a valid JWT token
- Tokens expire after 24 hours (configurable in application.properties)
- Account numbers are auto-generated
- Transaction IDs are auto-generated
- All amounts are in BigDecimal format (use decimal numbers)

