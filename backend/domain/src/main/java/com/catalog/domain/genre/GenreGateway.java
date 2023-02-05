package com.catalog.domain.genre;

import com.catalog.domain.category.CategoryID;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;
import net.datafaker.providers.base.Cat;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre aGenre);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);

    List<GenreID> existsByIds(Iterable<CategoryID> ids);

}
