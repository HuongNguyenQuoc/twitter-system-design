package com.example.tweet_write_service.follow.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FollowRequest(@NotNull UUID followerId, @NotNull UUID followeeId) {
}
