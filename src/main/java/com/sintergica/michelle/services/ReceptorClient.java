package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.DBConfig;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReceptorClient {
	private final DBConfig dbConfig;
	private final OkHttpClient client = new OkHttpClient();

	private Request makeRequestWithUrl(String url) {
		return new Request.Builder()
			.url(url)
			.addHeader("Authorization", "Basic " + dbConfig.getPassword())
			.build();
	}

	public Response executeRequest(String url) throws IOException {
		return client.newCall(makeRequestWithUrl(url)).execute();
	}

	public void executeAsyncRequest(String url, Function<Response, ?> callback) {
		new Thread(() -> {
			Response response;
			try {
				response = executeRequest(url);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (callback != null) {
				callback.apply(response);
			}
		}).start();
	}
}
