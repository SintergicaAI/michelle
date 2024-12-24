package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.entities.Container;
import com.sintergica.michelle.entities.Metadata;
import com.sintergica.michelle.entities.Package;
import com.sintergica.michelle.entities.Tag;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubHttpClient {

	private final GithubConfig githubConfig;
	private final OkHttpClient client = new OkHttpClient();
	private final Moshi moshi = new Moshi.Builder().build();
	private final Type packageType = Types.newParameterizedType(List.class, Package.class);
	private final Type tagType = Types.newParameterizedType(List.class, Tag.class);
	private final JsonAdapter<List<Package>> packageJsonAdapter = moshi.adapter(packageType);
	private final JsonAdapter<List<Tag>> tagJsonAdapter = moshi.adapter(tagType);

	private Request makeRequestWithUrl(String url) {
		return new Request.Builder()
			.url(url)
			.header("Accept", "application/vnd.github+json")
			.addHeader("Authorization", "Bearer " + githubConfig.getToken())
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}

	public String getBaseUrl() {
		return githubConfig.getUrl() + "/orgs/" + githubConfig.getLogin();
	}

	public Response executeRequest(String url) throws IOException {
		return client.newCall(makeRequestWithUrl(url)).execute();
	}

	public List<Package> extractPackagesFromResponse(Response response) throws IOException {
		return packageJsonAdapter.fromJson(response.body().source());
	}

	public List<String> extractTagsFromResponse(Response response) throws IOException {
		return tagJsonAdapter.fromJson(response.body().source())
			.stream()
			.flatMap(tag -> Optional.ofNullable(tag.getMetadata())
				.map(Metadata::getContainer)
				.map(Container::getTags)
				.stream()
				.flatMap(List::stream))
			.sorted(Comparator.naturalOrder())
			.toList()
			.reversed();
	}
}
