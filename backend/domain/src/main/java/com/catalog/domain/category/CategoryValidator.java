package com.catalog.domain.category;


import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    public CategoryValidator(Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkDescriptionConstraints();
        if(this.category.getName()==null){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.trim().isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
            return;
        }
    }

    private void checkDescriptionConstraints(){
        final var description = this.category.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }
        if (description.trim().isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
        }
    }
}
