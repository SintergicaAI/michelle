package com.sintergica.michelle.services;

import com.sintergica.michelle.configuration.Logger;
import com.sintergica.michelle.entities.Server;
import com.sintergica.michelle.repositories.AntonServiceRepository;
import com.sintergica.michelle.repositories.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
	private final ServerRepository serverRepository;
	private final AntonServiceRepository serviceRepository;
	private final Logger logger;
	private static final int TIMEOUT_MILLS = 500;
	private static final int ANTON_PORT = 42000;

	private boolean serverExists(Server newServer) {
		return serverRepository.findByServerName(newServer.getServerName())
			.isPresent();
	}

	private void checkServerAnton(Server server) {
		try (Socket socket = new Socket()) {
			socket.setReuseAddress(true);
			try {
				socket.connect(new InetSocketAddress(server.getAddress(), ANTON_PORT), TIMEOUT_MILLS);
				server.setHasAnton(true);
			} catch (IOException e) {
				server.setHasAnton(false);
			}
		} catch (IOException e) {
			logger.logException(e);
		}
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
		List<Server> servers = serverRepository.getAll();
		servers.forEach(server -> {
			checkServerAnton(server);
			if (server.isHasAnton()) {
				server.setServices(serviceRepository.getServices(server.getAddress()));
			}
		});
		return servers;
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
