package com.catalog.infrastructure.configuration.usecases;

import com.catalog.application.category.create.CreateCategoryUseCase;
import com.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.catalog.application.category.delete.DeleteCategoryUseCase;
import com.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.catalog.application.category.update.UpdateCategoryUseCase;
import com.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }


}
