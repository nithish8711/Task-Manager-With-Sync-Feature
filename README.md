# 🧩 Task Manager — Spring Boot + SQLite + Sync System

A **Spring Boot-based Task Management System** featuring **RESTful APIs**, **SQLite database**, and a **data synchronization module** designed for hybrid (online + offline) use cases.  
The system supports **task CRUD operations**, **sync queue management**, and **secure Basic Auth** protection.

---

## 📽️ Project Demo
🎥 [Watch Demo Video](https://drive.google.com/your-demo-video-link)

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Backend Framework | Spring Boot 3.x |
| Database | SQLite (via Hibernate JPA) |
| Authentication | Basic Auth (Spring Security) |
| API Testing | Postman |
| Language | Java 17+ |
| Build Tool | Maven |

---

## 📁 Project Structure

com.manager.task_manager
├── Controllers/
│   ├── TaskController.java
│   ├── SyncController.java
│   └── HealthController.java
│
├── DTO/
│   ├── TaskDTO.java
│   ├── SyncBatchItemDTO.java
│   ├── SyncProcessedItemDTO.java
│   ├── SyncRequestDTO.java
│   ├── SyncResponseDTO.java
│   └── SyncStatusDTO.java
│
├── Model/
│   ├── Task.java
│   ├── SyncQueue.java
│   └── SyncOperation.java
│
├── Repository/
│   ├── TaskRepository.java
│   └── SyncQueueRepository.java
│
├── Services/
│   ├── TaskService.java
│   └── SyncService.java
│
├── Security/
│   └── SecurityConfig.java
│
└── TaskManagerApplication.java


---

## 🚀 How to Run

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/task-manager.git
cd task-manager
2️⃣ Build the Project
bash
Copy code
mvn clean install
3️⃣ Run the Application
bash
Copy code
mvn spring-boot:run
Server starts at:

arduino
Copy code
http://localhost:3000
4️⃣ Login Credentials (Basic Auth)
makefile
Copy code
Username: admin
Password: admin123
🧠 System Overview
This application allows clients (like mobile or web apps) to:

Create, update, and delete tasks

Sync offline-created tasks using batch sync API

Monitor synchronization status

Secure endpoints using Basic Authentication

🔁 Data Flow
Here’s a visual representation of how data moves through the system:
![Uploading image.png…]()

---

🧩 API Endpoints & Postman Testing
🧱 1. Health Check
GET /api/health
🔓 Public endpoint

Response:

json
Copy code
{
  "status": "ok",
  "timestamp": "2025-10-30T12:45:00"
}

📝 2. Create a Task
POST /api/tasks
🔐 Requires Basic Auth

Request Body:

json
Copy code
{
  "title": "Finish project report",
  "description": "Submit by Friday"
}
Response:

json
Copy code
{
  "id": "2b4c3c0d-7a21-4de1-b12d-8c92f4b298df",
  "title": "Finish project report",
  "description": "Submit by Friday",
  "completed": false,
  "deleted": false,
  "syncStatus": "PENDING",
  "updatedAt": "2025-10-30T12:46:00"
}

📋 3. Get All Tasks
GET /api/tasks
🔐 Requires Basic Auth

Response:

json
Copy code
[
  {
    "id": "2b4c3c0d-7a21-4de1-b12d-8c92f4b298df",
    "title": "Finish project report",
    "description": "Submit by Friday",
    "completed": false,
    "deleted": false
  }
]

🔍 4. Get Task by ID
GET /api/tasks/{id}
🔐 Requires Basic Auth

Response:

json
Copy code
{
  "id": "2b4c3c0d-7a21-4de1-b12d-8c92f4b298df",
  "title": "Finish project report",
  "description": "Submit by Friday",
  "completed": false
}

✏️ 5. Update Task
PUT /api/tasks/{id}
🔐 Requires Basic Auth

Request:

json
Copy code
{
  "title": "Finish report (updated)",
  "description": "Extended deadline",
  "completed": true
}
Response:

json
Copy code
{
  "id": "2b4c3c0d-7a21-4de1-b12d-8c92f4b298df",
  "title": "Finish report (updated)",
  "description": "Extended deadline",
  "completed": true
}

🗑️ 6. Delete Task
DELETE /api/tasks/{id}
🔐 Requires Basic Auth

Response:

css
Copy code
204 No Content
🔄 7. Trigger Sync (Manual)
POST /api/sync
🔐 Requires Basic Auth

Response:

json
Copy code
{
  "processed": 5,
  "failed": 0,
  "timestamp": "2025-10-30T12:48:30"
}

📦 8. Batch Sync
POST /api/sync/batch
🔐 Requires Basic Auth

Request:

json
Copy code
{
  "items": [
    {
      "taskId": "1234",
      "data": {
        "title": "Offline task",
        "description": "Created offline",
        "completed": false
      }
    }
  ]
}
Response:

json
Copy code
{
  "processedItems": [
    {
      "clientId": "1234",
      "serverId": "bfa02b90-67ad-4c9a-93e7-9b2e4d1c7f5a",
      "status": "success",
      "resolvedData": {
        "id": "bfa02b90-67ad-4c9a-93e7-9b2e4d1c7f5a",
        "title": "Offline task",
        "description": "Created offline",
        "completed": false,
        "created_at": "2025-10-30T12:50:00",
        "updated_at": "2025-10-30T12:50:00"
      }
    }
  ]
}

📊 9. Sync Status
GET /api/sync/status
🔐 Requires Basic Auth

Response:

json
Copy code
{
  "pendingSyncCount": 2,
  "lastSyncTimestamp": "2025-10-30T12:51:00",
  "online": true,
  "syncQueueSize": 2
}

🧱 Architecture Summary
Layer	Purpose
Model Layer	Defines entities (Task, SyncQueue, SyncOperation).
DTO Layer	Transfers structured data between client & server.
Repository Layer	Handles persistence (via JPA Repositories).
Service Layer	Implements business logic (create, sync, update).
Controller Layer	Defines REST endpoints for CRUD & Sync APIs.
Security Layer	Uses Spring Security for Basic Auth protection.

🔐 Security
All APIs under /api/** are protected using HTTP Basic Auth.
Only /api/health is public.

⚖️ Trade-offs & Challenges
Challenge	Decision / Trade-off
SQLite for simplicity	Ideal for lightweight systems, not for large-scale concurrency
Basic Auth	Simple to implement but not secure for production (replace with JWT/OAuth)
SyncQueue handling	Introduced for offline task tracking
Batch sync complexity	Balanced via SyncProcessedItemDTO response
Hibernate SQLite dialect	Custom dialect (org.hibernate.community.dialect.SQLiteDialect) for compatibility

📚 Future Enhancements
JWT-based authentication

Async sync processing

WebSocket live task updates

Cloud database migration (PostgreSQL / MySQL)

Unit & integration test coverage

👨‍💻 Author
Nithish T
🎓 Final Year Biotechnology Student, Bannari Amman Institute of Technology
💡 Passionate about IoT, Java Backend & AI-based Automation Systems
