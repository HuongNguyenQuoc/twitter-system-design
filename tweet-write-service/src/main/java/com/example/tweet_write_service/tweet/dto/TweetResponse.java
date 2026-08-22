package com.example.tweet_write_service.tweet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TweetResponse (
        UUID id,
        UUID userId,
        String content,
				@JsonProperty("media_ids") List<UUID> mediaIds,
        LocalDateTime createdAt
) {}
