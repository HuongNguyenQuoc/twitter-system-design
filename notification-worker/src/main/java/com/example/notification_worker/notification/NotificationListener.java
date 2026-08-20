package com.example.notification_worker.notification;

import com.example.notification_worker.kafka.TweetCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

	private final JdbcTemplate jdbcTemplate;

	public NotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@KafkaListener(topics = "tweet-created", groupId = "notification-worker")
	public void onTweetCreated(TweetCreatedEvent event) {
		Long followerCount = jdbcTemplate.queryForObject(
						"SELECT COUNT(*) FROM followers WHERE followee_id = ?", Long.class, event.userId()
		);

		// To do: Replace this log with a real push (Firebase), ...
		log.info("Would notify {} followers about tweet {} from user {}",
						followerCount, event.tweetId(), event.userId());
	}
}
