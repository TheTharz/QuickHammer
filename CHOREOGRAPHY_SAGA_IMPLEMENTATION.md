# Choreography Saga Pattern Implementation

## Overview
Implemented a **Choreography Saga Pattern** for the **Bid Acceptance Flow** in QuickHammer microservices.

### What is Choreography Saga?
A saga pattern where each service publishes events and listens to other services' events, making decisions and taking actions autonomously. There's **no central coordinator** - services choreograph their actions through events.

---

## Saga Flow: Bid Acceptance with Compensation

### Happy Path Flow

```
1. BID SERVICE (Initiator)
   ├─ User accepts a bid
   ├─ Update bid status to ACCEPTED
   ├─ Publish: BidAcceptedEvent ─────────┐
   ├─ Reject other bids                  │
   ├─ Publish: BidRejectedEvent (×N)     │
   └─ Publish: UserBidStatisticsEvent    │
                                          │
2. JOB SERVICE (Participant)              │
   ├─ Listen: BidAcceptedEvent ◄──────────┘
   ├─ Validate job status (must be OPEN)
   ├─ Update job to IN_PROGRESS
   ├─ Assign freelancer to job
   └─ Publish: JobAssignedEvent ──────────┐
                                           │
3. NOTIFICATION SERVICE (Participant)      │
   ├─ Listen: JobAssignedEvent ◄───────────┤
   ├─ Send email to freelancer             │
   └─ Send email to client                 │
                                           │
4. NOTIFICATION SERVICE (Participant)      │
   ├─ Listen: BidRejectedEvent ◄───────────┤
   └─ Send rejection email to bidders      │
                                           │
5. USER SERVICE (Participant)              │
   ├─ Listen: UserBidStatisticsEvent ◄─────┘
   └─ Update user win/loss statistics
```

### Compensation Flow (When Job Assignment Fails)

```
2. JOB SERVICE (Failure detected)
   ├─ Job assignment fails (validation error, DB error, etc.)
   └─ Publish: BidAcceptanceRollbackEvent ────┐
                                               │
1. BID SERVICE (Compensation)                  │
   ├─ Listen: BidAcceptanceRollbackEvent ◄─────┘
   ├─ Rollback bid to PENDING status
   └─ Restore rejected bids to PENDING
```

---

## Events Published

### Forward Flow Events

| Event | Topic | Producer | Consumers | Purpose |
|-------|-------|----------|-----------|---------|
| `BidAcceptedEvent` | `bid.accepted` | Bid Service | Job Service | Trigger job assignment |
| `BidRejectedEvent` | `bid.rejected` | Bid Service | Notification Service | Notify rejected bidders |
| `UserBidStatisticsEvent` | `user.bid.statistics` | Bid Service | User Service | Update user stats |
| `JobAssignedEvent` | `job.assigned` | Job Service | Notification Service | Notify assignment |

### Compensation Events

| Event | Topic | Producer | Consumers | Purpose |
|-------|-------|----------|-----------|---------|
| `BidAcceptanceRollbackEvent` | `bid.acceptance.rollback` | Job Service | Bid Service | Rollback bid acceptance |

---

## Implementation Details

### 1. Bid Service (Saga Initiator + Compensation Handler)

**File:** `bid-service/src/main/java/com/devnerd/bid_service/services/BidService.java`

```java
public UpdateBidResponseDTO acceptBid(Long bidId) {
    String sagaId = UUID.randomUUID().toString(); // Track saga instance
    
    // Step 1: Accept bid
    bid.setStatus(BidStatus.ACCEPTED);
    bidRepository.save(bid);
    
    // Step 2: Publish BidAcceptedEvent
    eventProducer.publishBidAcceptedEvent(...);
    
    // Step 3: Reject other bids + publish events
    for (BidModel otherBid : otherBids) {
        eventProducer.publishBidRejectedEvent(...);
        eventProducer.publishUserBidStatisticsEvent(...);
    }
    
    // Step 4: Publish winner statistics
    eventProducer.publishUserBidStatisticsEvent(...);
}

// Compensation Transaction
public void rollbackBidAcceptance(Long bidId, String sagaId, String reason) {
    bid.setStatus(BidStatus.PENDING);
    // Restore rejected bids to PENDING
}
```

**Event Consumer (Compensation):**
```java
@KafkaListener(topics = "bid.acceptance.rollback")
public void handleBidAcceptanceRollback(BidAcceptanceRollbackEvent event) {
    bidService.rollbackBidAcceptance(event.getBidId(), ...);
}
```

### 2. Job Service (Saga Participant)

**File:** `job-service/src/main/java/com/devnerd/job_service/services/JobService.java`

```java
public void updateJobOnBidAccept(BidAcceptedEvent event) {
    try {
        // Validate job status
        if (job.getStatus() != JobStatus.OPEN) {
            throw new RuntimeException("Job not in OPEN status");
        }
        
        // Update job
        job.setStatus(JobStatus.IN_PROGRESS);
        job.setAssignedToId(event.getAssignedToId());
        jobRepository.save(job);
        
        // Continue saga
        eventProducer.publishJobAssignedEvent(...);
        
    } catch (Exception e) {
        // Trigger compensation
        eventProducer.publishBidAcceptanceRollbackEvent(...);
    }
}
```

### 3. User Service (Saga Participant)

**To be implemented:** Listen to `UserBidStatisticsEvent` and update user statistics.

### 4. Notification Service (Saga Participant)

**To be enhanced:** Add listeners for `BidRejectedEvent` to notify losers.

---

## Saga Tracking

Each saga instance is tracked using a **sagaId** (UUID) that flows through all events:

```java
String sagaId = UUID.randomUUID().toString();
// OR
String sagaId = "BID_ACCEPT_" + bidId;
```

All log messages include `[SAGA:{sagaId}]` prefix for distributed tracing.

---

## Key Features

✅ **Decentralized Control**: No orchestrator - services react to events autonomously  
✅ **Compensation Logic**: Automatic rollback when any step fails  
✅ **Idempotency**: Services handle duplicate events gracefully  
✅ **Saga Tracking**: Every event carries a sagaId for tracing  
✅ **Detailed Logging**: All saga operations logged with saga context  
✅ **Event-Driven**: Fully asynchronous, non-blocking architecture  

---

## Failure Scenarios Handled

| Failure Point | Compensation Action |
|---------------|---------------------|
| Job not found | Rollback bid to PENDING, restore rejected bids |
| Job not in OPEN status | Rollback bid to PENDING, restore rejected bids |
| Database error in Job Service | Rollback bid to PENDING, restore rejected bids |
| Job assignment validation fails | Rollback bid to PENDING, restore rejected bids |

---

## Testing the Saga

### Happy Path Test
```bash
# Accept a bid
curl -X POST http://localhost:5000/api/v1/bid/accept-bid?bidId=1

# Check logs for saga flow:
# [SAGA:xxx] Starting Bid Acceptance Saga for bidId: 1
# [SAGA:xxx] Bid 1 accepted successfully
# [SAGA:xxx] Published BidAcceptedEvent for job: 5
# [SAGA:xxx] Rejected bid: 2 for user: 3
# [SAGA:xxx] Processing BidAcceptedEvent for job: 5
# [SAGA:xxx] Job 5 assigned to freelancer 2
# [SAGA:xxx] Published JobAssignedEvent successfully
```

### Failure & Compensation Test
```bash
# Try to accept bid for an already assigned job
curl -X POST http://localhost:5000/api/v1/bid/accept-bid?bidId=3

# Check logs for compensation:
# [SAGA:xxx] Processing BidAcceptedEvent for job: 5
# [SAGA:xxx] Failed to update job: Job not in OPEN status
# [SAGA:xxx] Published BidAcceptanceRollbackEvent for bid: 3
# [SAGA:xxx] Received BidAcceptanceRollbackEvent for bid: 3
# [SAGA:xxx] Bid 3 rolled back to PENDING status
```

---

## Benefits of This Implementation

1. **Loose Coupling**: Services don't call each other directly
2. **Scalability**: Each service can scale independently
3. **Resilience**: Failure in one service doesn't break others
4. **Flexibility**: Easy to add new participants
5. **Auditability**: Complete saga trail in logs with sagaId

---

## Next Steps

To complete the implementation:

1. ✅ Bid Service - Saga initiation & compensation (DONE)
2. ✅ Job Service - Participant with rollback triggers (DONE)
3. ⏳ User Service - Add UserBidStatisticsEvent listener
4. ⏳ Notification Service - Add BidRejectedEvent listener
5. ⏳ Build & Deploy services
6. ⏳ Integration testing

---

## Comparison: Choreography vs Orchestration

**Choreography (Implemented)**:
- ✅ No single point of failure
- ✅ Services are autonomous
- ❌ Hard to track overall saga state
- ❌ Circular dependencies risk

**Orchestration** (Alternative):
- ✅ Centralized control & visibility
- ✅ Easier to track saga state
- ❌ Orchestrator is single point of failure
- ❌ Tight coupling to orchestrator

We chose **Choreography** because the bid acceptance flow is relatively simple and benefits from loose coupling between services.
