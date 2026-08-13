package com.example.tweet_write_service.follow;

import com.example.tweet_write_service.user.User;
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

    @Transactional
    public void followUser(UUID followerId, UUID followeeId) {
        FollowId id = new FollowId(followerId, followeeId);
        if (followRepository.existsById(id)) {
            throw new IllegalArgumentException("User is already following the followee.");
        }
        followRepository.save(new Follow(id));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new IllegalArgumentException("Followee not found."));
        followee.setFollowersCount(followee.getFollowersCount() + 1);
        userRepository.save(followee);
    }
}
