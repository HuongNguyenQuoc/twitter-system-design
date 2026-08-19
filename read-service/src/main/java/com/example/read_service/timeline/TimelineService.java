package com.example.read_service.timeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimelineService {

	private static final org.springframework.jdbc.core.RowMapper<TweetRow> TWEET_ROW_MAPPER =
					(rs, rowNum) -> new com.example.read_service.timeline.TweetRow(
									UUID.fromString(rs.getString("id")),
									UUID.fromString(rs.getString("user_id")),
									rs.getString("content"),
									rs.getTimestamp("created_at").toLocalDateTime()
					);

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final StringRedisTemplate redisTemplate;

	@Value("${fanout.celebrity-threshold}")
	private long celebrityThreshold;

	public TimelineService(NamedParameterJdbcTemplate jdbcTemplate, StringRedisTemplate redisTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.redisTemplate = redisTemplate;
	}

	public List<TimelineTweet> getHomeTimeline(UUID userId, int limit) {
		if (limit <= 0) {
			return List.of();
		}
		// Ai trong số người userId follow là celebrity? (cần pull riêng)
		List<UUID> celebrityIds = jdbcTemplate.query("""
										SELECT f.followee_id AS id FROM follows f
										JOIN users u ON f.followee_id = u.id
										WHERE f.follower_id = :userId AND u.followers_count > :celebrityThreshold
										""",
						new MapSqlParameterSource().addValue("userId", userId).addValue("celebrityThreshold", celebrityThreshold),
						(rs, rowNum) -> UUID.fromString(rs.getString("id"))
		);

		List<TweetRow> result = new ArrayList<>();

		// Push path: Lấy trong redis timeline của userId
		Set<String> pushedIdStrings = redisTemplate.opsForZSet().reverseRange("timeline:" + userId, 0, limit - 1);
		if (pushedIdStrings != null && !pushedIdStrings.isEmpty()) {
			List<UUID> pushedIds = pushedIdStrings.stream().map(UUID::fromString).toList();
			result.addAll(jdbcTemplate.query("""
											SELECT id, user_id, content, created_at FROM tweets
											WHERE id IN (:ids)
											""",
							new MapSqlParameterSource().addValue("ids", pushedIds),
							TWEET_ROW_MAPPER
			));
		}

		// Pull path: query những tweet mới nhất từ những người celebrity mà userId follow
		if (!celebrityIds.isEmpty()) {
			for (UUID celeb : celebrityIds) {
				result.addAll(jdbcTemplate.query("""
												SELECT id, user_id, content, created_at FROM tweets
												WHERE user_id = :celebId
												ORDER BY created_at DESC
												LIMIT :limit
												""",
								new MapSqlParameterSource().addValue("celebId", celeb).addValue("limit", limit),
								TWEET_ROW_MAPPER
				));
			}
		}

		// --- Gộp 2 nguồn, sort theo thời gian, cắt lấy top N ---
		return result.stream()
						.sorted(Comparator.comparing(TweetRow::createdAt).reversed())
						.limit(limit)
						.map(tr -> new TimelineTweet(tr.id(), tr.userId(), tr.content(), tr.createdAt()))
						.toList();
	}
}
