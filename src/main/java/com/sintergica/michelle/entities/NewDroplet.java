package com.sintergica.michelle.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewDroplet {
	private String name;
	private String slug;
	private String region;
	private String image;
}
