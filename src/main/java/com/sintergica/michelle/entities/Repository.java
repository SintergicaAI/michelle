package com.sintergica.michelle.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.github.GHRepository;

@Data
@NoArgsConstructor
public class Repository {
	private String name;
	private String fullName;
	private String url;

	public Repository(GHRepository repository) {
		this.name = repository.getName();
		this.fullName = repository.getFullName();
		this.url = repository.getHtmlUrl().toString();
	}
}
