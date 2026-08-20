# Twitter System Design

A learning project that rebuilds core pieces of Twitter's backend architecture — write path,
fan-out, home timeline (hybrid push/pull), search, and notifications — as independent Spring Boot
services communicating through Kafka.

## Architecture

| Service | Responsibility | Port |
|---|---|---|
| `tweet-write-service` | Create users, tweets, follow relationships. Publishes `tweet-created` events to Kafka. | 8080 |
| `fanout-worker` | Consumes `tweet-created`, pushes new tweet IDs into each follower's Redis timeline (`timeline:<userId>`). | — |
| `read-service` | Serves the home timeline: reads pre-computed IDs from Redis (push path) + live-queries celebrity accounts (pull path), merges and sorts. | 8081 |
| `search-service` | Consumes `tweet-created`, indexes tweet content into Elasticsearch for full-text search. | 8082 |
| `notification-worker` | Consumes `tweet-created`, logs who would be notified (placeholder for real push/email). | — |

All 5 services independently consume the same Kafka topic (`tweet-created`) using separate
consumer groups — no service knows about the others.

## Infrastructure

- **PostgreSQL** — source of truth (users, tweets, follows)
- **Redis** — timeline cache (sorted sets, fan-out on write)
- **Kafka** — event bus connecting the write path to downstream consumers
- **Elasticsearch** — full-text tweet search

## Running locally

1. Start infrastructure:
   ```bash
   docker-compose up -d
