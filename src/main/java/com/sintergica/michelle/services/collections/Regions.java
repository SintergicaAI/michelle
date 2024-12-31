package com.sintergica.michelle.services.collections;

import lombok.Getter;

@Getter
public enum Regions {
	SFO2("sfo2"),
	SFO3("sfo3");

	private final String id;
	Regions(String id) {
		this.id = id;
	}

	public static Regions getByValue(String value) {
		for (Regions region : Regions.values()) {
			if (region.id.equalsIgnoreCase(value)) {
				return region;
			}
		}
		return null;
	}
}
