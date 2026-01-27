# Circuit Breaker Testing Guide

## Interview Demo: How to Test Circuit Breaker

### Scenario 1: Normal Operation (Circuit CLOSED)
```bash
# Make a normal registration request
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "StrongP@ss123",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "1234567890"
  }'

# Expected: 200 OK, user registered
# Circuit State: CLOSED
```

### Scenario 2: Simulate User Service Failure (Circuit OPEN)
```bash
# Stop user-service
docker compose stop user-service

# Try to register (circuit will open after 5 failed calls)
for i in {1..6}; do
  curl -X POST http://localhost:8080/api/v1/auth/register \
    -H "Content-Type: application/json" \
    -d '{"username":"test'$i'", ...}'
done

# Expected after 5th call: 503 Service Unavailable
# Response: "User registration service is temporarily unavailable"
# Circuit State: OPEN (fails fast without calling user-service)
```

### Scenario 3: Check Circuit Breaker Status
```bash
# Check circuit breaker health
curl http://localhost:8080/actuator/health

# Expected response includes:
{
  "circuitBreakers": {
    "userService": {
      "status": "CIRCUIT_OPEN",
      "failureRate": "100.0%",
      "slowCallRate": "0.0%"
    }
  }
}
```

### Scenario 4: Service Recovery (Circuit HALF_OPEN → CLOSED)
```bash
# Start user-service
docker compose start user-service

# Wait 10 seconds for circuit to go HALF_OPEN

# Make test calls (3 permitted in HALF_OPEN)
curl -X POST http://localhost:8080/api/v1/auth/register ...

# If successful: Circuit goes CLOSED
# If fails: Circuit goes back to OPEN for another 10s
```

## Interview Questions You Should Be Ready For

### Q: "What happens when the circuit is OPEN?"
**A**: "Calls fail immediately with a ServiceUnavailableException, which returns HTTP 503. The circuit breaker doesn't even attempt to call the User Service, preventing resource exhaustion and cascading failures. This is called 'fail fast' behavior."

### Q: "How does the circuit know when to close?"
**A**: "After waitDurationInOpenState (10 seconds), it transitions to HALF_OPEN. In this state, it allows permittedNumberOfCallsInHalfOpenState (3 calls) as a test. If these succeed, the circuit closes. If they fail, it opens again for another 10 seconds."

### Q: "What's the difference between failure rate and slow call rate?"
**A**: "Failure rate tracks actual exceptions (FeignException, TimeoutException). Slow call rate tracks calls that exceed slowCallDurationThreshold (2 seconds) even if they eventually succeed. Both can trigger the circuit to open."

### Q: "Why use Circuit Breaker instead of just retry?"
**A**: "Retries make the problem worse when a service is down - you're hammering a failing service with more requests. Circuit breaker stops trying after detecting failures, giving the service time to recover while protecting your resources."

### Q: "What if you need the call to succeed even when circuit is open?"
**A**: "That's a design decision. For critical operations like payment processing, you might implement a queue-based system with eventual consistency instead of synchronous calls. For our registration flow, we chose fail-fast because we can't register a user without creating their profile in User Service."

## Metrics to Monitor in Production

```yaml
Key Metrics:
- Circuit State (CLOSED/OPEN/HALF_OPEN)
- Failure Rate (%)
- Slow Call Rate (%)
- Number of Calls (successful/failed/not_permitted)
- Average Response Time

Alert Thresholds:
- Alert when circuit opens (indicates service degradation)
- Alert if circuit stays open > 5 minutes (indicates serious issue)
- Alert if slow call rate > 30% (performance degradation)
```
