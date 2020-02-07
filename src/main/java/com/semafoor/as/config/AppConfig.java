package com.semafoor.as.config;

import com.semafoor.as.DbTestData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Base configuration class, used to group all other configuration classes. Beans that do not logically  belong to any
 * more specific configuration class can be defined here.
 */

@Configuration
@ComponentScan(basePackages = {"com.semafoor.as.config"})
public class AppConfig {

    /**
     * populate db with some test data.
     */

    @Bean
    @DependsOn("liquibase")
    public DbTestData testData(PasswordEncoder passwordEncoder, PlatformTransactionManager transactionManager) {
        return new DbTestData(passwordEncoder, transactionManager);
    }
}
