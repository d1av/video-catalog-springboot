package com.catalog.infrastructure.genre.presenters;

import com.catalog.application.category.retrieve.get.CategoryOutput;
import com.catalog.application.category.retrieve.list.CategoryListOutput;
import com.catalog.application.genre.retrieve.get.GenreOutput;
import com.catalog.application.genre.retrieve.list.GenreListOutput;
import com.catalog.infrastructure.category.models.CategoryListResponse;
import com.catalog.infrastructure.category.models.CategoryResponse;
import com.catalog.infrastructure.genre.models.GenreListResponse;
import com.catalog.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {
    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
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
