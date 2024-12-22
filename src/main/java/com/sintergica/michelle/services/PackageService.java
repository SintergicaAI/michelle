package com.sintergica.michelle.services;

import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.repositories.GithubPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PackageService {
	private final GithubPackageRepository packageRepository;

	public List<Package> getPackages() {
		return packageRepository.getPackages();
	}
}
