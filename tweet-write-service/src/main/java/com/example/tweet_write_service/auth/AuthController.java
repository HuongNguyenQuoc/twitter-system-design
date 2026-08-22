package com.example.tweet_write_service.auth;

import com.example.tweet_write_service.user.User;
import com.example.tweet_write_service.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	public record RegisterRequest(@NotBlank String username, @NotBlank String password) {}
	public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
	public record AuthResponse(String auth_token) {}

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.username());
		user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
		User savedUser = userRepository.save(user);
		return new AuthResponse(jwtService.generateToken(savedUser.getId(), savedUser.getUsername()));
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.username())
						.orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

		if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
			throw new IllegalArgumentException("Invalid username or password");
		}

		return new AuthResponse(jwtService.generateToken(user.getId(), user.getUsername()));

	}

}
