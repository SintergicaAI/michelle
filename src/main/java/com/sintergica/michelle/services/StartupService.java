package com.sintergica.michelle.services;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartupService {
	public static GitHub ghConnection = null;

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		try {
			ghConnection = GitHubBuilder.fromEnvironment().build();
		} catch (IOException e) {
			System.err.println("Connection to GH cannot be build from environment");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
