package com.sintergica.michelle.controllers;

import com.sintergica.michelle.entities.Repository;
import com.sintergica.michelle.services.RepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/repository")
public class RepositoryController {
	private final RepositoryService repositoryService;

	@GetMapping()
	public ResponseEntity<?> getRepositories() {
		List<Repository> repositories = repositoryService.getRepositories();
		if (repositories == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (repositories.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(repositories);
	}
}