package com.example.tweet_write_service.follow;

import com.example.tweet_write_service.follow.dto.FollowRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public void follow(@Valid @RequestBody FollowRequest request,
                       Authentication authentication) {
	    UUID followerId = (UUID) authentication.getPrincipal();
			followService.followUser(followerId, request.followeeId());
    }
}
