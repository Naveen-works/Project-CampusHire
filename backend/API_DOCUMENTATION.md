# CampusHire Student API Documentation

This documentation provides all necessary endpoints for Postman integration testing. Note that all endpoints (except `/api/student/auth/login` and `/api/student/auth/register`) strictly require Authentication via JWT `Authorization: Bearer <token>` Header OR a mock `?email=` query parameter attached to the request URL.

### Authentication & Registration
| Method | Endpoint | Query Params | Body (JSON) | Description |
|---|---|---|---|---|
| `POST` | `/api/student/auth/register` | `universityRegNo` | `{ "email": "...", "password": "..." }` | Register a new student profile |
| `POST` | `/api/student/auth/login` | None | `{ "email": "...", "password": "..." }` | Login to retrieve JWT Bearer token |

### Profile Management
| Method | Endpoint | Query Params | Body (JSON) | Description |
|---|---|---|---|---|
| `GET` | `/api/student/profile` | `email` | None | Gets the entire aggregated Student Profile |
| `GET` | `/api/student/profile/verification-status` | `email` | None | Gets the current Verification profile lock status (`PENDING`, `VERIFIED`, `REJECTED`) |
| `PUT` | `/api/student/profile/personal` | `email` | `{ "firstName": "...", "lastName": "...", "dob": "YYYY-MM-DD", "gender": "...", "aadharNumber": "..." }` | Updates Personal Details |
| `PUT` | `/api/student/profile/contact` | `email` | `{ "primaryPhone": "...", "secondaryPhone": "...", "currentAddress": "...", "permanentAddress": "..." }` | Updates Contact Details |
| `PUT` | `/api/student/profile/academic` | `email` | `{ "degree": "...", "department": "...", "currentSemester": 8, "cgpa": 8.5, "historyOfArrears": 0, "standingArrears": 0 }` | Updates Higher Academic Record |
| `PUT` | `/api/student/profile/schooling` | `email` | `{ "xSchoolName": "...", "xPassingYear": 2018, "xPercentage": 90.5, "xiiSchoolName": "...", "xiiPassingYear": 2020, "xiiPercentage": 92.0 }` | Updates X & XII Schooling Record |

### Placement Drives
| Method | Endpoint | Query Params | Body (JSON) | Description |
|---|---|---|---|---|
| `GET` | `/api/student/drives` | `email` | None | Get a list of all visible placement drives (includes dynamic `isEligible` flag calculation) |
| `GET` | `/api/student/drives/{driveId}` | `email` | None | Get specific drive details by ID |
| `POST` | `/api/student/applications/{driveId}/apply` | `email`, `driveId` | None | Create a new Application representing student applying to `driveId` |
| `GET` | `/api/student/applications` | `email` | None | List ALL placement drives the currently logged-in student has applied for |

### Dashboard & Analytics
| Method | Endpoint | Query Params | Body (JSON) | Description |
|---|---|---|---|---|
| `GET` | `/api/student/dashboard/stats` | `email` | None | Retrieves high-level aggregations (total companies, total ongoing drives, highest CTC) |

### Information Modules (Read-Only)
| Method | Endpoint | Query Params | Body (JSON) | Description |
|---|---|---|---|---|
| `GET` | `/api/student/info/companies` | `email` | None | Retrieves list of actively hiring companies |
| `GET` | `/api/student/info/events` | `email` | None | Retrieves upcoming events calendar |
| `GET` | `/api/student/info/announcements` | `email` | None | Retrieves global broadcast announcements |

---
**Tip for Postman:** To test easily without setting up the Auth Header per-request temporarily, simply append `?email=student@example.com` to every `GET/PUT/POST` URL (excluding `login`/`register`). The `StudentAccessAspect` will accept the request!
