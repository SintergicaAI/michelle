package com.sintergica.michelle.controllers;


import com.myjeeva.digitalocean.pojo.Droplet;
import com.sintergica.michelle.entities.Server;
import com.sintergica.michelle.services.DigitalOceanService;
import com.sintergica.michelle.services.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/server")
public class ServerController {
	private final ServerService serverService;

	@GetMapping
	public ResponseEntity<?> getAllServers() {
		List<Server> servers = serverService.getServers();
		if (servers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(servers);
	}

	@PostMapping
	public ResponseEntity<?> registerServer(@RequestBody Server newServer) {
		if (serverService.registerIfNew(newServer)) {
			return new ResponseEntity<>(newServer, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@DeleteMapping("/{serverName}")
	public ResponseEntity<?> deleteServer(@PathVariable("serverName") String serverName) {
		if (serverService.deleteByName(serverName)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
