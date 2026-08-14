package com.example.fanout_worker.kafka;

import com.example.fanout_worker.fanout.FanoutService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TweetCreatedListener {

    private final FanoutService fanoutService;

    public TweetCreatedListener(FanoutService fanoutService) {
        this.fanoutService = fanoutService;
    }

    @KafkaListener(topics = "tweet-created", groupId = "fanout-worker")
    public void onTweetCreated(TweetCreatedEvent event) {
        fanoutService.fanout(event);
    }
}
