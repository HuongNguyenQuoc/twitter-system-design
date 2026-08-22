package com.example.tweet_write_service.tweet;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TweetMediaId implements Serializable {

	private UUID tweetId;
	private UUID mediaId;
}
