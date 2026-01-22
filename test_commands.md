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

curl -X POST http://localhost:5000/api/v1/auth/login \
-H "Content-Type: application/json" \
-d '{
"email": "test5@example.com",
"password": "Test@1234"
}'

curl -X GET http://localhost:5000/api/v1/job/get-all-jobs?page=0&size=10 \
-H 'accept: */*'

curl -X GET http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0NUBleGFtcGxlLmNvbSIsInJvbGUiOiJ1c2VyIiwic2Vzc2lvbklkIjoiMDBmMWUwNzQtYTNiNi00YTQwLWExNGUtNWU0ZTRhM2U3MjQ2IiwiaWF0IjoxNzY5MDcwNDcxLCJleHAiOjE3NjkwNzQwNzF9.h6D8wW9plj8yJb3nIQKeOOzJ48s37MG62K-VyBGnLBs"