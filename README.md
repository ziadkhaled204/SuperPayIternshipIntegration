# Tamweely Integration

SuperPay ⇄ **WSO2 Micro Integrator** ⇄ **Mock Tamweely**

- **WSO2 (ESB)** — the integration layer. Takes an XML request from SuperPay, routes it to the right operation, transforms it to the JSON the backend expects, and maps the response back to XML.
- **Mock Tamweely** — a Spring Boot app standing in for the real Tamweely backend, so the whole flow can be tested end-to-end.

Operations supported: **Inquiry** · **Payment** · **CheckStatus**

---

## 1. Architecture

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

## 2. Repo Structure

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

---

## 3. How it works

SuperPay sends an XML `ExternalServiceRequest` to the ESB (`POST /api/tamweely`). The ESB reads the `sequence_type` param and routes accordingly:

```text
ExternalServiceRequest (XML)
      │
      ▼
┌────────────────────────────┐
│  TamweelyMainSequence      │  switch on sequence_type
└──────┬──────┬──────┬───────┘
       │      │      │
   inquiry  payment  checkstatus
       │      │      │
       ▼      ▼      ▼
  RequestSequence ──► build JSON payload
  Endpoint        ──► POST to Mock (8080)
  ResponseSequence► map JSON back to XML
      │
      ▼
ExternalServiceResponse (XML)
```

| Operation | sequence_type | JSON `Msg` | Mock endpoint (8080) |
|---|---|---|---|
| Inquiry | `inquiry` | `Inquiry` | `/api/SuperPay/SuperPayController/GetSuperPayDataByIDNO` |
| Payment | `payment` | `Payment` | `/api/SuperPay/SuperPayController/SetAmount` |
| CheckStatus | `checkstatus` | `CheckStatus` | `/api/SuperPay/SuperPayController/CheckStatus` |

---

## 4. WSO2 ESB

### Entry point
- **`TamweelyAPI.xml`** — context `/api/tamweely`, `POST` only.
- Enforces a **throttle policy**: max 50 req/IP/sec → rejects with `429` `ESB:RATE_LIMIT_EXCEEDED`.

### Routing
- **`TamweelyMainSequence.xml`** — reads `GLETxnID` + amount, then switches on `sequence_type` into the matching request/response sequence pair. Unknown type → `400` `ESB:INVALID_SEQ`.

### Transformations (request sequences)
- Extracts fields from the XML with fallbacks, e.g.:
  - `IDNO` → `InjectedParams/IDNO`, else `xparam/SubscriberId`
  - `Num_Funding` → `InjectedParams/Num_Funding`, else `xparam/GovernorateCode`
  - `RequestId` → `InjectedParams/RequestId`, else `GLETxnID`
  - `Signature` → `InjectedParams/Signature`, else `p_biller_service_id`
- Builds a JSON payload: `SenderName, Msg, IDNO, Num_Funding, RequestId, RequestDate, Signature` (+ `Amount` for payment), then calls the matching endpoint.

### Transformations (response sequences)
- Reads `$.status.code` from the JSON reply.
- **`200`** → bill fields mapped into XML `ExternalServiceResponse` with `Status: SVC:0`.
- otherwise → `Status: SVC:<code>` with the error message.
- Always answered with HTTP `200`.

### Fault handling
| Case | HTTP | Status |
|---|---|---|
| Rate limit | `429` | `ESB:RATE_LIMIT_EXCEEDED` |
| Invalid sequence_type | `400` | `ESB:INVALID_SEQ` |
| Any runtime fault | `500` | `ESB:INTERNAL_ERROR` |

### Build / run
- Maven integration project → produces a `.car` (Carbon Application).
- Runtime: WSO2 Micro Integrator **4.5.0**; API exposed on `localhost:8290`.
- Docker: `docker` profile builds image `tamweely:1.0.0` (see `deployment/`).

---

## 5. Mock Tamweely

Spring Boot app on **`localhost:8080`** simulating the Tamweely backend. Real Tamweely is **not** involved.

### Endpoints
Base: `/api/SuperPay/SuperPayController`

| Method | Path | Purpose |
|---|---|---|
| POST | `/GetSuperPayDataByIDNO` | Inquiry — return the client's bill |
| POST | `/SetAmount` | Payment — apply an amount to a bill |
| POST | `/CheckStatus` | CheckStatus — is the bill fully paid? |

### Request
JSON body requiring: `SenderName`, `Msg`, `IDNO`, `RequestId`, `Signature`. `Msg` must match the operation; `Amount` (payment) must be > 0.

### Response scenarios
Returns a `Bill` (bill fields + `status.code` / `status.message`):

| Scenario | Condition | `status.code` |
|---|---|---|
| Success | valid request + client found | `200` `successful` |
| Invalid request | missing field / wrong `Msg` | `400` `Invalid request` |
| Client not found | unknown `IDNO` / `Num_Funding` | `400` `Client not found` |
| Not paid | CheckStatus with balance remaining | `201` `Not Paid` |

### Behavior
- **Inquiry** — returns the stored bill.
- **Payment** — mutates the in-memory bill (updates paid/outstanding amounts, remaining installments, due dates).
- **CheckStatus** — `200` if fully paid, else `201`.
- Every call is logged as a `Transaction` in an in-memory `MockDataStore` (resets on restart).

### Build / run
- Standard Spring Boot Maven app. Entry point: `MockTamweelyApplication`.

---

## 6. Testing with Postman

Ready-to-use collections are included in the repo — import them into Postman and run the requests.

| Collection | Targets | What it covers |
|---|---|---|
| `TamweelyMI.postman_collection.json` | ESB at `localhost:8290` | Send `ExternalServiceRequest` XMLs through the MI API (`inquiry` / `payment` / `checkstatus` + error cases) |
| `TamweelyMockAPI.postman_collection.json` | Mock at `localhost:8080` | Call the mock endpoints directly to verify scenarios (success / invalid / not found) |

To test the full end-to-end flow: start the mock (8080) → start the MI (8290) → run the `TamweelyMI` collection.

---
