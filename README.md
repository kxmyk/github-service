# GitHub Repository Service

## Overview

This service provides an API to fetch GitHub repositories of a given user, including their branches and the latest commit SHA. It uses Spring Boot with WebClient for making API requests to GitHub.

## Features

- Fetches non-fork repositories for a given user.
- Retrieves branches for each repository along with the latest commit SHA.
- Returns a 404 response if the user does not exist.
- Uses a GitHub Personal Access Token (PAT) for authentication to avoid rate limits.

## Requirements

- Java 17+
- Maven 3.8+
- A GitHub Personal Access Token (PAT)

## Setup

### 1. Clone the Repository

```sh
git clone https://github.com/kxmyk/github-service.git
cd github-service
```

### 2. Generate a GitHub Personal Access Token

To authenticate API requests and avoid rate limits, generate a GitHub PAT:

1. Go to [GitHub Tokens](https://github.com/settings/tokens)
2. Click "Generate new token (classic)"
3. Select scope `public_repo`
4. Copy the token

### 3. Configure the Application

Create a `.env` file in the project root:

```
GITHUB_TOKEN=your_personal_access_token
```

Alternatively, export the token in your shell:

```sh
export GITHUB_TOKEN=your_personal_access_token
```

### 4. Build and Run the Application

```sh
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Get User Repositories

**Request:**

```sh
curl -X GET "http://localhost:8080/github/repos/{username}" -H "Authorization: Bearer {your_personal_access_token}"
```

**Response (200 OK):**

```json
[
  {
    "name": "repo-name",
    "ownerLogin": "username",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      }
    ]
  }
]
```

**Response (404 Not Found):**

```json
{
  "status": 404,
  "message": "User not found: {username}"
}
```

## Running Tests

Run the integration tests using:

```sh
mvn test
```

## Deployment

To package the application as a JAR:

```sh
mvn package
```

Run the JAR:

```sh
java -jar target/github-service-1.0.0.jar
```

## Notes

- The service does not handle pagination.
- It follows industry standards for API design.
- Written in Java with Spring Boot.

## License

This project is licensed under the MIT License.

