package com.catalog.application.category.create;

import com.catalog.application.UseCase;
import com.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
