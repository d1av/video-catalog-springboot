package com.catalog.application.genre.update;

import com.catalog.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
