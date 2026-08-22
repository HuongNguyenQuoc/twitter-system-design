package com.example.tweet_write_service.tweet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TweetMediaRepository extends JpaRepository<TweetMedia, TweetMediaId> {
	List<TweetMedia> findByIdTweetId(UUID tweetId);
}
