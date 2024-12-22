package com.sintergica.lightner_back.services;

import com.sintergica.lightner_back.entities.Package;
import com.sintergica.lightner_back.repositories.GithubPackageRepository;
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
