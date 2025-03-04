package com.sintergica.michelle.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventService {
	private final ReceptorClient receptorClient;
	private final Map<String, List<String>> eventRecords = new HashMap<>();

	public void subscribe(String channel, String address) {
		this.channelIfNotExists(channel);
		this.addAddressToChannel(channel, address);
	}

	public void notify(String channel) {
		if (!eventRecords.containsKey(channel)) {
			return;
		}
		eventRecords.get(channel)
			.forEach(address -> receptorClient.executeAsyncRequest(address, null));
	}

	public boolean channelExists(String channel) {
		return eventRecords.containsKey(channel);
	}

	private void channelIfNotExists(String channel) {
		if (!eventRecords.containsKey(channel)) {
			eventRecords.put(channel, new ArrayList<>());
		}
	}

	private void addAddressToChannel(String channel, String address) {
		List<String> addressList = eventRecords.get(channel);
		if (!addressList.contains(address)) {
			addressList.add(address);
		}
	}
}
