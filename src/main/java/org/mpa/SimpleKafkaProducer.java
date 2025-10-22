package org.mpa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class SimpleKafkaProducer {

    public static void main(String[] args) {
        KafkaConfig config = new KafkaConfig();
        
        String topic = config.getDefaultTopic();
        String key = "my-key";
        String value = "Hello from Java!";
        
        if (args.length >= 3) {
            System.out.println("Arguments provided: ");
            topic = args[0];
            key = args[1];
            value = args[2];
        } else {
            System.out.println("No arguments provided, using defaults.");
            System.out.printf("Broker: %s, Topic: %s%n", config.getBootstrapServers(), topic);
        }

        // Producer config from properties file
        Properties props = config.getProducerProperties();

        // Create producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // Create a record
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
