package com.example.search_service.tweet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

	private final TweetSearchRepository tweetSearchRepository;

	public SearchController(TweetSearchRepository tweetSearchRepository) {
		this.tweetSearchRepository = tweetSearchRepository;
	}

	@GetMapping
	public List<TweetDocument> searchTweets(@RequestParam String query) {
		return tweetSearchRepository.searchByContent(query);
	}
}
