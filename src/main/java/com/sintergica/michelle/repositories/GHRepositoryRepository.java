package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.services.StartupService;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class GHRepositoryRepository {
	private final GithubConfig githubConfig;

	public List<GHRepository> findAll() throws IOException {
		GHOrganization organization = StartupService.ghConnection.getOrganization(githubConfig.getLogin());
		return organization.getRepositories().values().stream().toList();
	}
}
