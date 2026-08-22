package com.example.tweet_write_service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

	@Modifying
	@Query("UPDATE User u SET u.followersCount = u.followersCount + 1 WHERE u.id = :userId")
	void increaseFollowers(@Param("userId") UUID userId);

	Optional<User> findByUsername(String username);
}