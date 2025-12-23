curl -X POST http://localhost:5000/api/v1/auth/register \
-H "Content-Type: application/json" \
-d '{
    "username": "testuser5",
    "password": "Test@1234",
    "email": "test5@example.com",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "1234567890"
}'