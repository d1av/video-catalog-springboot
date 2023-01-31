package com.catalog.infrastructure.genre;

import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class GenreMySQLGateway implements GenreGateway {
    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(GenreID anId) {
        final var aGenreId = anId.getValue();
        if (this.genreRepository.findById(aGenreId).isPresent()) {
            this.genreRepository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return Optional.empty();
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
