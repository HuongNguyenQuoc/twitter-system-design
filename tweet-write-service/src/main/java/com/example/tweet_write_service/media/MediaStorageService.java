package com.example.tweet_write_service.media;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MediaStorageService {

	private final MinioClient minioClient;
	private final String bucket;

	public MediaStorageService(@Value("${minio.endpoint}") String endpoint,
	                           @Value("${minio.access-key}") String accessKey,
	                           @Value("${minio.secret-key}") String secretKey,
	                           @Value("${minio.bucket}") String bucket) {
		this.minioClient = MinioClient.builder()
						.endpoint(endpoint)
						.credentials(accessKey, secretKey)
						.build();
		this.bucket = bucket;
	}

	@PostConstruct
	public void ensureBucketExists() throws Exception {
		boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
		if(!exists) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
	}

	// Implement methods to handle media storage operations (upload, download, delete, etc.)
	public String upload(MultipartFile file) throws Exception {
		String objectKey = UUID.randomUUID() + "-" + file.getOriginalFilename();
		try (InputStream inputStream = file.getInputStream()) {
			minioClient.putObject(PutObjectArgs.builder()
											.object(objectKey)
											.stream(inputStream, file.getSize(), -1)
											.bucket(bucket)
											.contentType(file.getContentType())
							.build()
			);
		}
		return objectKey;
	}
}
