package com.monoplus.mcd.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.monoplus.mcd")
@ImportResource({"file:config/applicationContext.xml"})
public class DbConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfig.class);
}
