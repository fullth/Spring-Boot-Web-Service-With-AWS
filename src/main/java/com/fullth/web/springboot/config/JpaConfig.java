package com.fullth.web.springboot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.fullth.web.springboot.domain")
@EnableJpaRepositories(basePackages = "com.fullth.web.springboot.domain")
@EnableJpaAuditing
@Configuration
public class JpaConfig {}


