package com.example.tweet_write_service.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Catches: "User is already following", "Followee not found", etc.
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(
										new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
										ex.getMessage(),
										LocalDateTime.now())
						);
	}

	// Catches DB constraint errors, like a duplicate username
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body(
										new ErrorResponse(HttpStatus.CONFLICT.value(),
														"This record already exists or violates a database rule.",
										LocalDateTime.now())
						);
	}

	// Catches @Valid failures (e.g. blank username, content too long)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
						// List<FieldError> errors = ex.getBindingResult().getFieldErrors();
						.map(err -> err.getField() + ": " + err.getDefaultMessage())
						.reduce((a, b) -> a + "; " + b) // return Optional<String> it will combine all error messages into one string
						.orElse("Validation failed");
		return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(
										new ErrorResponse(
														HttpStatus.BAD_REQUEST.value(),
														message,
														LocalDateTime.now()
										)
						);
	}

	// Catch-all: anything unexpected becomes a clean 500, no stack trace leaked to the client
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(
										new ErrorResponse(
														HttpStatus.INTERNAL_SERVER_ERROR.value(),
														"An unexpected error occurred.",
														LocalDateTime.now()
										)
						);
	}
}
