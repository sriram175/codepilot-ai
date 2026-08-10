<div align="center">

# 🤖 CodePilot AI

### AI-powered Codebase Assistant using Retrieval-Augmented Generation (RAG)

Chat with any Git repository using **Google Gemini**, **Spring AI**, **Qdrant**, and **Spring Boot**.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring_AI-1.1-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Qdrant](https://img.shields.io/badge/Qdrant-Latest-red)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![License](https://img.shields.io/badge/License-MIT-green)

</div>

---

# 📖 Overview

CodePilot AI is an AI-powered code assistant that enables developers to chat with software repositories using natural language.

Instead of searching manually through hundreds of files, CodePilot AI performs semantic search over the repository, retrieves the most relevant code, and uses **Retrieval-Augmented Generation (RAG)** with **Google Gemini** to generate accurate, context-aware answers.

Unlike a generic chatbot, responses are grounded in the indexed source code, reducing hallucinations and improving answer quality.

---

# ✨ Features

- 📂 Git repository ingestion using **JGit**
- 🧠 AI-powered repository summarization
- 🔍 Semantic code search using vector embeddings
- 💬 Context-aware conversational chat
- 🔄 Query rewriting for follow-up questions
- 📚 Conversation history support
- 📌 Source attribution for every response
- ⚡ Google Gemini integration
- 🗄️ PostgreSQL persistence
- 📈 Qdrant Vector Database
- 🐳 Fully Dockerized deployment

---

# 🏗️ System Architecture

```text
                         +----------------------+
                         |      React UI        |
                         +----------+-----------+
                                    |
                                    |
                           REST API Calls
                                    |
                                    ▼
                    +-------------------------------+
                    |      Spring Boot Backend       |
                    +-------------------------------+
                     |            |             |
                     |            |             |
                     ▼            ▼             ▼
              PostgreSQL      Google       Qdrant
            Conversations      Gemini     Vector DB
                  |          LLM & Embeddings
                  |
                  ▼
            Repository Metadata
```

---

# 🧠 Retrieval-Augmented Generation (RAG) Pipeline

```text
Git Repository
      │
      ▼
Repository Ingestion
      │
      ▼
Text Chunking
      │
      ▼
Embedding Generation
      │
      ▼
Store Embeddings in Qdrant
      │
      ▼
User Question
      │
      ▼
Query Rewrite
      │
      ▼
Semantic Search
      │
      ▼
Relevant Code Context
      │
      ▼
Prompt Construction
      │
      ▼
Google Gemini
      │
      ▼
AI Response
```

---

# ⚙️ Tech Stack

## Backend

- Java 21
- Spring Boot 3
- Spring AI
- Spring Data JPA
- Hibernate
- Maven

## AI

- Google Gemini
- Gemini Embedding API
- Retrieval-Augmented Generation (RAG)

## Database

- PostgreSQL

## Vector Database

- Qdrant

## Git Processing

- JGit

## Frontend

- React
- TypeScript
- Vite

## Infrastructure

- Docker
- Docker Compose

---

# 📁 Project Structure

```text
codepilot-ai/

├── src/
│   ├── controller/
│   ├── config/
│   ├── ingestion/
│   │      ├── parser/
│   │      ├── embedding/
│   │      ├── summary/
│   │      └── qdrant/
│   ├── retrieval/
│   │      ├── chat/
│   │      ├── search/
│   │      ├── classifier/
│   │      ├── history/
│   │      └── prompt/
│   ├── repository/
│   ├── entity/
│   └── service/
│
├── docker-compose.yml
├── Dockerfile
└── README.md
```

---

# 🚀 Running Locally

## Prerequisites

- Java 21
- Maven
- Docker Desktop

---

## Clone Repository

```bash
git clone https://github.com/<your-username>/codepilot-ai.git
cd codepilot-ai
```

---

## Configure Environment Variables

Create a `.env` file:

```properties
POSTGRES_PASSWORD=postgres

GOOGLE_API_KEY=YOUR_GEMINI_API_KEY

FRONTEND_URL=http://localhost:5173
```

---

## Build

Windows

```powershell
.\mvnw clean package
```

Linux/macOS

```bash
./mvnw clean package
```

---

## Start Everything

```bash
docker compose up --build
```

---

# 🌐 Services

| Service | URL |
|----------|-----|
| Backend | http://localhost:8080 |
| Frontend | http://localhost:5173 |
| Actuator | http://localhost:8080/actuator/health |
| Qdrant Dashboard | http://localhost:6333/dashboard |

---

# 🐳 Docker Architecture

The application runs using three Docker containers.

| Container | Purpose |
|------------|----------|
| Backend | Spring Boot REST API |
| PostgreSQL | Stores repositories, conversations and metadata |
| Qdrant | Stores vector embeddings for semantic search |

---

# 🔐 Environment Variables

| Variable | Description |
|----------|-------------|
| GOOGLE_API_KEY | Google Gemini API Key |
| POSTGRES_PASSWORD | PostgreSQL password |
| DB_URL | PostgreSQL connection URL |
| DB_USERNAME | PostgreSQL username |
| DB_PASSWORD | PostgreSQL password |
| QDRANT_HOST | Qdrant hostname |
| QDRANT_PORT | Qdrant gRPC port |
| REPOSITORY_ROOT | Repository storage path |

---

# 📡 API Endpoints

## Repository

```
POST /repositories
```

Create a repository.

---

## Repository Ingestion

```
POST /repositories/{id}/ingest
```

Indexes repository and generates embeddings.

---

## Chat

```
POST /chat
```

Ask questions about the repository.

---

# 📸 Screenshots

> Replace these placeholders with actual screenshots.

## Dashboard

```
docs/images/dashboard.png
```

---

## Repository Ingestion

```
docs/images/ingestion.png
```

---

## Chat Interface

```
docs/images/chat.png
```

---

## Qdrant Dashboard

```
docs/images/qdrant-dashboard.png
```

---

# 🎥 Demo

A short demo video or GIF can be added here.

Example:

```
docs/demo.gif
```

---

# 🛣️ Future Enhancements

- JWT Authentication
- Multi-user support
- GitHub OAuth
- Repository synchronization
- Streaming AI responses (SSE)
- Redis caching
- GitHub Actions CI/CD
- Kubernetes deployment
- Monitoring & Observability
- Multi-LLM support (OpenAI, Anthropic, Ollama)

---

# 🤝 Contributing

Contributions are welcome!

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a Pull Request.

---

# 📄 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Sriram Mallina**

Software Engineer

Java • Spring Boot • Microservices • AI • RAG • Docker • PostgreSQL • Qdrant

---

⭐ If you found this project useful, consider giving it a star!
