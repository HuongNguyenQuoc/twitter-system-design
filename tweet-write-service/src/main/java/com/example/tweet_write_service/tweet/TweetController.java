package com.example.tweet_write_service.tweet;


import com.example.tweet_write_service.tweet.dto.CreateTweetRequest;
import com.example.tweet_write_service.tweet.dto.TweetResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tweet")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public TweetResponse create(@Valid @RequestBody CreateTweetRequest request,
                                Authentication authentication) {
				// Ensure the userId in the request matches the authenticated user's ID
	    UUID userId = (UUID) authentication.getPrincipal();
      Tweet tweet = tweetService.createTweet(userId, request.content());
      return new TweetResponse(tweet.getId(), tweet.getUserId(), tweet.getContent(), tweet.getCreatedAt());
    }
}
