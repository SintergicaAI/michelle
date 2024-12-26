package com.sintergica.michelle.services;

import com.sintergica.michelle.entities.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
	private final List<Server> servers = new ArrayList<>();

	private boolean serverExists(Server newServer) {
		return servers.stream()
			.anyMatch(storedServer -> storedServer.getAddress().equalsIgnoreCase(newServer.getAddress()));
	}

	public boolean registerIfNew(Server newServer) {
		boolean serverExists = serverExists(newServer);
		return !serverExists && servers.add(newServer);
	}

	public List<Server> getServers() {
		return servers;
	}

	public Server getByName(String serverName) {
		return servers.stream()
			.filter(server -> server.getServerName().equalsIgnoreCase(serverName))
			.findFirst()
			.orElse(null);
	}

	public boolean deleteByName(String serverName) {
		Server server = getByName(serverName);
		if (server != null) {
			servers.remove(server);
			return true;
		}
		return false;
	}
}
