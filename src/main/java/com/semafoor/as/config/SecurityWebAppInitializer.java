package com.semafoor.as.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Providing an empty class extending {@link AbstractAnnotationConfigDispatcherServletInitializer} instructs spring
 * to enable {@link DelegatingFilterProxy}. This ensures that the spring security filter chain will be used before
 * any other registered filters
 */

public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
}
