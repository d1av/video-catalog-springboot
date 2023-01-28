package com.catalog.infrastructure.api.controllers;

import com.catalog.application.category.create.CreateCategoryCommand;
import com.catalog.application.category.create.CreateCategoryOutput;
import com.catalog.application.category.create.CreateCategoryUseCase;
import com.catalog.application.category.delete.DeleteCategoryUseCase;
import com.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.catalog.application.category.update.UpdateCategoryCommand;
import com.catalog.application.category.update.UpdateCategoryOutput;
import com.catalog.application.category.update.UpdateCategoryUseCase;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.validation.handler.Notification;
import com.catalog.infrastructure.api.CategoryAPI;
import com.catalog.infrastructure.category.models.CategoryApiOutput;
import com.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.catalog.infrastructure.category.models.UpdateCategoryApiInput;
import com.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(
            String search,
            int page,
            int perPage,
            String sort,
            String direction) {
        return listCategoriesUseCase.execute(new CategorySearchQuery(
                page,perPage,search,sort,direction
        ));
    }

    @Override
    public CategoryApiOutput getById(String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
