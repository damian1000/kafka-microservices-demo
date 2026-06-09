# TODO

- Add a broker-backed retry-to-DLT integration test using Testcontainers Kafka.
- Add an idempotent consumer with a duplicate-delivery test.
- Add a transactional outbox: producer writes events in the same DB transaction as its state change; a separate publisher drains the outbox.
- Replace stringified JSON payloads with Avro or Protobuf schemas and add compatibility checks.
- Propagate correlation IDs end-to-end and emit OpenTelemetry traces.
- Add failure-injection integration tests (broker disconnect, slow consumer).
