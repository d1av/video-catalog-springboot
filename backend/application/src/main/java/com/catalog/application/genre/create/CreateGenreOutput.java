package com.catalog.application.genre.create;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreID;

public record CreateGenreOutput(
        String id
) {
    public static CreateGenreOutput from(final GenreID anId) {
        return new CreateGenreOutput(anId.getValue());
    }

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue() != null ? aGenre.getId().getValue() : "123");
    }
}
