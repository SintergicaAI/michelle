package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.GeneralConfiguration;
import com.sintergica.michelle.configuration.Logger;
import com.sintergica.michelle.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final TokenRepository repository;
	private final ServerService serverService;
	private final ReceptorClient receptorClient;
	private final GeneralConfiguration generalConfiguration;
	private final Logger logger;
	private final List<String> tokens = new ArrayList<>();

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		this.repository.findAll().forEach(token -> tokens.add(token.getToken()));
	}

	public List<String> getBannedTokens() {
		return this.tokens;
	}

	public void banToken(String token) {
		if (this.tokens.contains(token)) {
			return;
		}
		this.tokens.add(token);
		serverService.getServers().forEach(server -> {
			String url = server.getAddress() + generalConfiguration.getWebhookUrl() + token;
			reportBannedToken(url);
		});
	}

	private void reportBannedToken(String url) {
		new Thread(() -> {
			try {
				receptorClient.executeRequest(url);
			} catch (IOException e) {
				logger.logException(e);
			}
		}).start();
	}
}
