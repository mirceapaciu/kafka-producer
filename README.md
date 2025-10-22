# kafka-producer

This is a minimal example project with a Kafka producer and consumer.

## Build

From the project root (requires Maven and Java installed):

```powershell
mvn -DskipTests package
```

This produces a shaded (fat) JAR in `target/` named `kafka-producer-1.0-SNAPSHOT-shaded.jar` which contains the runtime dependencies.

## Run producer

Uses `src/main/resources/kafka.properties` by default. To send a message:

```powershell
# using the shaded jar
java -jar .\target\kafka-producer-1.0-SNAPSHOT-shaded.jar "my-topic" "my-key" "Hello from Java!"

# or explicitly run the producer main class
java -cp .\target\kafka-producer-1.0-SNAPSHOT-shaded.jar org.mpa.SimpleKafkaProducer "my-topic" "my-key" "Hello from Java!"
```

If you don't pass arguments, the producer uses defaults declared in `kafka.properties`.

## Run consumer

The consumer is included in the same shaded JAR. Run it like this:

```powershell
# run with defaults (topic and consumer group read from kafka.properties)
java -cp .\target\kafka-producer-1.0-SNAPSHOT-shaded.jar org.mpa.SimpleKafkaConsumer

# or specify topic and group explicitly
java -cp .\target\kafka-producer-1.0-SNAPSHOT-shaded.jar org.mpa.SimpleKafkaConsumer "my-topic" "my-group"
```

## Notes

- If Kafka runs in Docker, ensure the `bootstrap.servers` in `kafka.properties` is reachable from where you run the JAR (use `host.docker.internal:9092` on some Docker setups or run the consumer/producer from a container on the same network).
- The shaded JAR contains both producer and consumer classes. The default `java -jar` behavior runs the `Main-Class` configured in the manifest (producer). Use `-cp` and the fully-qualified class name to run the consumer.
- To change broker/serialization settings, edit `src/main/resources/kafka.properties` and rebuild.
This is custom Kafka Producer.
