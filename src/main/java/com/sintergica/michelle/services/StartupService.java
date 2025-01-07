package com.sintergica.michelle.services;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.sintergica.michelle.configuration.DigitalOceanConfig;
import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.configuration.Logger;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StartupService {
	private final Logger logger;
	private final GithubConfig githubConfig;
	private final DigitalOceanConfig digitalOceanConfig;
	public static GitHub ghConnection = null;
	public static GHOrganization organization;
	public static DigitalOcean doConnection = null;

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		logger.logText("STARTUP");
		try {
			ghConnection = GitHubBuilder.fromEnvironment().build();
			organization = ghConnection.getOrganization(githubConfig.getLogin());

			doConnection = new DigitalOceanClient(digitalOceanConfig.getToken());
		} catch (IOException e) {
			System.err.println("Connection to GH cannot be build from environment");
			logger.logException(e);
			System.exit(1);
		}
	}
}
