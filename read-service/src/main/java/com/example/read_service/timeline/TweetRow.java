package com.example.read_service.timeline;

import java.time.LocalDateTime;
import java.util.UUID;

record TweetRow(
				UUID id,
				UUID userId,
				String content,
				LocalDateTime createdAt
) {
}
