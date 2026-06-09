# TODO

## Roadmap (prioritized)

### P1 — done

- `OrderConsumer` now matches the documented trigger: `msg.contains("DLT")`
  fires on a payload whose `symbol` is `DLT`, matching the README example.
  Test renamed to `payloadContainingDltSymbolThrowsToTriggerRetryAndDlt`.
- The substring match is intentionally simple-stupid for a demo; a real
  consumer would deserialize and inspect the parsed field. Worth noting
  if anyone re-uses the pattern for production code.

### P2 — pick one production-semantics investment

The review's call for "production messaging semantics" is right but a shopping list. Pick one and ship it well:

- **Broker-backed retry-to-DLT integration test.** Testcontainers Kafka, push a failing message, assert it ends up on the `-dlt` topic after the retry topics. Demonstrates the most distinctive behaviour in this repo end-to-end.
- **Idempotent consumer + duplicate-delivery test.** Persist processed-message ids, verify a replayed event isn't processed twice. Closest to real-world consumer pain.
- **Transactional outbox.** Producer writes the outgoing event in the same DB transaction as its state change; a separate publisher drains the outbox. The pattern every "exactly once" system actually uses.

### P3 — stretch / observability (later)

- Avro or Protobuf schemas with compatibility checks instead of stringified JSON.
- Correlation IDs propagated end-to-end; OpenTelemetry traces.
- Consumer-lag / retry / DLT dashboards (Prometheus + Grafana).
- Failure-injection integration tests (broker disconnect, slow consumer).
