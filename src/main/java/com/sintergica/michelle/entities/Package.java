package com.sintergica.michelle.entities;

import lombok.Data;

import java.util.List;

@Data
public class Package {
	private int id;
	private String name;
	private String package_type;
	private List<String> tags;
}
