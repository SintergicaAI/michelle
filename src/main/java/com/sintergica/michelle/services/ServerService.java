package com.sintergica.michelle.services;

import com.sintergica.michelle.entities.Server;
import com.sintergica.michelle.repositories.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
	private final ServerRepository serverRepository;

	private boolean serverExists(Server newServer) {
		return serverRepository.findByServerName(newServer.getServerName())
			.isPresent();
	}

	public boolean registerIfNew(Server newServer) {
		boolean serverExists = serverExists(newServer);
		if (!serverExists) {
			serverRepository.save(newServer);
			return true;
		}
		return false;
	}

	public List<Server> getServers() {
		return serverRepository.getAll();
	}

	public Server getByName(String serverName) {
		return serverRepository.findByServerName(serverName)
			.orElse(null);
	}

	public boolean deleteByName(String serverName) {
		Server server = getByName(serverName);
		if (server != null) {
			serverRepository.deleteById(server.getId());
			return true;
		}
		return false;
	}
}
