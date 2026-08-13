package com.example.tweet_write_service.tweet.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TweetResponse (
        UUID id,
        UUID userId,
        String content,
        LocalDateTime createdAt
) {}
