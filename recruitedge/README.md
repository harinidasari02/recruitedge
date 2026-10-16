# RecruitEdge – Recruitment & Applicant Tracking Portal

A full-stack applicant tracking system for posting jobs, managing candidates through hiring stages, and scheduling interviews.

## Features
- Post, edit, close, and delete job openings
- Add candidates to open jobs and move them through stages (Applied, Screening, Interview, Offer, Hired, Rejected)
- Schedule and cancel interviews; scheduling moves the candidate to the Interview stage
- Business rules enforced in the service layer: no applications to closed jobs, no duplicate applications, no interviews in the past, no interviewer double-booking, no changes after Hired or Rejected
- Global exception handling that returns consistent JSON errors (400, 404, 409, 500)

## Tech Stack
**Backend:** Java 17, Spring Boot 3, Spring Data JPA (Hibernate), MySQL, Maven, JUnit 5, Mockito
**Frontend:** React 18 (Vite)

## Project Structure
```
recruitedge/
├── backend/    Spring Boot API (controller, service, repository, model, exception layers)
└── frontend/   React app
```

## Getting Started

### Prerequisites
Java 17+, Maven, Node.js 18+, MySQL 8+

### Backend
1. Make sure MySQL is running.
2. Copy `backend/src/main/resources/application.properties.example` to `application.properties` and set your MySQL username and password. The database is created automatically.
3. Run:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   API: http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm run dev
```
App: http://localhost:5173 (API calls are proxied to port 8080)

### Tests
```bash
cd backend
mvn test
```

## API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/jobs | List jobs |
| GET | /api/jobs/{id} | Get one job |
| POST | /api/jobs | Create a job |
| PUT | /api/jobs/{id} | Update a job |
| PATCH | /api/jobs/{id}/close | Close a job |
| DELETE | /api/jobs/{id} | Delete a job |
| GET | /api/candidates?jobId= | List candidates, optionally by job |
| POST | /api/candidates | Add a candidate to a job |
| PATCH | /api/candidates/{id}/stage | Move a candidate to a stage |
| GET | /api/interviews | List interviews |
| POST | /api/interviews | Schedule an interview |
| DELETE | /api/interviews/{id} | Cancel an interview |

## Screenshots
Add screenshots to `docs/` and link them here.

## Future Improvements
- Authentication and role-based access
- Email notifications for interviews
- Resume uploads
