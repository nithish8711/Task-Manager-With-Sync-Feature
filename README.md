# 🧩 Task Manager — Spring Boot + SQLite + Sync System

A **Spring Boot-based Task Management System** featuring **RESTful APIs**, **SQLite database**, and a **data synchronization module** designed for hybrid (online + offline) use cases.  
The system supports **task CRUD operations**, **sync queue management**, and **secure Basic Auth** protection.

---

## 📽️ Project Demo
🎥 [Watch Demo Video](https://drive.google.com/file/d/1vTvatgpswi75IABbTA2F9NNuNPXFPFjM/view?usp=sharing)

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

## 🚀 How to Run

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/task-manager.git
cd task-manager

2️⃣ Build the Project
mvn clean install

3️⃣ Run the Application
bash
Copy code
mvn spring-boot:run
Server starts at:

http://localhost:8080
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
<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/ff782aeb-d2a4-4d81-94ab-f852f56f9180" />


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
{
  "title": "Finish project report",
  "description": "Submit by Friday"
}
Response:

json
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
{
  "title": "Finish report (updated)",
  "description": "Extended deadline",
  "completed": true
}
Response:

json
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

204 No Content
🔄 7. Trigger Sync (Manual)
POST /api/sync
🔐 Requires Basic Auth

Response:

json
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
{
  "pendingSyncCount": 2,
  "lastSyncTimestamp": "2025-10-30T12:51:00",
  "online": true,
  "syncQueueSize": 2
}

🔐 Security
All APIs under /api/** are protected using HTTP Basic Auth.
Only /api/health is public.

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
