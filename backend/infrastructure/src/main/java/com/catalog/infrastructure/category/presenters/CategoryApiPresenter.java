package com.catalog.infrastructure.category.presenters;

import com.catalog.application.category.retrieve.get.CategoryOutput;
import com.catalog.application.category.retrieve.list.CategoryListOutput;
import com.catalog.infrastructure.category.models.CategoryResponse;
import com.catalog.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

    // Outro jeito é criando class final com construtor privado, ou o jeito abaixo

//    Function<CategoryOutput,CategoryApiOutput> present =
//            output -> new CategoryApiOutput(
//                    output.id().getValue(),
//                    output.name(),
//                    output.description(),
//                    output.isActive(),
//                    output.createdAt(),
//                    output.updatedAt(),
//                    output.deletedAt()
//            );
}
