package com.sintergica.michelle.services.collections;

import lombok.Getter;

@Getter
public enum Images {
	DEBIAN_12x64("168639140"),
	DOCKER_UBUNTU("173563496");

	private final String id;

	Images(final String id) {
		this.id = id;
	}

	public static Images getByValue(String value) {
		for (Images image : Images.values()) {
			if (image.id.equalsIgnoreCase(value)) {
				return image;
			}
		}
		return null;
	}
}
