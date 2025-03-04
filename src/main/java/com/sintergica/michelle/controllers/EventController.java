package com.sintergica.michelle.controllers;

import com.sintergica.michelle.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
	private final EventService eventService;

	@PostMapping("/{channel}")
	public ResponseEntity<?> subscribe(
		@PathVariable String channel,
		@RequestBody String clientAddress) {

		eventService.subscribe(channel, clientAddress);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{channel}")
	public ResponseEntity<?> trigger(@PathVariable String channel) {
		if (!eventService.channelExists(channel)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		eventService.notify(channel);
		return ResponseEntity.ok().build();
	}
}
