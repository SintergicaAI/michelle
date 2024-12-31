package com.sintergica.michelle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "digitalocean")
public class DigitalOceanConfig {
	private String token;
	private int sshKeyId;
	private String sshKeyName;
	private String sshKeyFingerprint;
}
