package com.example.tweet_write_service.user;

import com.example.tweet_write_service.user.dto.CreateUserRequest;
import com.example.tweet_write_service.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class userController {

    private final UserRepository userRepository;

    public userController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        var user = new User();
        user.setUsername(request.username());
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getFollowersCount());
    }

}
