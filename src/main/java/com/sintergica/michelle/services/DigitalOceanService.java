package com.sintergica.michelle.services;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Key;
import com.myjeeva.digitalocean.pojo.Region;
import com.sintergica.michelle.configuration.DigitalOceanConfig;
import com.sintergica.michelle.entities.NewDroplet;
import com.sintergica.michelle.services.collections.DropletSlugs;
import com.sintergica.michelle.services.collections.Images;
import com.sintergica.michelle.services.collections.Regions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DigitalOceanService {
	private final DigitalOceanConfig config;

	private DigitalOcean client() {
		return StartupService.doConnection;
	}

	public List<Droplet> getDroplets() {
		int page = 1;
		int pageSize = 100;
		List<Droplet> droplets = new ArrayList<>();
		try {
			Droplets availableDroplets = client().getAvailableDroplets(page, pageSize);
			while (!availableDroplets.getDroplets().isEmpty()) {
				droplets.addAll(availableDroplets.getDroplets());
				page++;
				availableDroplets = client().getAvailableDroplets(pageSize, page);
			}
		} catch (DigitalOceanException | RequestUnsuccessfulException e) {
			throw new RuntimeException(e);
		}
		return droplets;
	}

	public Droplet getDropletByName(String name) {
		return getDroplets().stream()
			.filter(droplet -> droplet.getName().equalsIgnoreCase(name))
			.findFirst()
			.orElse(null);
	}

	public Droplet createDroplet(NewDroplet droplet) {
		return createDroplet(
			droplet.getName(),
			DropletSlugs.getByValue(droplet.getSlug()),
			Regions.getByValue(droplet.getRegion()),
			Images.getByValue(droplet.getImage())
		);
	}

	public Droplet createDroplet(String name, DropletSlugs slug, Regions region, Images image) {
		if (getDropletByName(name) != null) {
			return null;
		}

		Key key = new Key(config.getSshKeyName());
		key.setFingerprint(config.getSshKeyFingerprint());
		key.setId(Integer.valueOf(config.getSshKeyId()));

		Droplet newDroplet = new Droplet();
		newDroplet.setName(name);
		newDroplet.setSize(slug.getId());
		newDroplet.setRegion(new Region(region.getId()));
		newDroplet.setImage(new Image(image.getId()));
		newDroplet.setEnablePrivateNetworking(true);
		newDroplet.setKeys(List.of(key));

		try {
			return client().createDroplet(newDroplet);
		} catch (DigitalOceanException | RequestUnsuccessfulException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean deleteDroplet(String name) {
		Droplet droplet = getDropletByName(name);
		if (droplet == null) {
			return false;
		}
		try {
			client().deleteDroplet(droplet.getId());
			return true;
		} catch (DigitalOceanException | RequestUnsuccessfulException e) {
			throw new RuntimeException(e);
		}
	}
}
