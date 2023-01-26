package com.catalog.application.category.create;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final CategoryID anId) {
        return new CreateCategoryOutput(anId.getValue());
    }
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
