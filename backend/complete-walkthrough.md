# CampusHire Backend System: Complete System Walkthrough

## 1. System Architecture Overview

CampusHire is a vertically integrated, monolithic enterprise placement management system built specifically for university governance. The backend framework is **Spring Boot 3.x (Java 21)** mapping tightly to a **PostgreSQL** relational database using **Spring Data JPA/Hibernate**. 

The system implements a rigid **3-Tier Architecture**:
1.  **Presentation (Controllers):** Strict RESTful APIs returning standardized `ApiResponse<T>` JSON payloads.
2.  **Business Logic (Services):** Transactional (`@Transactional`) execution environments enforcing role-based privileges, data validation, and state-machine transitions (e.g., Application Stages).
3.  **Data Access (Repositories):** Entity interfaces leveraging JPQL and derived query methods for optimized filtering.

Additionally, the system utilizes **Aspect-Oriented Programming (AOP)** via custom `@AuditAction` metadata wrapper annotations attached to controller endpoints. An `AdminAuditAspect` intercepts these requests to passively log user actions (Action Type, Target Entity, IP, Timestamp) directly into the `audit_logs` table without polluting business logic.

---

## 2. Core Operational Modules and Privilege Matrices

The system operates via a strict tripartite Role-Based Access Control (RBAC) model. Data moves "Up" (created by Students) but Authority flows "Down" (Admin governs all).

### 2.1 The Student Module (Data Entry & Participation)
The Student Module handles the initiation of placement cycles. 
-   **Profile Assembly:** Students dynamically enter their details (Academics, Personal, Contact, Schooling). The `StudentProfileService` guards all `PUT` mutations: if an Admin sets `isLocked = true` or Faculty approves `verificationStatus = VERIFIED`, the profile becomes read-only to the student (`ProfileLockedException`).
-   **Drive Discovery:** Evaluates active (`UPCOMING`, `ONGOING`) Drives. The `EligibilityCriteria` Engine runs runtime checks against the student's `AcademicRecord` (CGPA, active arrears, arrears history) and `SchoolingDetails` (X/XII percentages).
-   **Application Sandbox:** Eligible, verified students invoke the `DriveApplicationController` to formally apply, inserting idempotency-protected rows into `drive_applications`.

### 2.2 The Faculty Module (Validation & Workflow Tracking)
The Faculty Module represents the "Departmental Control Layer". Faculty members are assigned to specific `Departments` via their `User` configurations.
-   **Verification Engine:** Using `FacultyStudentController`, Faculty review student-submitted profiles. An approval transitions the `StudentProfile` to `VERIFIED` and logs a permanent `ProfileVerification` record with audit remarks. Rejection pushes the profile back for fixes.
-   **Stage State Machine:** As drives progress, faculty update the `stage` of `drive_applications` strictly along the sequence: `APPLIED -> ASSESSMENT -> TECHNICAL -> HR`. Only Admin can finalize to `SELECTED`.
-   **Data Scoping:** Faculty queries strictly inject `?departmentId=X` to ensure they cannot view or mutate students, drives, or analytics belonging to other departments.

### 2.3 The Admin Module (Command & Governance)
The Admin (Placement Officer) has system-wide oversight, completely decoupled from manual student data entry.
-   **Drive Lifecycle Management:** Admins define `Company` profiles and instantiate `PlacementDrive` definitions mapped with strict `EligibilityCriteria` barriers.
-   **Shortlisting Automations:** Admins execute batch queries against `drive_applications` combined with `StudentProfile` eligibility to freeze confirmed candidate lists.
-   **Final Placements (The Omega Operation):** Once a student clears the HR stage, the Admin records the `Offer` via `AdminFinalPlacementController`. This updates the total `numberOfOffers`, calculates the `highestPackageLpa` on the student profile, sets `isPlaced = true`, and permanently triggers the `isLocked = true` safety switch across the entire profile to prevent post-placement tampering.
-   **Analytics & Governance:** Aggregates macro trends (Average packages, top recruiters, verified vs waitlisted ratios) and monitors AOP-intercepted system behavior through the `AdminAuditController`.

---

## 3. Database Schema Overview (3NF Normalized)

The application extensively normalizes data into 20+ precise relational groupings mapping to JPA Entities:

1.  **Identity & Security:** `users`, `departments`. (Linked via `department_id` Foreign Key).
2.  **Student Identity Sub-Entities (One-to-One splits):** `student_profiles` splits deeply into `student_personal_details`, `student_contact_details`, `student_identity_docs`, `academic_records`, and `schooling_details`. This allows lazy fetching (`FetchType.LAZY`) minimizing memory usage when returning thousands of students.
3.  **Recruitment Ecosystem:** `companies` acts as the root. `placement_drives` (Many-to-One) tie to a company. Drives map to `drive_eligibility` (One-to-One) defining parameters, and `drive_allowed_departments` (Many-to-Many).
4.  **Operational Linkages:** `drive_applications` is the core join table structurally linking a `StudentProfile` to a `PlacementDrive`.
5.  **Governance Audits:** `profile_verifications` (Faculty → Student tracking) and `audit_logs` (System → Action tracking).

---

## 4. Notable Engineering Highlights

1.  **Strict State Transition Logic:** The application lifecycle (`ApplicationStage` Enums) enforces a forward-only validation flow. If a faculty member attempts to move a student directly from `APPLIED` to `HR`, the service layer rejects the transaction.
2.  **AOP Audit Triggers:** Implementing `@AuditAction` enables "fire-and-forget" compliance logging for highly destructive or sensitive endpoints (e.g., Marking Selected, Deleting Companies, Creating Drives) yielding a permanent forensic trail.
3.  **Unified Response Wrappers:** Every Controller method returns a standard `ApiResponse<T>`:
    ```json
    {
      "success": true,
      "message": "Operation completed successfully",
      "data": { ... payload ... },
      "timestamp": "ISO-8601"
    }
    ```
4.  **Simulated Authorization Environment:** Currently, to bypass unbuilt JWT filtering components, identity vectors (roles and contextual `email` strings) are injected into the API queries as `@RequestParam(defaultValue = "admin@college.edu")` simulating a secure OAuth-like `Principal`.
