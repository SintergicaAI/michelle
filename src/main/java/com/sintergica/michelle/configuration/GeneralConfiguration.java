package com.sintergica.michelle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "general")
public class GeneralConfiguration {
	private String webhookUrl;
	private int httpRequestTimeout;
	private int antonPort;
}
