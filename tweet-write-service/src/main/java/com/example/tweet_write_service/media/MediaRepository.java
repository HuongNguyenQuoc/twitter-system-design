package com.example.tweet_write_service.media;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
	// Dùng để kiểm tra media có thuộc về người đang đăng tweet không
	long countByIdInAndUserId(Collection<UUID> ids, UUID userId);
}
