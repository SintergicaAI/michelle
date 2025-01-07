package com.sintergica.michelle.repositories;

import com.sintergica.michelle.configuration.Logger;
import com.sintergica.michelle.entities.AntonService;
import com.sintergica.michelle.services.AntonHttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AntonServiceRepository {
	private static final String SERVICE_PATH = "/service";
	private static final String ANTON_PORT = ":42000";
	private final AntonHttpClient client;
	private final Logger logger;

	public List<AntonService> getServices(String address) {
		address += ANTON_PORT;
		if (!address.contains("http")) {
			address = "http://" + address;
		}
		try {
			Response response = client.executeRequest(address + SERVICE_PATH);
			if (response == null) {
				return Collections.emptyList();
			}
			return client.extractServicesFromResponse(response);
		} catch (IOException e) {
			logger.logException(e);
			return Collections.emptyList();
		}
	}
}
