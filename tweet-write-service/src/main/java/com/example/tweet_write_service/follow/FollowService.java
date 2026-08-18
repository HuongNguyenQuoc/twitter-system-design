package com.example.tweet_write_service.follow;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.tweet_write_service.user.UserRepository;

import java.util.UUID;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // Here we use @Transactionl to ensure that both the follow relationship creation and the followee's followers count more 1
    @Transactional
    public void followUser(UUID followerId, UUID followeeId) {
        FollowId id = new FollowId(followerId, followeeId);
        if (followRepository.existsById(id)) {
            throw new IllegalArgumentException("User is already following the followee.");
        }
        followRepository.save(new Follow(id));

				if (!userRepository.existsById(followeeId)) {
					throw new IllegalArgumentException("Followee user not found: " + followeeId);
				}
        userRepository.increaseFollowers(followeeId);
    }
}
