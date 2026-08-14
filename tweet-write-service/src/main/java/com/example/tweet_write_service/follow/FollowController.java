package com.example.tweet_write_service.follow;

import com.example.tweet_write_service.follow.dto.FollowRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public void follow(@Valid @RequestBody FollowRequest request) {
        followService.followUser(request.followerId(), request.followeeId());
    }
}
