package com.example.tweet_write_service.user.dto;

import java.util.UUID;

public record UserResponse(UUID id, String username, long followersCount) {
}
