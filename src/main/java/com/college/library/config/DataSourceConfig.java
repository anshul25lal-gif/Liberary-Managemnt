package com.college.library.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Hardcodes H2 as the datasource so Railway's injected MySQL env vars
 * (MYSQLHOST, MYSQLPORT, etc.) cannot override Spring Boot's auto-config.
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
