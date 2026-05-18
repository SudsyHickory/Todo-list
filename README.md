# Spring + React TODO App

## Running the Application

### Backend

1. From the root of the project, run `./mvnw spring-boot:run` to start the Spring Boot backend.

### Frontend

1. Navigate to the `dm-todo-frontend` directory.
2. Run `npm install` to install dependencies.
3. Run `npm run dev` to start the development server.

## Notes

- The frontend is mostly AI generated, but I can explain what's going on there
- The backend is running on `http://localhost:8080` and the frontend is running on `http://localhost:5173`
- The backend uses an in-memory H2 database

## Deployment

There is a `Dockerfile` written for the backend. I didn't have enough time to containerize the frontend, but ideally I would create the containers and use environment variables to configure the API URL.