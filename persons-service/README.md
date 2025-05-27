# Persons Service

A robust and scalable microservice for managing person records, built with Spring Boot 3.4.6 and Java 17.

## Features

- RESTful API for CRUD operations on person records
- JWT-based authentication and authorization
- Rate limiting to prevent abuse
- Redis caching for improved performance
- Comprehensive monitoring with Spring Boot Actuator and Prometheus
- Containerized deployment with Docker
- Kubernetes-ready with production-grade configurations
- Comprehensive test coverage
- OpenAPI (Swagger) documentation

## Technology Stack

- **Framework**: Spring Boot 3.4.6
- **Language**: Java 17
- **Database**: PostgreSQL
- **Caching**: Redis
- **Security**: Spring Security with JWT
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Monitoring**: Spring Boot Actuator, Prometheus, Grafana
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Testing**: JUnit 5, Mockito, TestContainers

## Prerequisites

- JDK 17 or later
- Maven 3.8.x or later
- Docker and Docker Compose
- Kubernetes cluster (for production deployment)
- PostgreSQL 15 or later
- Redis 7 or later

## Getting Started

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/your-org/rest-app.git
   cd rest-app/persons-service
   ```

2. Start the required services using Docker Compose:
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

3. Build and run the application:
   ```bash
   ./mvnw clean package
   java -jar target/persons-service-1.0.0.jar
   ```

The application will start on port 8081.

### API Documentation

Once the application is running, you can access:
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI Specification: http://localhost:8081/v3/api-docs

### Testing

Run the test suite:
```bash
./mvnw test
```

## Deployment

### Docker

Build the Docker image:
```bash
docker build -t persons-service:1.0.0 .
```

Run the container:
```bash
docker run -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgres \
  -e REDIS_HOST=redis \
  persons-service:1.0.0
```

### Kubernetes

1. Create the required secrets:
   ```bash
   kubectl apply -f k8s/config.yaml
   ```

2. Deploy the application:
   ```bash
   kubectl apply -f k8s/deployment.yaml
   ```

3. Verify the deployment:
   ```bash
   kubectl get pods -l app=persons-service
   ```

## Configuration

The application can be configured using environment variables or application properties:

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Application port | 8081 |
| `spring.profiles.active` | Active profile | dev |
| `spring.datasource.url` | Database URL | jdbc:postgresql://localhost:5432/persons_db |
| `spring.redis.host` | Redis host | localhost |
| `jwt.secret` | JWT secret key | (required) |
| `jwt.expiration` | JWT expiration time | 3600000 |
| `rate-limit.max-requests` | Rate limit requests | 100 |
| `rate-limit.window-size` | Rate limit window (seconds) | 60 |

## Monitoring

The application exposes several monitoring endpoints:

- Health check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`
- Info: `/actuator/info`

## Security

- JWT-based authentication
- Role-based access control
- Rate limiting
- CORS configuration
- Secure headers
- Input validation
- SQL injection prevention
- XSS protection

## Performance

- Redis caching for frequently accessed data
- Connection pooling for database and Redis
- Optimized JPA queries
- Pagination support
- Efficient resource utilization
- Horizontal scaling support

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please open an issue in the GitHub repository or contact the maintainers.

## Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL and Redis communities
- All contributors to the project
