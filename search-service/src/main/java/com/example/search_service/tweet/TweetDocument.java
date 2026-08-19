package com.example.search_service.tweet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@Document(indexName = "tweets")
@Getter
@Setter
@NoArgsConstructor
public class TweetDocument {

	@Id
	private String id;

	@Field(type = FieldType.Keyword)
	private String userId;

	@Field(type = FieldType.Text)
	private String content;

	@Field(type = FieldType.Date)
	private LocalDateTime createdAt;
}
