package com.example.galendata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * holds all config files for the application. Config files for specific classes are imported into
 * the main config class.
 */
@Configuration
@Import({CrawlerConfig.class})
public class MainConfig {
}
