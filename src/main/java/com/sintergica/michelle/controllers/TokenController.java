package com.sintergica.michelle.controllers;

import com.sintergica.michelle.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {
	private final TokenService sessionService;
	@GetMapping("/banned")
	public ResponseEntity<?> getBannedTokens() {
		List<String> bannedTokens = sessionService.getBannedTokens();
		if (bannedTokens.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(bannedTokens);
	}

	@PostMapping("/{token}")
	public ResponseEntity<?> banToken(@PathVariable String token) {
		if (token != null && !token.isEmpty()) {
			sessionService.banToken(token);
			return ResponseEntity.ok().build();
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}
}
