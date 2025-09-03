create user flow

client -> auth service validations and hashing -> user service (create the user and return id) -> auth service(store hashed password + userId) -> auto login with session creation -> client receive the response (user info with jwt token)
