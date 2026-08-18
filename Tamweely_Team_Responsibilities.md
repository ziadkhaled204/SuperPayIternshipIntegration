# Tamweely Integration — Project Structure & Work Distribution

## 1. Project Overview

The project consists of two main applications:

1. **WSO2 Integration Project**
   - Acts as the integration layer between SuperPay and Tamweely.
   - Handles APIs, sequences, transformations, endpoints, HMAC signing, routing, and fault handling.

2. **Mock Tamweely Spring Boot Application**
   - Simulates the Tamweely backend.
   - Provides the APIs that WSO2 consumes.
   - Returns different responses and scenarios to allow complete integration testing.
   - Does NOT represent the real Tamweely backend; it only mocks the APIs required by this integration.

---

# 2. Overall Architecture

```text
                    SuperPay
                       │
                       │ XML Request
                       ▼
              ┌───────────────────┐
              │       WSO2        │
              │ Micro Integrator   │
              │                   │
              │ Integration APIs  │
              │ Sequences         │
              │ Endpoints         │
              │ Transformations   │
              │ HMAC              │
              │ Fault Handling    │
              └─────────┬─────────┘
                        │
                        │ HTTP / JSON
                        ▼
              ┌───────────────────┐
              │  Mock Tamweely    │
              │   Spring Boot     │
              │                   │
              │ Inquiry API       │
              │ Payment API       │
              │ Check Status API  │
              │                   │
              │ Dynamic Scenarios │
              └───────────────────┘

```

# 3. Repo & Project Structure


```text
TamweelyIntegration/
│
├── wso2/
│   │
│   ├── api/
│   │   └── TamweelyAPI.xml                         ← P1
│   │
│   ├── endpoints/
│   │   ├── TamweelyInquiryEndpoint.xml              ← P2
│   │   ├── TamweelyPaymentEndpoint.xml              ← P3
│   │   └── TamweelyCheckStatusEndpoint.xml         ← P4
│   │
│   ├── sequences/
│   │   │
│   │   ├── integration/
│   │   │   │
│   │   │   ├── TamweelyMainSequence.xml            ← P1
│   │   │   │
│   │   │   ├── TamweelyInquiryRequestSequence.xml  ← P2
│   │   │   ├── TamweelyInquiryResponseSequence.xml ← P2
│   │   │   │
│   │   │   ├── TamweelyPaymentRequestSequence.xml  ← P3
│   │   │   ├── TamweelyPaymentResponseSequence.xml ← P3
│   │   │   │
│   │   │   ├── TamweelyCheckStatusRequestSequence.xml  ← P4
│   │   │   └── TamweelyCheckStatusResponseSequence.xml ← P4
│   │   │
│   │   └── fault/
│   │       └── TamweelyFaultSequence.xml            ← P1
│   │
│   └── README.md
│
│
└── mock-tamweely/
    │
    ├── pom.xml
    │
    └── src/
        └── main/
            │
            ├── java/com/superpay/tamweely/
            │   │
            │   ├── MockTamweelyApplication.java    ← P5
            │   │
            │   ├── controller/
            │   │   └── TamweelyMockController.java ← P5
            │   │
            │   ├── service/
            │   │   └── TamweelyMockService.java    ← P5
            │   │
            │   ├── model/
            │   │   ├── Bill.java                   ← P5
            │   │   └── Transaction.java            ← P5
            │   │
            │   └── data/
            │       └── MockDataStore.java           ← P5
            │
            └── resources/
                └── application.properties           ← P5


```