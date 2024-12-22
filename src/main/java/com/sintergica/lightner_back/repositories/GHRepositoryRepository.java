package com.sintergica.lightner_back.repositories;

import com.sintergica.lightner_back.configuration.GithubConfig;
import com.sintergica.lightner_back.entities.Repository;
import com.sintergica.lightner_back.services.StartupService;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
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
