package com.catalog.application.category.retrieve.list;

import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(final SearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
