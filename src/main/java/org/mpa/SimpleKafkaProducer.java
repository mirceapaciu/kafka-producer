package org.mpa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class SimpleKafkaProducer {

    public static void main(String[] args) {
        // Producer config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka server
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Create producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // Create a record
            String topic = "my-topic";
            String key = "my-key";
            String value = "Hello from Java!";
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // Send the record
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get(); // Wait for ack
            System.out.printf("Sent message to topic %s, partition %d, offset %d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
