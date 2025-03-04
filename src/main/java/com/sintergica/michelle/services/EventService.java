package com.sintergica.michelle.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventService {
	private final ReceptorClient receptorClient;
	private final Map<String, List<String>> eventRecords = new HashMap<>();

	public void subscribe(String channel, String endpoint) {
		if (!endpoint.startsWith("/")) {
			endpoint = "/" + endpoint;
		}
		String address = getClientIpAddress() + endpoint;
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

	public static String getClientIpAddress() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		for (String header : IP_HEADER_CANDIDATES) {
			String ipList = request.getHeader(header);
			if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
				return ipList.split(",")[0];
			}
		}

		return request.getRemoteAddr();
	}

	private static final String[] IP_HEADER_CANDIDATES = {
		"X-Forwarded-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
		"REMOTE_ADDR"
	};
}
