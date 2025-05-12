# DorossAI - AI-Powered Educational Tool

DorossAI is a Spring Boot application that provides AI-powered educational tools through a RESTful API. It integrates with a FastAPI service to deliver features like flashcard generation, quiz creation, text summarization, and key point extraction.

## Technologies Used

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for database operations
- **H2 Database** for data storage
- **Swagger/OpenAPI** for API documentation
- **Lombok** for reducing boilerplate code
- **FastAPI** integration for AI functionality

## Features

DorossAI offers the following AI-powered educational features:

1. **Flashcard Generation**: Generate educational flashcards on various topics
2. **Quiz Creation**: Create quizzes with questions and answers on specific topics
3. **Text Summarization**: Summarize long texts into concise summaries
4. **Key Point Extraction**: Extract key points from educational content

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven
- FastAPI service running on port 8000 (or configured port)

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/dorossai.git
   cd dorossai
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080 by default.



## API Documentation

The API documentation is available through Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Authentication

- **POST /api/auth/signup**: Register a new user
  - Request body: `{ "name": "User Name", "email": "user@example.com", "password": "password" }`
  - Response: JWT token and user details

- **POST /api/auth/login**: Authenticate a user
  - Request body: `{ "email": "user@example.com", "password": "password" }`
  - Response: JWT token and user details

### Flashcards

- **POST /api/generate-flashcards**: Generate flashcards on a topic
  - Request body: `{ "topic": "Topic Name", ... }`
  - Response: Generated flashcards

### Quizzes

- **POST /api/generate-quiz**: Generate a quiz on a topic
  - Request body: `{ "topic": "Topic Name", ... }`
  - Response: Generated quiz questions and answers

### Summarization

- **POST /api/summarize**: Summarize text
  - Request body: `{ "text": "Long text to summarize", ... }`
  - Response: Summarized text

### Key Points

- **POST /api/extract-key-points**: Extract key points from text
  - Request body: `{ "text": "Text to extract key points from", ... }`
  - Response: Extracted key points

## Authentication

The application uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Obtain a JWT token by registering or logging in
2. Include the token in the Authorization header of subsequent requests:
   ```
   Authorization: Bearer your-jwt-token
   ```

## Database

The application uses an H2 in-memory database by default. The H2 console is available at:
```
http://localhost:8080/h2-console
```

## About DorossAI

DorossAI is designed to help students and educators leverage AI for educational purposes. The name "Doross" combined with "AI" represents the fusion of education and artificial intelligence to create powerful learning tools.
