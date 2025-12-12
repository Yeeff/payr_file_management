package com.horizonx.file_services.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String instance = System.getenv("DB_INSTANCE");
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "scheduler_db";
        String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
        String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "password";

        if (instance != null && !instance.isEmpty()) {
            // Cloud SQL
            String url = "jdbc:mysql://google/" + dbName + "?cloudSqlInstance=" + instance + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory";
            return DataSourceBuilder.create()
                    .url(url)
                    .username(dbUser)
                    .password(dbPassword)
                    .build();
        } else {
            // Local
            String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?allowPublicKeyRetrieval=true&useSSL=false";
            return DataSourceBuilder.create()
                    .url(url)
                    .username(dbUser)
                    .password(dbPassword)
                    .build();
        }
    }
}