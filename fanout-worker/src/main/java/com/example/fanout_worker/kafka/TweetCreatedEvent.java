package com.example.fanout_worker.kafka;

import java.time.LocalDateTime;
import java.util.UUID;

public record TweetCreatedEvent (
        UUID tweetId,
        UUID userId,
        String content,
        LocalDateTime createdAt
){}
