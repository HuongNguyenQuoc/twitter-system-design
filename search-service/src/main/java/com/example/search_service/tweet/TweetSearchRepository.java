package com.example.search_service.tweet;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TweetSearchRepository extends ElasticsearchRepository<TweetDocument, String> {
	@Query("""
					{"match":
						{"content":
							{"query": "?0"}
						}
					}
					""")
	List<TweetDocument> searchByContent(String keyword);
}
