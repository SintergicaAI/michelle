package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.GithubConfig;
import com.sintergica.michelle.entities.Package;
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
import java.util.List;

@Component
@AllArgsConstructor
public class GithubPackageRepository {
	private final GithubConfig githubConfig;
	private final OkHttpClient client = new OkHttpClient();
	private final Moshi moshi = new Moshi.Builder().build();
	private final Type type = Types.newParameterizedType(List.class, Package.class);
	private final JsonAdapter<List<Package>> packageJsonAdapter = moshi.adapter(type);
	public List<Package> getPackages() {
		String url = githubConfig.getUrl();
		url += "/orgs/" + githubConfig.getLogin() + "/packages?package_type=container";
		Request request = new Request.Builder()
			.url(url)
			.header("Accept", "application/vnd.github+json")
			.addHeader("Authorization", "Bearer " + githubConfig.getToken())
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();

		try {
			Response response = client.newCall(request).execute();
			if (response.body() != null) {
				return packageJsonAdapter.fromJson(response.body().source());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
