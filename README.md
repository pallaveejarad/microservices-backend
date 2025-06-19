
![License](https://img.shields.io/badge/license-MIT-blue.svg)

# 🧱 Microservices Backend Application

This is a full-fledged **backend microservices system** built using the **Spring Boot ecosystem**, designed to simulate a real-world order processing system with proper architecture, inter-service communication, service discovery, API Gateway, circuit breaker, exception handling, and more.

---

## 📦 Modules / Microservices

| Service          | Port  | Description                                      |
|------------------|-------|--------------------------------------------------|
| API Gateway      | 8080  | Entry point for all external client requests     |
| Eureka Server    | 8761  | Service registry for discovery                   |
| Product Service  | 8081  | Manages product catalog (CRUD)                   |
| Order Service    | 8082  | Places and stores orders                         |
| Payment Service  | 8083  | Simulates order payment processing               |

---

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot 3.3.x**
- **Spring Cloud 2023.0.1**
- **Spring Cloud Gateway**
- **Spring Data JPA**
- **WebClient (Reactive HTTP client)**
- **Resilience4j (Circuit Breaker & Fallback)**
- **Eureka Discovery Server**
- **MySQL** (separate DB per service)
- **Maven** (build tool)
- **Lombok**
- **Docker (planned)**

---

## 🧭 Architecture

```plaintext
             [ Client / Postman ]
                    |
               [ API Gateway ]
                    |
    -------------------------------------
    |              |                 |
[Product]     [Order]        [Payment]
  Service      Service         Service
     \            |                /
      ------------|---------------
                   ↓
            [ Eureka Server ]
```

---

## 🛠️ Features Implemented

### ✅ Phase 1: Basic Service Setup
- Created separate Spring Boot projects
- Connected each service with Eureka
- Routed requests through API Gateway
- Setup MySQL for persistence
- CRUD for Product
- Order placement with payment simulation

### ✅ Phase 2: Resilience & Professional Error Handling
- WebClient for inter-service HTTP calls
- Resilience4j Circuit Breaker with fallback
- @ControllerAdvice for global error handling
- Custom error responses with proper HTTP codes

---

## 🔧 How to Run Locally

### ✅ Clone this repo

```bash
git clone https://github.com/pallaveejarad/microservices-backend.git
cd microservices-backend
```

### ✅ Make sure MySQL is running with databases:
- `productdb1`
- `orderdb`
- `paymentdb`

Username/password should be set in each `application.yml` file.

### ✅ Start services in this order:

```bash
# Eureka Server
./mvnw spring-boot:run -f eureka-server/pom.xml

# API Gateway
./mvnw spring-boot:run -f api-gateway/pom.xml

# Microservices
./mvnw spring-boot:run -f product-service/pom.xml
./mvnw spring-boot:run -f order-service/pom.xml
./mvnw spring-boot:run -f payment-service/pom.xml
```

---

## ✅ Test APIs via Postman

- Get all products → `GET http://localhost:8080/api/products`
- Create product → `POST http://localhost:8080/api/products`
- Place order → `POST http://localhost:8080/api/order`

---

## 📌 Next Phases (Coming Soon)

| Phase       | Description                                            |
|-------------|--------------------------------------------------------|
| 🔐 Phase 3  | Security with Spring Security & JWT                    |
| 🔍 Phase 4  | Distributed Tracing (Zipkin, Sleuth, Micrometer)       |
| ⚙️ Phase 5  | Centralized Config (Spring Cloud Config Server)        |
| 🐳 Phase 6  | Dockerize services & Docker Compose orchestration      |
| 🚀 Phase 7  | CI/CD using GitHub Actions                             |

---

## 👩‍💻 Author

**Pallavee Jarad**  
“Learning modern backend microservices step by step with hands-on code and smart design.”
