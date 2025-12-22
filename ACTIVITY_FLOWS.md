# QuickHammer Activity Flows

This document outlines the main activity flows within the QuickHammer microservices application.

## System Overview

The application is composed of several microservices communicating via REST APIs (synchronous) and Kafka events (asynchronous).

**Core Services:**
- **Auth Service**: Authentication and Authorization.
- **User Service**: User profile management.
- **Job Service**: Job posting and management.
- **Bid Service**: Bidding on jobs.
- **Notification Service**: Email notifications.
- **Payment Service**: Payment processing (Stripe integration).
- **API Gateway**: Entry point for all client requests.

---

## 1. User Authentication Flow

**Goal**: Register a new user and log them in.

1.  **Registration**:
    -   **Client** sends `POST /api/v1/auth/register` to **Auth Service**.
    -   **Auth Service** validates password strength.
    -   **Auth Service** calls **User Service** (via Feign Client) to create the user profile.
    -   **User Service** saves user details to `user-service-db`.
    -   **Auth Service** saves credentials (hashed password) to `auth-service-db`.
    -   **Auth Service** returns success.

2.  **Login**:
    -   **Client** sends `POST /api/v1/auth/login` to **Auth Service**.
    -   **Auth Service** verifies credentials.
    -   **Auth Service** generates a JWT token and Session ID.
    -   **Auth Service** publishes `UserLoginEvent` to `user.login` Kafka topic.
    -   **Auth Service** returns JWT token to Client.

---

## 2. Job Creation Flow

**Goal**: A Client (Employer) posts a new job.

1.  **Create Job**:
    -   **Client** sends `POST /api/v1/job/create-job` to **Job Service**.
    -   **Job Service** saves job details to `job-service-db`.
    -   **Job Service** returns job details.

---

## 3. Bidding Flow

**Goal**: A Freelancer places a bid on a job.

1.  **Place Bid**:
    -   **Client** sends `POST /api/v1/bid/create-bid` to **Bid Service**.
    -   **Bid Service** saves bid details to `bid-service-db`.

2.  **View Bids**:
    -   **Client** (Employer) sends `GET /api/v1/bid/bids-by-job?jobId={id}` to **Bid Service**.
    -   **Bid Service** returns list of bids for the job.

---

## 4. Bid Acceptance & Job Assignment Flow (Event-Driven)

**Goal**: Employer accepts a bid, triggering job assignment and notification.

1.  **Accept Bid**:
    -   **Client** (Employer) sends `POST /api/v1/bid/accept-bid?bidId={id}` to **Bid Service**.
    -   **Bid Service** updates bid status to `ACCEPTED`.
    -   **Bid Service** publishes `BidAcceptedEvent` to `bid.accepted` Kafka topic.

2.  **Job Assignment**:
    -   **Job Service** consumes `BidAcceptedEvent`.
    -   **Job Service** updates job status to `ASSIGNED` and links the freelancer.
    -   **Job Service** publishes `JobAssignedEvent` to `job.assigned` Kafka topic.

3.  **Notification**:
    -   **Notification Service** consumes `JobAssignedEvent`.
    -   **Notification Service** sends an email notification (likely to the freelancer and/or employer).

---

## 5. Payment Flow (Onboarding)

**Goal**: Onboard a freelancer for payments.

1.  **Onboard**:
    -   **Client** sends `POST /api/v1/payment/onboard` to **Payment Service**.
    -   **Payment Service** interacts with Stripe to create an account link.
    -   **Payment Service** returns the onboarding URL.

---

## Event Schema Summary

| Event | Producer | Consumer | Topic | Payload Key Fields |
| :--- | :--- | :--- | :--- | :--- |
| `UserLoginEvent` | Auth Service | (None found*) | `user.login` | `userId`, `sessionId` |
| `BidAcceptedEvent` | Bid Service | Job Service | `bid.accepted` | `jobId`, `bidId`, `assignedToId`, `bidBudget` |
| `JobAssignedEvent` | Job Service | Notification Service | `job.assigned` | `jobId`, `clientId`, `assignedToId`, `agreedBidBudget` |