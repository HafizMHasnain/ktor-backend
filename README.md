# TikTok Clone Backend Server

A backend server application built using [Ktor](https://ktor.io/) for a TikTok-style social media app. This project is designed to provide API endpoints for features such as user authentication, video uploads, and user profiles. The backend uses a **MySQL** database for persistent data storage.

---

## 🚀 Features

- **User Authentication**
    - User registration (sign up)
    - User login with JWT or session-based authentication

- **Video Management**
    - Upload videos
    - Fetch video feed
    - Like/Unlike videos
    - Video metadata (caption, timestamp, etc.)

- **User Profile**
    - View and update profile
    - Fetch user’s uploaded videos
    - Profile pictures (optional)

- **Database**
    - MySQL is used to store users, videos, and related metadata
    - Proper schema relationships and indexing for performance

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Framework**: [Ktor](https://ktor.io/)
- **Database**: MySQL
- **ORM**: Exposed (or any you are using)
- **Authentication**: JWT / Sessions (depending on your implementation)
- **Video Storage**: Local storage or S3-compatible object storage

---

## 📂 Project Structure

```bash
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   ├── routes/          # All route handlers (e.g., authRoutes, videoRoutes)
│   │   │   ├── models/          # Data classes and table definitions
│   │   │   ├── controllers/     # Business logic for endpoints
│   │   │   ├── database/        # DB setup and configuration
│   │   │   └── Application.kt   # Entry point
│   └── resources/
│       └── application.conf     # Ktor + DB configurations
