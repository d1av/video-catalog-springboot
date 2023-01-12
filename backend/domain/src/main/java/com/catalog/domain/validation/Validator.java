package com.catalog.domain.validation;

public abstract class Validator {
    private final ValidationHandler handler;

    public Validator(final ValidationHandler handler) {
        this.handler = handler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}
