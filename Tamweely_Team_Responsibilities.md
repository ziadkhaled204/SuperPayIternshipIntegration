# Tamweely Integration — Project Structure & Flow

## Project Structure & everyone responsbility

```text
TamweelyIntegration/
│
├── api/
│   ├── TamweelyAPI.xml                  ← P1
│   └── MockTamweelyAPI.xml                         ← P5
│
├── endpoints/
│   ├── TamweelyInquiryEndpoint.xml                 ← P2
│   ├── TamweelyPaymentEndpoint.xml                 ← P3
│   └── TamweelyCheckStatusEndpoint.xml             ← P4
│
├── sequences/
│   │
│   ├── integration/
│   │   │
│   │   ├── TamweelyMainSequence.xml                ← P1
│   │   │
│   │   ├── TamweelyInquiryRequestSequence.xml      ← P2
│   │   ├── TamweelyInquiryResponseSequence.xml     ← P2
│   │   │
│   │   ├── TamweelyPaymentRequestSequence.xml      ← P3
│   │   ├── TamweelyPaymentResponseSequence.xml     ← P3
│   │   │
│   │   ├── TamweelyCheckStatusRequestSequence.xml  ← P4
│   │   └── TamweelyCheckStatusResponseSequence.xml ← P4
│   │
│   └── mock/
│       │
│       ├── MockTamweelyInquirySequence.xml         ← P5
│       │
│       ├── MockTamweelyPaymentSequence.xml         ← P6
│       │
│       └── MockTamweelyCheckStatusSequence.xml     ← P6
│
└── fault/
    └── TamweelyFaultSequence.xml                   ← P1
```


## integration flow

```text
SuperPay
   │
   │ XML Request
   ▼
TamweelyIntegrationAPI                         P1
   │
   ▼
TamweelyMainSequence                          P1
   │
   ▼
Operation Request Sequence                    P2/P3/P4
   │
   │ XML → JSON
   │ HMAC Signature
   ▼
Operation Endpoint                            P2/P3/P4
   │
   │ HTTP POST / JSON
   ▼
Tamweely
   │
   │ JSON Response
   ▼
Operation Response Sequence                   P2/P3/P4
   │
   │ JSON → XML
   ▼
TamweelyMainSequence
   │
   ▼
SuperPay
```
## mock flow

TamweelyIntegration
        │
        │ HTTP Request
        ▼
MockTamweelyAPI                              P5
        │
        ├──────────────────┐
        │                  │
        ▼                  ▼
 Inquiry Mock          Payment Mock
 Sequence               Sequence
   P5                       P6
        │                  │
        └────────┬─────────┘
                 │



## important note for mocking 

you have two options for dynamic scenarios


option A: send scenario as query param (mock/tamweely/inquiry?scenario=success)
and then you can get it with (get-property('query.param.scenario'))

option B: create cases with specific IDs like 

(999999999999 --> sucess, 1000000000000 --> found, 18888888888888 --> fail)

