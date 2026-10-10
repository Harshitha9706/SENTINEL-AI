# SENTINEL AI — AI-Enabled Public Safety & Incident Management Platform

SENTINEL AI is an enterprise-grade public-safety and incident-management platform built exclusively with **Java** and **Spring Boot**, featuring **Thymeleaf** server-rendered interfaces, **Spring Data JPA** persistence to **MySQL**, and a decoupled **Explainable AI Decision Service**.

---

## 1. System Architecture

SENTINEL AI operates as two decoupled, independently runnable microservices:

```
SENTINEL Platform
├── Service A: Incident Management Service (Port 8080)
│   ├── Ingestion & Incident Lifecycle Management
│   ├── Case Investigation Management
│   ├── Public Safety Alert Center & Acknowledgement
│   ├── Operational Command Dashboard & KPIs
│   ├── Thymeleaf SSR Web Interface
│   ├── Spring Data JPA / MySQL Persistence
│   └── Resilient REST Client to AI Service
│
└── Service B: AI Decision Service (Port 8081)
    ├── Deterministic Rules-Based Scoring Engine
    ├── Explainability & Factor Contribution Breakdown
    ├── Historical Zone Recurrence Analyzer
    └── Tactical Response Recommendation Engine
```

### Resilient Fallback Guarantee
If the AI Decision Service (:8081) is stopped or temporarily unreachable:
- Incident intake is **never interrupted or blocked**.
- Incidents are safely persisted to MySQL with `ai_assessment_status = 'UNAVAILABLE'`.
- A 1-click **"Retry AI Assessment"** action allows operators to re-evaluate the incident as soon as the AI service is restored.

---

## 2. Explainable AI Decision Engine

### Mathematical Formula
$$P = 0.40 \cdot S + 0.30 \cdot T + 0.20 \cdot R + 0.10 \cdot U$$

Where:
- **$S$ (Reported Severity Score, $0 - 100$):**
  - `CRITICAL` = 100, `HIGH` = 75, `MEDIUM` = 50, `LOW` = 25
- **$T$ (Immediate Threat Indicator, $0 - 100$):**
  - `Active Threat / Weapon Brandished` = 100, `None` = 0
- **$R$ (Historical Recurrence Score, $0 - 100$):**
  - Frequency in reported zone over 90 days: $6+$ incidents = 100, $3-5$ incidents = 70, $1-2$ incidents = 40, $0$ incidents = 10
- **$U$ (Time Sensitivity / Latency Decay, $0 - 100$):**
  - Elapsed minutes since report: $\le 60\text{ min} = 100$, $\le 180\text{ min} = 80$, $\le 360\text{ min} = 60$, older = 40

### Priority Classification Thresholds
- **HIGH:** $75.0 \le P \le 100.0$
- **MEDIUM:** $45.0 \le P < 75.0$
- **LOW:** $0.0 \le P < 45.0$

### Safety Guardrail: Immediate Threat Override
> If `immediateThreat == true`, the system forces **Emergency Human Dispatch Protocol** independently of the aggregate score, and automatically triggers a `CRITICAL` alert.

---

## 3. Database Schema (Six Core Tables)

1. **`incidents`**: Central incident registry with category, description, location, severity, status, timestamps, priority score, and explainability notes.
2. **`historical_incidents`**: Baseline synthetic repository used to compute zone recurrence factor ($R$).
3. **`cases`**: Formal investigation cases escalated from incidents with lead investigator and deadlines.
4. **`investigation_activities`**: Detailed audit logs of investigative activities per case.
5. **`alerts`**: High-priority and critical alerts with acknowledgement tracking.
6. **`resources`**: Public safety units (Patrol vehicles, tactical units, ambulances, CID investigators).

---

## 4. REST API Reference

### Service A (:8080)
- `POST /api/incidents` — Register incident & trigger AI evaluation
- `GET /api/incidents` — Search and filter incidents (`status`, `priority`, `query`)
- `GET /api/incidents/{id}` — Incident details with AI score
- `PATCH /api/incidents/{id}/status` — Lifecycle status update (`REPORTED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
- `POST /api/incidents/{id}/retry-ai` — Re-trigger AI analysis
- `POST /api/cases` — Escalate incident to investigation case
- `GET /api/cases` — Retrieve cases
- `GET /api/dashboard/summary` — Real-time operational KPIs
- `GET /api/alerts` — Alert feed
- `PATCH /api/alerts/{id}/acknowledge` — Acknowledge alert

### Service B (:8081)
- `POST /api/ai/analyze` — Run explainable priority assessment
- `GET /api/ai/patterns` — Historical zone frequency analytics
- `POST /api/ai/recommendations` — Tactical response recommendations

---

## 5. Thymeleaf Web User Interface

| Route | View | Description |
| :--- | :--- | :--- |
| `/` or `/dashboard` | **Operations Command Center** | Real-time KPI summary cards, unacknowledged alerts feed, recent incident stream, synthetic category frequency. |
| `/incidents` | **Incident Registry** | Comprehensive search by keyword, status dropdown, priority filter, and direct navigation to details. |
| `/incidents/new` | **Incident Intake Form** | Form validation for category, description, location, severity radio buttons, and immediate life-safety threat flag. |
| `/incidents/{id}` | **Incident Detail & AI Panel** | Original report overview, Explainable AI breakdown (score badge, contributing factors, suggested tactical action, retry button), status transition, and case escalation. |
| `/cases` | **Investigation Docket** | Case registry with lead investigator, associated incident link, and status tracking. |
| `/alerts` | **Alerts Feed** | Real-time severity badges, incident linkage, and one-click officer acknowledgement workflow. |

---

## 6. How to Run

### Prerequisites
- Java 17+
- Maven 3.9+ (Bundled in `tools/apache-maven-3.9.9`)
- MySQL 8.x running on `localhost:3306` with database `sentinel_db`  
  *(Note: A local fallback profile `application-local.properties` using H2 in MySQL-mode is also included for zero-setup execution if MySQL is not currently running).*

### Step 1: Start AI Decision Service (Port 8081)
```bash
cd ai-decision-service
mvn spring-boot:run
```
Or double-click `run-ai-service.bat`.

### Step 2: Start Incident Management Service (Port 8080)
```bash
cd incident-service
mvn spring-boot:run
```
Or double-click `run-incident-service.bat`.

### Step 3: Open in Browser
Visit: **`http://localhost:8080`**
