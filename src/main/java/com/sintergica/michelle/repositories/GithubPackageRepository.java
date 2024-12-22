package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.entities.Tag;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class GithubPackageRepository {
	private static final String PACKAGE_TYPE_CONTAINER = "container";

	private final GithubConfig githubConfig;
	private final OkHttpClient client = new OkHttpClient();
	private final Moshi moshi = new Moshi.Builder().build();
	private final Type packageType = Types.newParameterizedType(List.class, Package.class);
	private final Type tagType = Types.newParameterizedType(List.class, Tag.class);
	private final JsonAdapter<List<Package>> packageJsonAdapter = moshi.adapter(packageType);
	private final JsonAdapter<List<Tag>> tagJsonAdapter = moshi.adapter(tagType);

	public List<Package> getPackages() {
		Response response;

		try {
			response = client.newCall(makeRequestWithUrl(buildPackageUrl())).execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (response.body() != null) {
			List<Package> packages = extractPackagesFromResponse(response);
			if (packages != null) {
				packages.forEach(this::addTagsToPackage);
			} else {
				return Collections.emptyList();
			}
			return packages;
		}
		return Collections.emptyList();
	}

	public List<String> getPackageVersions(String packageName) {

		try {
			Response response = client.newCall(makeRequestWithUrl(buildVersionUrl(packageName))).execute();
			if (response.body() != null) {
				List<String> finalTags = new ArrayList<>();
				List<Tag> tags = tagJsonAdapter.fromJson(response.body().source());

				if (tags != null) {
					tags.forEach(tag -> {
						List<String> internalTags = tag.getMetadata().getContainer().getTags();
						if (internalTags != null && !internalTags.isEmpty()) {
							finalTags.addAll(internalTags);
						}
					});
				}
				return finalTags;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private String buildPackageUrl() {
		return githubConfig.getUrl()
			+ "/orgs/" + githubConfig.getLogin()
			+ "/packages?package_type=" + PACKAGE_TYPE_CONTAINER;
	}

	private String buildVersionUrl(String packageName) {
		return githubConfig.getUrl()
			+ "/orgs/" + githubConfig.getLogin()
			+ "/packages/" + PACKAGE_TYPE_CONTAINER + "/" + packageName + "/versions";
	}

	private Request makeRequestWithUrl(String url) {
		return new Request.Builder()
			.url(url)
			.header("Accept", "application/vnd.github+json")
			.addHeader("Authorization", "Bearer " + githubConfig.getToken())
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}

	private List<Package> extractPackagesFromResponse(Response response) {
		try {
			return packageJsonAdapter.fromJson(response.body().source());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void addTagsToPackage(Package _package) {
		List<String> versions = getPackageVersions(_package.getName());
		if (versions != null && !versions.isEmpty()) {
			_package.setTags(versions);
		}
	}
}
