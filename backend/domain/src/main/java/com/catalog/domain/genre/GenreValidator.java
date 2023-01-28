package com.catalog.domain.genre;

import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class GenreValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 1;
    private final Genre genre;

    public GenreValidator(final Genre aGenre, final ValidationHandler handler) {
        super(handler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        } else {
            if (name.trim().isBlank()) {
                this.validationHandler().append(new Error("'name' should not be empty"));
            }
            final int length = name.length();
            if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
                this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
            }
        }
    }

}
