package com.catalog.infrastructure.category.presenters;

import com.catalog.application.category.retrieve.get.CategoryOutput;
import com.catalog.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {
    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
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
