package com.catalog.application.delete;

import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {
    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String onIn) {
        this.genreGateway.deleteById(GenreID.from(onIn));
    }
}
