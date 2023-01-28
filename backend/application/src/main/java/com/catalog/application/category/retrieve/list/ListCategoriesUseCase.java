package com.catalog.application.category.retrieve.list;

import com.catalog.application.UseCase;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {

}
