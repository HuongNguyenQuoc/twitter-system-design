package com.example.fanout_worker.fanout;

import com.example.fanout_worker.kafka.TweetCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class FanoutService {

    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate redisTemplate;

    @Value("${fanout.celebrity-threshold}")
    private int celebrityThreshold;

    public FanoutService(JdbcTemplate jdbcTemplate, StringRedisTemplate redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    public void fanout(TweetCreatedEvent event) {
        Long followersCount = jdbcTemplate.queryForObject(
                "SELECT followers_count FROM users WHERE id = ?",
                Long.class,
                event.userId()
        );

        if (followersCount == null) {
            throw new IllegalArgumentException("User not found: " + event.userId());
        }

        if (followersCount > celebrityThreshold) {
            // For celebrity users, we can use a different strategy, like sending the tweet to a message queue for processing
            // Here we just log it for simplicity
            return;
        }

        List<UUID> followerIds = jdbcTemplate.queryForList(
                "SELECT follower_id FROM follows WHERE followee_id = ?",
                UUID.class, event.userId()
        );

        double score = event.createdAt().toEpochSecond(ZoneOffset.UTC);
        for (UUID followerId : followerIds) {
            String key = "timeline:" + followerId;
            redisTemplate.opsForZSet().add(key, event.tweetId().toString(), score);
        }
    }

}
