# Task Management Application

## Project Overview

Task Management Application is a RESTful service that allows users to manage their tasks. Built with Spring Boot, it
provides a robust API for creating, reading, updating, and deleting tasks.

### Technology Stack

- Java 17
- Spring Boot 3.4.3
- PostgreSQL 15
- Swagger/OpenAPI
- Docker & Docker Compose
- Prometheus & Grafana for monitoring

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17 or later
- Maven 3.6 or later

### Local Development Setup

1. Clone the repository:
   git clone <repository-url>
   cd tasker_app_backend

2. Start the development environment:

```bash
   docker-compose up -d
```

3. Access the applications:

- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

### API Documentation

The API documentation is available through Swagger UI at http://localhost:8080/swagger-ui.html

### Database Migrations

Database migrations are managed by Flyway and run automatically on application startup.

### Monitoring

The application includes:

- Prometheus metrics at /actuator/prometheus
- Grafana dashboards for visualization
- Health checks at /actuator/health

## Development Workflow

1. Create a new branch for your feature
2. Implement changes
3. Write tests
4. Create a pull request

## Testing

The project includes both unit and integration tests. Run tests with:

```bash
bash
mvn test
```

## Deployment


## Monitoring

The application exposes metrics through Spring Boot Actuator and Prometheus.
Grafana dashboards are available for:

- Application metrics
- JVM metrics
- Database metrics

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

[License Information]