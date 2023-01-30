package com.catalog.application.genre.retrieve.list;

import com.catalog.application.UseCase;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase
        extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

}
