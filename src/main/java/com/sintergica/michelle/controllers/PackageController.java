package com.sintergica.michelle.controllers;

import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.services.PackageService;
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
@RequestMapping("/package")
public class PackageController {
	private final PackageService packageService;

	@GetMapping
	public ResponseEntity<?> getPackages() {
		List<Package> packages = packageService.getPackages();
		if (packages == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (packages.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(packages);
	}
}
