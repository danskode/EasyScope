
# 📚 Technical Overview

## Backend:
Spring Boot (3.4.0): Provides the backbone for the application's business logic and APIs.
Database Communication: Uses JDBC to interact with our MySQL database (version 8.0.34).
Test Environment: Configured with an H2 in-memory database (version 2.3.232) for streamlined testing.
AI Integration: Powered by Spring AI (version 1.0.0-M5) to connect with OpenAI's APIs.

## Frontend:
Thymeleaf (3.4.0): Handles dynamic rendering of HTML pages.

## Deployment:
Hosted on Azure (Platform as a Service).
Deployment pipelines are automated via GitHub Actions, ensuring a smooth CI/CD process.
Version Control:
Collaboration and code management are conducted through Git and GitHub.

# 🚀 How to Contribute

## Fork the Repository: Start by forking the project on GitHub.
Set Up the Environment:
Install Java 17 and ensure you have IntelliJ IDEA (2024.2.4) or equivalent.
Clone the repository and set up the required dependencies (Spring Boot, Thymeleaf, JDBC, etc.).
Configure the database connection for local development. Default is MySQL; for testing, use H2.

## Code and Document:
Follow the coding guidelines (to be added soon in CONTRIBUTE.md).
Write meaningful commit messages and document your changes thoroughly.
Test Your Changes: Ensure your code passes all tests and works seamlessly in the CI/CD pipeline.
Create a Pull Request: Submit your changes for review and include a clear description of what your PR adds or fixes.

# 🛠️ What’s Missing?
We’re not looking for contributors to help us expand the project, as it is for educational purposes only! But thanks for asking!

## What could be done?
Improve security: We guess this is an important issue.
Test Coverage: Extend unit and integration tests for better stability.
New Features: Have ideas? Feel free to suggest or prototype them!
