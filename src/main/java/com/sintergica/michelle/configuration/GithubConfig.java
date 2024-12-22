package com.sintergica.michelle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "githubconfig")
public class GithubConfig {
	private String login;
	private String url;
	private String token;
}
