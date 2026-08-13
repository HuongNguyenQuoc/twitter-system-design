package com.example.tweet_write_service.tweet;

import com.example.tweet_write_service.kafka.TweetCreatedEvent;
import com.example.tweet_write_service.kafka.TweetEventProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final TweetEventProducer tweetEventProducer;

    public TweetService(TweetRepository tweetRepository, TweetEventProducer tweetEventProducer) {
        this.tweetRepository = tweetRepository;
        this.tweetEventProducer = tweetEventProducer;
    }

    public Tweet createTweet(UUID userId, String content) {
        Tweet tweet = new Tweet();
        tweet.setUserId(userId);
        tweet.setContent(content);
        Tweet savedTweet = tweetRepository.save(tweet);

        tweetEventProducer.publish(new TweetCreatedEvent(
                savedTweet.getId(),
                savedTweet.getUserId(),
                savedTweet.getContent(),
                savedTweet.getCreatedAt()
        ));

        return savedTweet;
    }
}
