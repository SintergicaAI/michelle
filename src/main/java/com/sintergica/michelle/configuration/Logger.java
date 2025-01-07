package com.sintergica.michelle.configuration;

import com.sintergica.michelle.services.MattermostHttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class Logger {
	private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private final LoggingConfig config;
	private final MattermostHttpClient client;

	private RequestBody buildRequestBody(String message) {
		String payload = config.getPayload();
		payload = payload.replace("$MESSAGE", message);
		System.out.println(payload);
		return RequestBody.create(payload, MediaType.get("application/json"));
	}

	private StringBuilder getLogStarter() {
		StringBuilder payload = new StringBuilder();
		DateTimeFormatter date = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

		payload.append(System.getProperty("user.name")).append("\n");
		payload.append(date.format(LocalDateTime.now())).append("\n");

		return payload;
	}

	public void logException(Exception exception) {
		Thread thread = new Thread(() -> {
			StringBuilder payload = getLogStarter();

			payload.append(exception.getMessage()).append("\n");

			for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
				payload.append(stackTraceElement.toString()).append("\n");
			}
			try {
				client.sendRequest(buildRequestBody(payload.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		thread.setName("LOGGER");
		thread.start();
	}

	public void logText(String text) {
		Thread thread = new Thread(() -> {
			StringBuilder payload = getLogStarter();
			payload.append(text);
			try {
				client.sendRequest(buildRequestBody(payload.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		thread.setName("LOGGER");
		thread.start();
	}
}
