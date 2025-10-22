package org.mpa;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaConfig {
    private static final String CONFIG_FILE = "kafka.properties";
    private final Properties properties;

    public KafkaConfig() {
        this.properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Unable to find " + CONFIG_FILE + ", using defaults");
                return getDefaultProperties();
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Error loading " + CONFIG_FILE + ": " + e.getMessage());
            return getDefaultProperties();
        }
        return props;
    }

    private Properties getDefaultProperties() {
        Properties defaults = new Properties();
        defaults.setProperty("bootstrap.servers", "localhost:9092");
        defaults.setProperty("producer.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        defaults.setProperty("producer.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        defaults.setProperty("consumer.key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        defaults.setProperty("consumer.value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        defaults.setProperty("consumer.auto.offset.reset", "earliest");
        defaults.setProperty("consumer.enable.auto.commit", "true");
        defaults.setProperty("consumer.auto.commit.interval.ms", "1000");
        defaults.setProperty("default.topic", "my-topic");
        defaults.setProperty("default.consumer.group", "my-consumer-group");
        return defaults;
    }

    public Properties getProducerProperties() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("bootstrap.servers"));
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getProperty("producer.key.serializer"));
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, properties.getProperty("producer.value.serializer"));
        return producerProps;
    }

    public Properties getConsumerProperties(String groupId) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("bootstrap.servers"));
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getProperty("consumer.key.deserializer"));
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getProperty("consumer.value.deserializer"));
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getProperty("consumer.auto.offset.reset"));
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, properties.getProperty("consumer.enable.auto.commit"));
        consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, properties.getProperty("consumer.auto.commit.interval.ms"));
        return consumerProps;
    }

    public String getDefaultTopic() {
        return properties.getProperty("default.topic", "my-topic");
    }

    public String getDefaultConsumerGroup() {
        return properties.getProperty("default.consumer.group", "my-consumer-group");
    }

    public String getBootstrapServers() {
        return properties.getProperty("bootstrap.servers", "localhost:9092");
    }
}