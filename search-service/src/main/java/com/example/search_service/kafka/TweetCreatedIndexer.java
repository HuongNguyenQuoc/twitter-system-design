package com.example.search_service.kafka;

import com.example.search_service.tweet.TweetDocument;
import com.example.search_service.tweet.TweetSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TweetCreatedIndexer {

	private final TweetSearchRepository tweetSearchRepository;

	public TweetCreatedIndexer(TweetSearchRepository tweetSearchRepository) {
		this.tweetSearchRepository = tweetSearchRepository;
	}

	@KafkaListener(topics = "tweet-created", groupId = "search-service")
	public void onTweetCreated(TweetCreatedEvent event) {
		TweetDocument document = new TweetDocument();
		document.setId(event.tweetId().toString());
		document.setUserId(event.userId().toString());
		document.setContent(event.content());
		document.setCreatedAt(event.createdAt());
		tweetSearchRepository.save(document);
	}
}
