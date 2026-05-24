# Kafka Microservices Demo

[![CI](https://github.com/damian1000/kafka-microservices-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/damian1000/kafka-microservices-demo/actions/workflows/ci.yml)
[![JDK](https://img.shields.io/badge/jdk-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Spring Boot](https://img.shields.io/badge/spring--boot-4.0.6-brightgreen)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/kafka-4.3.0-blue)](https://kafka.apache.org/)

Two Spring Boot 4 microservices, Kafka 4.x, end-to-end retry + dead-letter handling. The whole stack runs locally with `docker compose up` plus two `./gradlew bootRun` invocations.

## What this demonstrates

- **Multi-module Gradle 9 build** with two independently runnable Spring Boot 4 services
- **Synchronous HTTP** between services (`quoteservice` → `orderbook` for best-bid-offer)
- **Asynchronous Kafka messaging** (`orderbook` publishing order events on the `order` topic)
- **`@RetryableTopic` + `@DltHandler`** — the Spring Kafka 4 non-blocking retry pattern with automatic dead-letter routing
- **Java 25** end-to-end (toolchain, source and target)
- **Confluent stack** (Kafka 7.9.5 / Zookeeper / Control Center) via Docker Compose

## Architecture

```
  ┌──────────────────┐   POST /quote/request/create   ┌──────────────────┐
  │   HTTP client    │ ──────────────────────────────▶│  quoteservice    │
  │  (curl/postman)  │ ◀────────── Quote ─────────────│   port 8081      │
  └──────────────────┘                                └────────┬─────────┘
                                                                │ POST /order/bbo
                                                                ▼
                                                       ┌──────────────────┐
                                                       │    orderbook     │
                                                       │    port 8083     │
                                                       └────────┬─────────┘
                                                                │ publishes
                                                                ▼
                                                       ┌──────────────────┐
                                                       │  Kafka broker    │
                                                       │   topic: order   │
                                                       └────────┬─────────┘
                                                                │ consumed by
                                                                ▼
                                                       ┌──────────────────┐
                                                       │ OrderConsumer in │
                                                       │   quoteservice   │
                                                       │ retry → DLT      │
                                                       └──────────────────┘
```

## Prerequisites

- **JDK 25** (the Gradle toolchain will fetch one if needed, but having it locally is faster)
- **Docker** (for the Kafka broker stack)

## Quick start

```bash
# 1. Start Kafka broker + Zookeeper + Control Center
docker compose up -d

# 2. In one terminal, run orderbook (port 8083)
./gradlew :orderbook:bootRun

# 3. In another terminal, run quoteservice (port 8081)
./gradlew :quoteservice:bootRun

# 4. Hit the quote endpoint
curl -X POST http://localhost:8081/quote/request/create \
    -H 'Content-Type: application/json' \
    -d '{"symbol":"AAPL","quantity":100}'

# Response:
# {"symbol":"AAPL","quantity":100,"bid":20050,"offer":20100}
```

To trigger the Kafka flow:

```bash
# orderbook will publish a "createOrderEvent" to the 'order' topic
curl -X POST http://localhost:8083/order/create \
    -H 'Content-Type: application/json' \
    -d '{"symbol":"AAPL","quantity":100}'
```

The `OrderConsumer` inside `quoteservice` will pick the message up. To see retry + DLT in action, inspect the consumer code (`quoteservice/src/main/java/com/example/kafkademo/consumer/OrderConsumer.java`) — the `@RetryableTopic(attempts = "3", backOff = @BackOff(delay = 5000, multiplier = 3.0))` annotation produces `order-retry-5000`, `order-retry-15000`, and `order-dlt` topics automatically. You can watch them at the Control Center UI: http://localhost:9021

## Ports

| Service | Port |
|---|---|
| quoteservice (HTTP) | 8081 |
| orderbook (HTTP) | 8083 |
| Kafka broker | 9092 |
| Confluent Control Center | 9021 |
| Zookeeper | 2181 |

## Stack

- Java 25
- Spring Boot 4.0.6
- Spring for Apache Kafka 4.x
- Apache Kafka clients 4.3.0
- Gradle 9.5.1 (multi-module)
- Apache HttpClient 5
- Jackson, Lombok, JUnit Jupiter 6
- Confluent Platform 7.9.5 (broker + Zookeeper + Control Center)

## Tests

```bash
./gradlew test
```

Both modules have Spring Boot context-load tests that exercise a live broker — `docker compose up -d` must be running first.

## Notes on design

- **Why HTTP for BBO instead of Kafka?** Best-bid-offer is a synchronous request/response — you call it, you need the answer right now. Kafka is used where the producer doesn't need to wait (the order event in `orderbook`).
- **Why non-blocking retry?** With `@RetryableTopic`, failures don't park the consumer thread on `Thread.sleep`. Spring routes the message to a delay topic, frees the consumer, and re-attempts later. After N attempts, it goes to the DLT.
- **Multi-module Gradle?** Each service is independently runnable and could be split into its own repo. Co-locating them keeps the demo self-contained — clone, run, see the flow.

## License

Apache 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).
