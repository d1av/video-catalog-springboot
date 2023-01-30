package com.catalog.application.genre.retrieve.get;

import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {
    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(String onIn) {
        final var aGenreId = GenreID.from(onIn);
        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }
}
