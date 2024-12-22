package com.sintergica.michelle.services;

import com.sintergica.michelle.entities.Repository;
import com.sintergica.michelle.repositories.GHRepositoryRepository;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RepositoryService {
	private final GHRepositoryRepository ghRepository;
	public List<Repository> getRepositories() {
		List<GHRepository> all;
		List<Repository> repositories = new ArrayList<>();
		try {
			all = ghRepository.findAll();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
		all.forEach(repository -> {
			Repository repo = new Repository();
			repo.setName(repository.getName());
			repo.setFullName(repository.getFullName());
			repositories.add(repo);
		});
		return repositories;
	}
}
