package com.sintergica.lightner_back.controllers;

import com.sintergica.lightner_back.services.StartupService;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/repository")
public class RepositoryController {
	@GetMapping()
	public ResponseEntity<?> getRepositories() {
		Map<String, String> repoInfo = new HashMap<>();
		try {
			GHOrganization organization = StartupService.ghConnection.getOrganization("SintergicaAI");
			Map<String, GHRepository> repositories = organization.getRepositories();
			repositories.forEach((name, repoValues) -> repoInfo.put(name, repoValues.getFullName()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return ResponseEntity.ok(repoInfo);
	}
}
