# PawfectMatch

A pet adoption platform connecting shelters and adopters.

## Prerequisites

Install these before getting started:
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/)

## Setup Instructions

### Step 1 — Clone the repository
```
git clone <your-repo-url>
cd PawfectMatch
```

### Step 2 — Start the backend
```
docker compose up --build
```
Wait until you see `Started BackendApplication` in the terminal. This starts the Spring Boot backend on port 8080 and Appsmith on port 80.

### Step 3 — Set up Appsmith
1. Open your browser and go to `http://localhost`
2. Create an Appsmith account
3. On the home page click **Import**
4. Upload the `My first application.json` file from the repo
5. When asked about the datasource, keep the default settings

### Step 4 — Open the app
Click on the imported app and you're good to go!

## Test Credentials

**Providers (Shelters):**
| Email | Password |
|---|---|
| happypaws@shelter.com | password123 |
| sunnyrescue@shelter.com | password123 |
| pawsandclaws@rescue.com | password123 |
| fureverhome@shelter.com | password123 |

**Adopters:**
| Email | Password |
|---|---|
| alice@example.com | password123 |
| bob@example.com | password123 |
| carol@example.com | password123 |

## Features
- Login and signup for adopters and providers
- Adopters get matched with pets based on their preferences
- Browse all available pets
- Providers can add, edit and delete pets
- Messaging between adopters and providers
- Role-based access control

## Tech Stack
- **Backend:** Spring Boot, H2 in-memory database
- **Frontend:** Appsmith
- **Infrastructure:** Docker