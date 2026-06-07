# TODO

## Roadmap (prioritized)

### P1 — fix README drift (cheap, do first)

- **README claims `{"symbol":"DLT","quantity":100}` triggers retry+DLT.** The actual `OrderConsumer.consume()` (`quoteservice/.../consumer/OrderConsumer.java:25`) fails when the serialized message *contains the literal substring "fail"*. Either:
  - Change the README example to send a payload whose serialized form contains "fail" (and explain it that way), or
  - Change `OrderConsumer` to look at the parsed `symbol` field and fail when it equals `"DLT"` (matching the README narrative more precisely).
- Verify every other README example actually triggers the behaviour it claims while you're in there.

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