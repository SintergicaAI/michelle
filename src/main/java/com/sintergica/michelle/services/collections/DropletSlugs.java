package com.sintergica.michelle.services.collections;

import lombok.Getter;

@Getter
public enum DropletSlugs {
	S_1VCPU_512MB_10GB("s-1vcpu-512mb-10gb"),
	S_1VCPU_1GB("s-1vcpu-1gb"),
	S_1VCPU_2GB("s-1vcpu-2gb"),
	S_2VCPU_2GB("s-2vcpu-2g"),
	S_2VCPU_4GB("s-2vcpu-4gb"),
	S_4VCPU_8GB("s-4vcpu-8gb"),
	S_8VCPU_16GB("s-8vcpu-16gb");

	private final String id;

	DropletSlugs(String id) {
		this.id = id;
	}

	public static DropletSlugs getByValue(String value) {
		for (DropletSlugs dropletSlug : DropletSlugs.values()) {
			if (dropletSlug.id.equalsIgnoreCase(value)) {
				return dropletSlug;
			}
		}
		return null;
	}
}
