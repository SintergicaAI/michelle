package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.Logger;
import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.services.GithubHttpClient;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class GithubPackageRepository {
	private static final String PACKAGE_TYPE_CONTAINER = "container";
	private final GithubHttpClient client;
	private final Logger logger;

	private String buildPackageUrl() {
		return client.getBaseUrl()
			+ "/packages?package_type=" + PACKAGE_TYPE_CONTAINER;
	}

	private String buildVersionUrl(String packageName, int page) {
		return client.getBaseUrl()
			+ "/packages/" + PACKAGE_TYPE_CONTAINER + "/" + packageName
			+ "/versions?per_page=100"
			+ "&page=" + page;
	}

	public List<Package> getPackages() {
		try {

			Response response = client.executeRequest(buildPackageUrl());

			if (response == null) {
				return Collections.emptyList();
			}

			List<Package> packages = client.extractPackagesFromResponse(response);
			packages.forEach(this::addTagsToPackage);
			return packages;
		} catch (IOException e) {
			logger.logException(e);
			return Collections.emptyList();
		}
	}

	public List<String> getPackageVersions(String packageName, int page) {
		try {
			Response response = client.executeRequest(buildVersionUrl(packageName, page));
			return response != null
				? client.extractTagsFromResponse(response)
				: Collections.emptyList();
		} catch (IOException e) {
			logger.logException(e);
			return Collections.emptyList();
		}
	}

	public void addTagsToPackage(Package _package) {
		List<String> tags = new ArrayList<>();
		int page = 1;
		List<String> packageVersions = getPackageVersions(_package.getName(), page);
		while (!packageVersions.isEmpty()) {
			page++;
			tags.addAll(packageVersions);
			packageVersions = getPackageVersions(_package.getName(), page);
		}
		_package.setTags(tags);
	}
}
