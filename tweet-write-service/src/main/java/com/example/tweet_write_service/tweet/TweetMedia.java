package com.example.tweet_write_service.tweet;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "tweet_media")
@Getter
@Setter
@NoArgsConstructor
public class TweetMedia {

	@EmbeddedId
	private TweetMediaId tweetMediaId;

	public TweetMedia(TweetMediaId tweetMediaId) {
		this.tweetMediaId = tweetMediaId;
	}
}
