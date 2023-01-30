package com.catalog.application.genre.retrieve.list;

import com.catalog.application.category.retrieve.list.CategoryListOutput;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase {

    public final GenreGateway genreGateway;
    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = genreGateway;
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery).map(GenreListOutput::from);
    }
}
