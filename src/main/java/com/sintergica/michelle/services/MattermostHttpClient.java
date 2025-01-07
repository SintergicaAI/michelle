package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.LoggingConfig;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Service
@RequiredArgsConstructor
public class MattermostHttpClient {
	private final LoggingConfig config;
	private static OkHttpClient client = null;

	public void setClient() {
		TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				}
			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			client = new OkHttpClient.Builder()
				.sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
				.hostnameVerifier((hostname, session) -> true)
				.build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendRequest(RequestBody body) throws IOException {
		Request request = new Request.Builder()
			.url(config.getHook())
			.post(body)
			.build();
		if (client == null) {
			setClient();
		}
		client.newCall(request).execute().close();
	}
}
