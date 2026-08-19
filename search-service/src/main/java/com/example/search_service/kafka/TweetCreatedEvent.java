package com.example.search_service.kafka;

import java.time.LocalDateTime;
import java.util.UUID;

public record TweetCreatedEvent(UUID tweetId, UUID userId, String content, LocalDateTime createdAt) {}
