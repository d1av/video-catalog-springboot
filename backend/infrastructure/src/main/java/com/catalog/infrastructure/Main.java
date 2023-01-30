package com.catalog.infrastructure;

import com.catalog.domain.genre.Genre;
import com.catalog.infrastructure.configuration.WebServerConfig;
import com.catalog.infrastructure.genre.GenreMySQLGateway;
import com.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test-integration");
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

}