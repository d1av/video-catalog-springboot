package com.catalog.domain.category;


import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        if (this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
