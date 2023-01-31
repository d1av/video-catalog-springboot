package com.catalog.infrastructure.configuration.usecases;

import com.catalog.application.genre.create.CreateGenreUseCase;
import com.catalog.application.genre.create.DefaultCreateGenreUseCase;
import com.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import com.catalog.application.genre.delete.DeleteGenreUseCase;
import com.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.catalog.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import com.catalog.application.genre.update.UpdateGenreUseCase;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {
    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(final GenreGateway genreGateway,
                              final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway,genreGateway);
    }
}
