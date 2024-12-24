package com.sintergica.michelle.repositories;

import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.services.GithubHttpClient;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class GithubPackageRepository {

	private static final String PACKAGE_TYPE_CONTAINER = "container";
	private final GithubHttpClient githubHttpClient;

	private String buildPackageUrl() {
		return githubHttpClient.getBaseUrl()
			+ "/packages?package_type=" + PACKAGE_TYPE_CONTAINER;
	}

	private String buildVersionUrl(String packageName) {
		return githubHttpClient.getBaseUrl()
			+ "/packages/" + PACKAGE_TYPE_CONTAINER + "/" + packageName + "/versions";
	}

	public List<Package> getPackages() {
		try {
			Response response = githubHttpClient.executeRequest(buildPackageUrl());

			if (response == null) {
				return Collections.emptyList();
			}

			List<Package> packages = githubHttpClient.extractPackagesFromResponse(response);
			packages.forEach(this::addTagsToPackage);
			return packages;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getPackageVersions(String packageName) {
		try {
			Response response = githubHttpClient.executeRequest(buildVersionUrl(packageName));
			return response != null
				? githubHttpClient.extractTagsFromResponse(response)
				: Collections.emptyList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addTagsToPackage(Package _package) {
		List<String> versions = getPackageVersions(_package.getName());
		if (!versions.isEmpty()) {
			_package.setTags(versions);
		}
	}
}
