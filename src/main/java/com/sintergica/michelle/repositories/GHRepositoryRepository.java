package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.services.StartupService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GHRepositoryRepository {
	public List<GHRepository> findAll() throws IOException {
		return StartupService.organization.getRepositories().values().stream().toList();
	}

	public GHRepository findByName(String name) throws IOException {
		return StartupService.organization.getRepository(name);
	}

	public GHRepository createRepository(String name) throws IOException {
		return StartupService.organization.createRepository(name).create();
	}
}
