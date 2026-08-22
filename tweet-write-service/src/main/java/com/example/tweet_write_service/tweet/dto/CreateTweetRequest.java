package com.example.tweet_write_service.tweet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateTweetRequest (
  @NotBlank @Size(max = 280) String content,

  @Size(max = 4)
  @JsonProperty("media_ids")
  List<UUID> mediaIds
) {}
