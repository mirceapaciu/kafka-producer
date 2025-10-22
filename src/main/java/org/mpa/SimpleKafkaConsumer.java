package org.mpa;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SimpleKafkaConsumer {

    public static void main(String[] args) {
        KafkaConfig config = new KafkaConfig();
        
        String topic = config.getDefaultTopic();
        String groupId = config.getDefaultConsumerGroup();
        
        if (args.length >= 1) {
            topic = args[0];
        }
        if (args.length >= 2) {
            groupId = args[1];
        }
        
        System.out.printf("Starting consumer for topic: %s, group: %s%n", topic, groupId);
        System.out.printf("Broker: %s%n", config.getBootstrapServers());

        // Consumer config from properties file
        Properties props = config.getConsumerProperties(groupId);

        // Create consumer
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscribe to topic
            consumer.subscribe(Collections.singletonList(topic));
            
            System.out.println("Consumer started. Press Ctrl+C to stop.");
            
            // Poll for messages
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message:%n");
                    System.out.printf("  Topic: %s%n", record.topic());
                    System.out.printf("  Partition: %d%n", record.partition());
                    System.out.printf("  Offset: %d%n", record.offset());
                    System.out.printf("  Timestamp: %d%n", record.timestamp());
                    System.out.printf("  Key: %s%n", record.key());
                    System.out.printf("  Value: %s%n", record.value());
                    System.out.println("---");
                }
                
                // Optional: break after first batch for testing
                // if (!records.isEmpty()) break;
            }
        } catch (Exception e) {
            System.err.println("Consumer error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}