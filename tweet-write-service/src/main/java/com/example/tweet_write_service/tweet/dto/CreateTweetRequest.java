package com.example.tweet_write_service.tweet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTweetRequest (
        @NotBlank @Size(max = 280) String content
) {}
