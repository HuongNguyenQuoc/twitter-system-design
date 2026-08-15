package com.example.read_service.timeline;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
public class TimelineController {

	private final TimelineService timelineService;

	public TimelineController(TimelineService timelineService) {
		this.timelineService = timelineService;
	}

	@GetMapping("/home")
	public List<TimelineTweet> getHomeTimeline(@RequestParam UUID userId,
	                                           @RequestParam(defaultValue = "10") int limit) {
		return timelineService.getHomeTimeline(userId, limit);
	}
}
