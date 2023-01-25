package com.catalog.infrastructure;

import com.catalog.application.category.create.CreateCategoryUseCase;
import com.catalog.application.category.delete.DeleteCategoryUseCase;
import com.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.catalog.application.category.update.UpdateCategoryUseCase;
import com.catalog.domain.category.Category;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import com.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test");
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    ApplicationRunner runner(
            @Autowired CreateCategoryUseCase createCategoryUseCase,
            @Autowired UpdateCategoryUseCase updateCategoryUseCase,
            @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
            @Autowired ListCategoriesUseCase listCategoriesUseCase,
            @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase
    ) {
        return args -> {

        };
    }
}