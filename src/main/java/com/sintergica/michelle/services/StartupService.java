package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.GithubConfig;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StartupService {
	private final GithubConfig githubConfig;
	public static GitHub ghConnection = null;
	public static GHOrganization organization;

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		try {
			ghConnection = GitHubBuilder.fromEnvironment().build();
			organization = ghConnection.getOrganization(githubConfig.getLogin());
		} catch (IOException e) {
			System.err.println("Connection to GH cannot be build from environment");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
