package com.sintergica.michelle.controllers;


import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.sintergica.michelle.entities.NewDroplet;
import com.sintergica.michelle.services.DigitalOceanService;
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
@RequestMapping("/droplet")
public class DropletController {
	private final DigitalOceanService digitalOceanService;

	@GetMapping
	public ResponseEntity<?> getDroplets() {
		List<Droplet> droplets = digitalOceanService.getDroplets();
		if (droplets.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(droplets);
	}

	@PostMapping
	public ResponseEntity<?> createDroplet(@RequestBody NewDroplet newDroplet) {
		if (digitalOceanService.getDropletByName(newDroplet.getName()) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		Droplet droplet = digitalOceanService.createDroplet(newDroplet);
		if (droplet == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(droplet, HttpStatus.CREATED);
	}

	@DeleteMapping("/{dropletName}")
	public ResponseEntity<?> deleteDroplet(@PathVariable String dropletName) {
		if (digitalOceanService.getDropletByName(dropletName) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (digitalOceanService.deleteDroplet(dropletName)) {
			return ResponseEntity.ok().build();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
