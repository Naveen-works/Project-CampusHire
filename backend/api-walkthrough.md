# CampusHire Backend API Walkthrough

This document serves as the comprehensive REST interface contract detailing mapping endpoints for the Admin, Faculty, and Student modules.

---
## 1. ADMIN MODULE APIs
*(Placement Officers managing overarching systems, generating drives, and locking outcomes)*

### 1.1 Placement Drives Management
- `POST /api/admin/drives`
  - **Purpose:** Crates a new corporate placement drive.
  - **Request Body:** `PlacementDriveDto` (companyId, title, role, ctcLpa).
  - **Expected Output:** `200 OK` → Drive configuration wrapper.
- `POST /api/admin/drives/{driveId}/eligibility`
  - **Purpose:** Establishes the `EligibilityCriteria` (CGPA, X/XII marks, Backlogs, allowed Departments) shielding the drive.
  - **Request Body:** `EligibilityCriteriaDto`.
- `PUT /api/admin/drives/{driveId}/status?status={STATUS}`
  - **Purpose:** Toggles drives across `UPCOMING`, `ONGOING`, `COMPLETED` phases.
  - **Validation:** Automatically rejects applications to non-ongoing drives.

### 1.2 Placement Outcomes & Offers
- `POST /api/admin/drives/{driveId}/placements/offers`
  - **Purpose:** The final, most sensitive transaction. Marks a student `SELECTED` linking the success directly to their academic record.
  - **Request Parameters:** `studentId`, `ctcLpa`, `role`.
  - **System Impact:** Automatically triggers the `StudentProfile.isLocked = true` sequence protecting the profile from post-hiring edits.
  - **Validation:** Enforces that the student's application stage was fully progressed to `HR` by Faculty beforehand.

### 1.3 System Tracking & Exports
- `GET /api/admin/dashboard/stats`
  - **Purpose:** Renders root-level dashboard metrics (Total Students, Verified Count, Placed Count, Active Drives).
- `GET /api/admin/export/students?departmentId={ID}`
  - **Purpose:** Compiles a raw snapshot of the `StudentProfiles` table returning a `text/csv` stream encoded via `HttpServletResponse`.
- `GET /api/admin/audit`
  - **Purpose:** Fetches the immutable log of all actions (`CREATE_DRIVE`, `MARK_SELECTED`) recorded universally across the system via AOP interceptors.

---
## 2. FACULTY MODULE APIs
*(Department-level coordinators tracking applications and verifying submitted data)*

*(Note: Faculty controllers require `?facultyEmail=hod.cse@college.edu` mapping to apply contextual department-level filtering during current simulated test modes)*

### 2.1 Profile Verification Engine
- `GET /api/faculty/students`
  - **Purpose:** Renders all students mapped solely to the faculty’s `Department`.
- `PUT /api/faculty/students/{studentId}/verify?status={STATUS}`
  - **Purpose:** Approves/Rejects a student’s data entry submission.
  - **Request Parameters:** `status` (VERIFIED/REJECTED), `remarks` (String explaining rejections).
  - **System Impact:** If `VERIFIED`, unlocks the student’s capability to apply for jobs and engages the `Eligibility Engine`.

### 2.2 Application Funnel Management
- `GET /api/faculty/drives/{driveId}/participants`
  - **Purpose:** Tracks students who have successfully applied to a specific active drive.
- `PUT /api/faculty/applications/{applicationId}/stage?stage={STAGE}`
  - **Purpose:** Progresses an active application along the corporate recruitment pipeline (`ASSESSMENT` > `TECHNICAL` > `HR`).
  - **Validation:** Strictly blocks faculty from sending the `'SELECTED'` parameter, isolating final authority to Admins.

### 2.3 Department Operations
- `GET /api/faculty/analytics`
  - **Expected Output:** Aggregation metrics uniquely scoping `topRecruiters`, `averagePackageLpa`, and `placementPercentage` purely against the faculty's home department.
- `POST /api/faculty/announcements`
  - **Purpose:** Publishes a generalized broadcast notification targeted either `GLOBAL`ly or to the faculty's `DEPARTMENT`.

---
## 3. STUDENT MODULE APIs
*(Primary data-entry and active participation pathways)*

### 3.1 Profile Builder (The Resume Pipeline)
- `GET /api/student/profile`
  - **Purpose:** Retreives an expansive, nested JSON tree carrying the student's `PersonalDetails`, `ContactDetails`, `AcademicRecord`, and `SchoolingDetails`.
- `PUT /api/student/profile/academic`
  - **Purpose:** Updates foundational hiring metrics (`cgpa`, active `standingArrears`).
  - **Request Body:** `AcademicRecordDto`.
  - **Validation:** Operation blocked entirely (`ProfileLockedException`) if `VerificationStatus == VERIFIED` preventing students from altering grades post-approval.

### 3.2 Drive Engagement
- `GET /api/student/drives`
  - **Purpose:** Lists all active `ONGOING` placement grids.
  - **System Impact:** The server runtime immediately cross-references the student's nested `AcademicRecord` against every active drive's distinct `EligibilityCriteria`, formatting an isolated `isEligible` flag.
- `POST /api/student/applications/{driveId}/apply`
  - **Purpose:** Formally registers intent for a specific placement process.
  - **Validation:** The drive must be `ONGOING`, the profile must be `VERIFIED`, and `isEligible` must evaluate `true` dynamically on the server instance.

### 3.3 Status Tracking
- `GET /api/student/applications`
  - **Purpose:** Generates a personalized array tracking all historical recruitment movements mapped alongside current unfulfilled Application Stages (`APPLIED` / `TECHNICAL`).
- `GET /api/student/info/announcements`
  - **Purpose:** Broadcast stream receiver for consuming Placement notices, interview schedules, and generalized events mapping back to the Admin/Faculty announcement dispatchers.
