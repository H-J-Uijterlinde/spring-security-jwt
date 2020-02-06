package com.semafoor.as.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.semafoor.as.web"})
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
}
