package com.catalog.application.category.retrieve.list;

import com.catalog.application.UseCase;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {

}
