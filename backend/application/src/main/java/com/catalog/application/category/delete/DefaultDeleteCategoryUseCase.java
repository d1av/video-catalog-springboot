package com.catalog.application.category.delete;

import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }
    @Override
    public void execute(final String onIn) {
        this.categoryGateway.deleteById(CategoryID.from(onIn));
    }
}
