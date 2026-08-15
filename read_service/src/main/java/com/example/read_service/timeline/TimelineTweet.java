package com.example.read_service.timeline;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimelineTweet(
				UUID id,
				UUID userId,
				String content,
				LocalDateTime createdAt
) {
}
