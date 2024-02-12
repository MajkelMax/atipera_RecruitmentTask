# Github Branches API

This project provides a RESTful API to retrieve branches of GitHub repositories (with are not fork) associated with a given user.

## Prerequisites

Before running this application, ensure you have the following installed:
- Java Development Kit (JDK) version 21
- Apache Maven
- Git

## Getting Started

1. Clone the repository:
  git clone https://github.com/MajkelMax/atipera_RecruitmentTask.git
2. Navigate to the project directory
3. Build the project using Maven:
  mvn clean package
4. Run the application:
  java -jar target/recruitment-task-<version>.jar

Usage
The API provides the following endpoint:

GET /api/branch/{username}: Retrieves branches of GitHub repositories for the specified user.

Example: curl http://localhost:8080/api/branch/{username}

Dependencies
Spring Boot: Framework for building Spring-based applications.
Lombok: Library to reduce boilerplate code.
Spring Web: Library for building web applications.
Spring Boot Test: Library for testing Spring Boot applications.
