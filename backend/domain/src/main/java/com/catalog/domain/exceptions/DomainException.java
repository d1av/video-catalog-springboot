package com.catalog.domain.exceptions;

import com.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {
    private final List<Error> errors;

    private DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public static DomainException with(final Error anErrors) {
        return new DomainException("", List.of(anErrors));
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Error firstError() {
        return errors.get(0);
    }
}
