package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.DBConfig;
import com.sintergica.michelle.entities.AntonService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AntonHttpClient {
	private final DBConfig dbConfig;
	private final OkHttpClient client = new OkHttpClient();
	private final Moshi moshi = new Moshi.Builder().build();
	private final Type serviceType = Types.newParameterizedType(List.class, AntonService.class);
	private final JsonAdapter<List<AntonService>> serviceJsonAdapter = moshi.adapter(serviceType);

	private Request makeRequestWithUrl(String url) {
		return new Request.Builder()
			.url(url)
			.addHeader("Authorization", "Basic " + dbConfig.getPassword())
			.build();
	}

	public Response executeRequest(String url) throws IOException {
		return client.newCall(makeRequestWithUrl(url)).execute();
	}

	public List<AntonService> extractServicesFromResponse(Response response) throws IOException {
		List<AntonService> antonServices = serviceJsonAdapter.fromJson(response.body().source());
		response.close();
		return antonServices;
	}
}
