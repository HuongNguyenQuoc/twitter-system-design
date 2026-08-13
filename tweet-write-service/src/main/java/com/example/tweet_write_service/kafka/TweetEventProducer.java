package com.example.tweet_write_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class TweetEventProducer {

    public static final String TOPIC = "tweet-created";

    private final KafkaTemplate<String, TweetCreatedEvent> kafkaTemplate;

    public TweetEventProducer(KafkaTemplate<String, TweetCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TweetCreatedEvent event) {
        // key = userId -> Kafka will use the key to determine the partition for the message
        // This ensures that all tweets from the same user will go to the same partition, preserving order
        kafkaTemplate.send(TOPIC, event.userId().toString(), event);
    }
}
