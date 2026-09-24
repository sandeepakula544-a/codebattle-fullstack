# ⚔️ CodeBattle — Real-Time 1v1 DSA Arena

A real-time competitive programming battle platform where two coders compete head-to-head on Data Structures & Algorithms problems with live synchronized timers, instant compilation via Monaco Editor, and real-time WebSocket match events.

---

## 🚀 Tech Stack

* **Frontend**: React 18, Vite, Monaco Editor, React Router v6, SockJS & STOMP WebSockets, Axios
* **Backend**: Java 21, Spring Boot 3.3.4, Spring Security, JWT (jjwt 0.12), WebSocket (STOMP), Spring Data JPA, Hibernate
* **Database**: MySQL 8.0+
* **Deployment**: Docker, Docker Compose, Vercel (Frontend), Railway / Render / VPS (Backend)

---

## 🛠️ Local Development Setup

### 1. Database Setup
Ensure MySQL is running on port `3306`:
```sql
CREATE DATABASE codebattle;
```

### 2. Backend Setup
1. Open a terminal in `backend/`:
   ```bash
   cd backend
   ```
2. Configure credentials in `src/main/resources/application.properties` (or set environment variables `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD`).
3. Build and run:
   ```bash
   ./mvnw spring-boot:run
   # Or on Windows:
   mvn spring-boot:run
   ```
   The backend API will be live at `http://localhost:8080`.

### 3. Frontend Setup
1. Open a terminal in `frontend/`:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
   The frontend will be live at `http://localhost:5173`.

---

## 🐳 1-Command Docker Deployment (Full Stack)

You can spin up MySQL + Backend + Frontend all together with Docker Compose:

```bash
docker compose up --build -d
```

* **Frontend**: Accessible at `http://localhost:3000`
* **Backend**: Accessible at `http://localhost:8080`
* **MySQL**: Running at `localhost:3306`

To shut down:
```bash
docker compose down
```

---

## ☁️ Cloud Deployment Guide

### A. Deploy Frontend to Vercel (Free & Instant)
1. Push your repository to GitHub.
2. In the [Vercel Dashboard](https://vercel.com), click **Add New Project** and import the repository.
3. Set the **Root Directory** to `frontend` (or `codebattle/frontend`).
4. In **Environment Variables**, add:
   * `VITE_API_URL`: `https://your-deployed-backend.com/api`
   * `VITE_WS_URL`: `https://your-deployed-backend.com/ws`
5. Click **Deploy**. Vercel will build the frontend with the included `vercel.json` SPA rewrite rules.

---

### B. Deploy Backend to Railway or Render
1. Create a MySQL database service on Railway, Aiven, PlanetScale, or Supabase.
2. Deploy the `backend/` folder as a Docker service (using `backend/Dockerfile`) or Spring Boot service.
3. Configure the following environment variables:
   * `PORT`: `8080` (or leave default for cloud provider)
   * `SPRING_DATASOURCE_URL`: `jdbc:mysql://<host>:<port>/<dbname>?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true`
   * `SPRING_DATASOURCE_USERNAME`: `<your_db_username>`
   * `SPRING_DATASOURCE_PASSWORD`: `<your_db_password>`
   * `CORS_ALLOWED_ORIGINS`: `https://your-frontend-app.vercel.app,http://localhost:5173`
   * `JWT_SECRET`: `<any_secure_random_string_at_least_32_characters>`

---

## 📂 Project Structure

```
codebattle-complete/
├── docker-compose.yml              # Root multi-container orchestration
├── .gitignore                      # Git ignore for root workspace
├── README.md                       # Complete documentation & deployment guide
└── codebattle/
    ├── docker-compose.yml
    ├── .gitignore
    ├── backend/
    │   ├── Dockerfile              # Multi-stage JDK 21 build
    │   ├── .env.example            # Environment variables template
    │   ├── .gitignore
    │   ├── pom.xml
    │   └── src/
    │       └── main/
    │           ├── java/com/codebattle/codebattle/  # Controllers, Services, Entities, DTOs
    │           └── resources/application.properties # Cloud-ready config
    └── frontend/
        ├── Dockerfile              # Production Nginx container
        ├── nginx.conf              # SPA fallback routing
        ├── vercel.json             # Vercel SPA routing
        ├── .env.example            # Client env vars
        ├── .gitignore
        ├── package.json
        ├── vite.config.js
        └── src/
            ├── components/Navbar.jsx # Cyber arena top navigation
            ├── pages/               # Home, Lobby, Contest, Results, Leaderboard, Profile
            └── index.css            # Dark futuristic esports theme
```
