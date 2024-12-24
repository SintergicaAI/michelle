package com.sintergica.michelle.repositories;

import com.sintergica.michelle.services.StartupService;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GHRepositoryRepository {
	private final GHOrganization organization = StartupService.organization;
	public List<GHRepository> findAll() throws IOException {
		return organization.getRepositories().values().stream().toList();
	}

	public GHRepository findByName(String name) throws IOException {
		return organization.getRepository(name);
	}

	public GHRepository createRepository(String name) throws IOException {
		return organization.createRepository(name).create();
	}

	public void deleteRepository(String name) throws IOException {
		GHRepository repository = organization.getRepository(name);
		if (repository != null) {
			repository.delete();
		}
	}
}
