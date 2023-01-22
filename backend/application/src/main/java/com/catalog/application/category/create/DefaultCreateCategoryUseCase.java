package com.catalog.application.category.create;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.validation.handler.Notification;
import com.catalog.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var notification = Notification.create();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        aCategory.validate(new ThrowsValidationHandler());

        if(notification.hasError()){

        }

        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }
}
