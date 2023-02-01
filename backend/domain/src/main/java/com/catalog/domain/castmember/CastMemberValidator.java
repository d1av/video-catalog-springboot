package com.catalog.domain.castmember;

import com.catalog.domain.category.Category;
import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class CastMemberValidator extends Validator {
    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;
    private final CastMember castMember;

    public CastMemberValidator(final CastMember aMember,
                               final ValidationHandler handler) {
        super(handler);
        this.castMember = aMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        } else {
            if (name.trim().isBlank()) {
                this.validationHandler().append(new Error("'name' should not be empty"));
            }
            final int length = name.trim().length();
            if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
                this.validationHandler().append(new Error("'name' must be between %s and %s characters".formatted(NAME_MIN_LENGTH, NAME_MAX_LENGTH)));
            }
        }
    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();
        if (type == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }
}
