# URL Shortner API
## Overview
The URL Shortener API is a simple RESTful service that allows users to shorten long URLs, retrieve the original URLs, update and delete short URLs, and track statistics on the number of accesses.

## Features
- Shorten a long URL.
- Retrieve the original URL using the short code.
- Update an existing short URL.
- Delete a short URL.
- Get statistics on how many times a short URL has been accessed.

## Tech Stack
- **Java** (Spring Boot)
- **H2 Database** (In-Memory Database)
- **Spring Data JPA** (ORM)
- **Spring Boot Starter Web** (REST API)
- **JUnit** (Testing)

## API Endpoints

### 1. Create Short URL
**POST /shorten**

#### Request Body:
\`\`\`json
{
  \"url\": \"https://www.example.com/some/long/url\"
}
\`\`\`

#### Response (201 Created):
\`\`\`json
{
  \"id\": \"1\",
  \"url\": \"https://www.example.com/some/long/url\",
  \"shortCode\": \"abc123\",
  \"createdAt\": \"2021-09-01T12:00:00Z\",
  \"updatedAt\": \"2021-09-01T12:00:00Z\"
}
\`\`\`

#### Errors:
- 400 Bad Request: Invalid input.

### 2. Retrieve Original URL
**GET /shorten/{shortCode}**

#### Response (200 OK):
\`\`\`json
{
  \"id\": \"1\",
  \"url\": \"https://www.example.com/some/long/url\",
  \"shortCode\": \"abc123\",
  \"createdAt\": \"2021-09-01T12:00:00Z\",
  \"updatedAt\": \"2021-09-01T12:00:00Z\"
}
\`\`\`

#### Errors:
- 404 Not Found: Short URL not found.

### 3. Update Short URL
**PUT /shorten/{shortCode}**

#### Request Body:
\`\`\`json
{
  \"url\": \"https://www.example.com/some/updated/url\"
}
\`\`\`

#### Response (200 OK):
\`\`\`json
{
  \"id\": \"1\",
  \"url\": \"https://www.example.com/some/updated/url\",
  \"shortCode\": \"abc123\",
  \"createdAt\": \"2021-09-01T12:00:00Z\",
  \"updatedAt\": \"2021-09-01T12:30:00Z\"
}
\`\`\`

#### Errors:
- 400 Bad Request: Invalid input.
- 404 Not Found: Short URL not found.

### 4. Delete Short URL
**DELETE /shorten/{shortCode}**

#### Response (204 No Content)

#### Errors:
- 404 Not Found: Short URL not found.

### 5. Get URL Statistics
**GET /shorten/{shortCode}/stats**

#### Response (200 OK):
\`\`\`json
{
  \"id\": \"1\",
  \"url\": \"https://www.example.com/some/long/url\",
  \"shortCode\": \"abc123\",
  \"createdAt\": \"2021-09-01T12:00:00Z\",
  \"updatedAt\": \"2021-09-01T12:00:00Z\",
  \"accessCount\": 10
}
\`\`\`

#### Errors:
- 404 Not Found: Short URL not found.

## Installation and Running
### Prerequisites
- Java 21 (OpenJDK)
- Maven

### Steps to Run
1. Clone the repository:
   \`\`\`sh
   git clone <repository_url>
   cd ShortenerURL
   \`\`\`
2. Configure H2 database settings in \`application.properties\`:
   \`\`\`properties
   spring.datasource.url=jdbc:h2:mem:shortener_db
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.h2.console.enabled=true
   \`\`\`
3. Build and run the application:
   \`\`\`sh
   mvn spring-boot:run
   \`\`\`

## Testing
To run tests, execute:
\`\`\`sh
mvn test
\`\`\`

## License
This project is licensed under the MIT License.'
