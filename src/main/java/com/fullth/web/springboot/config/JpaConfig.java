package com.fullth.web.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.fullth.web.springboot.domain")
@EnableJpaAuditing
public class JpaConfig {}
