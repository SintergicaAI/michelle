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
		all.forEach(repository -> repositories.add(new Repository(repository)));
		return repositories;
	}

	public Repository createRepository(String name) {
		try {
			return new Repository(ghRepository.createRepository(name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Repository getRepositoryByName(String name) {
		try {
			GHRepository byName = ghRepository.findByName(name);
			if (byName == null) {
				return null;
			}
			return new Repository(byName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean deleteRepository(String name) {
		try {
			ghRepository.deleteRepository(name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return getRepositoryByName(name) == null;
	}
}
