package com.example.tweet_write_service.media;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

	public record MediaResponse(UUID media_id) {} // This is a record class that represents the response from the server to the client

	private final MediaStorageService mediaStorageService;
	private final MediaRepository mediaRepository;

	public MediaController(MediaStorageService mediaStorageService, MediaRepository mediaRepository) {
		this.mediaStorageService = mediaStorageService;
		this.mediaRepository = mediaRepository;
	}

	@PostMapping
	public MediaResponse upload(@RequestParam("file") MultipartFile file,
	                            Authentication auth) throws Exception {

		UUID userId = (UUID) auth.getPrincipal(); // Get the user ID from the authentication object

		String objectKey = mediaStorageService.upload(file); // Upload the file to MinIO and get the object key

		Media media = new Media();
		media.setUserId(userId);
		media.setObjectKey(objectKey);
		media.setContentType(file.getContentType());
		Media savedMedia = mediaRepository.save(media); // Save the media metadata to the database

		return new MediaResponse(savedMedia.getId()); // Return the media ID to the client
	}

}
