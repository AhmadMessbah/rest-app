# API Gateway

This is a Spring Cloud Gateway-based API Gateway service that provides a single entry point for all microservices in the application. It handles routing, security, rate limiting, and other cross-cutting concerns.

## Features

- **Service Routing**: Routes requests to appropriate microservices based on path patterns
- **JWT Authentication**: Validates JWT tokens and enforces security
- **Rate Limiting**: Implements rate limiting using Redis
- **CORS Support**: Configures CORS for cross-origin requests
- **Load Balancing**: Integrates with service discovery for load balancing
- **Monitoring**: Exposes metrics and health endpoints
- **Documentation**: Provides Swagger UI for API documentation

## Prerequisites

- Java 17 or later
- Maven 3.8.x or later
- Redis 6.2 or later
- Docker (for containerization)
- Kubernetes (for deployment)

## Building

```bash
mvn clean package
```

## Running Locally

1. Start Redis:
```bash
docker run -d --name redis -p 6379:6379 redis:6.2-alpine
```

2. Run the application:
```bash
java -jar target/api-gateway.jar
```

Or using Docker:
```bash
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```

## Configuration

The application can be configured using environment variables or application properties:

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | 8080 |
| `spring.redis.host` | Redis host | localhost |
| `spring.redis.port` | Redis port | 6379 |
| `spring.redis.password` | Redis password | |
| `jwt.secret` | JWT secret key | |
| `jwt.expiration` | JWT expiration time (ms) | 86400000 |
| `rate-limit.enabled` | Enable rate limiting | true |
| `rate-limit.default.replenish-rate` | Default requests per second | 100 |
| `rate-limit.default.burst-capacity` | Default burst capacity | 100 |

## API Endpoints

- `/api/persons/**` - Persons Service endpoints
- `/api/images/**` - Image Requests Service endpoints
- `/actuator/**` - Actuator endpoints for monitoring
- `/swagger-ui/**` - Swagger UI for API documentation
- `/v3/api-docs/**` - OpenAPI documentation

## Security

The API Gateway implements JWT-based authentication. All requests to protected endpoints must include a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Rate Limiting

Rate limiting is implemented using Redis and can be configured per endpoint:

- Default: 100 requests per second
- Persons Service: 50 requests per second
- Image Requests Service: 30 requests per second

## Monitoring

The application exposes several monitoring endpoints:

- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/gateway` - Gateway-specific metrics

## Deployment

### Docker

Build and push the Docker image:
```bash
docker build -t your-registry/api-gateway:latest .
docker push your-registry/api-gateway:latest
```

### Kubernetes

1. Create the namespace:
```bash
kubectl create namespace api-gateway
```

2. Apply the configurations:
```bash
kubectl apply -f k8s/config.yaml -n api-gateway
kubectl apply -f k8s/deployment.yaml -n api-gateway
```

## Development

### Running Tests

```bash
mvn test
```

### Code Style

The project uses the Google Java Style Guide. To format your code:

```bash
mvn spotless:apply
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 