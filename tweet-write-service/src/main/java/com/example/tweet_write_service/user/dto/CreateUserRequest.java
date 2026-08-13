package com.example.tweet_write_service.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank String username) {
}


