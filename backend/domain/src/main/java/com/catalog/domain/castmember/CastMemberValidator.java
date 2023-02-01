package com.catalog.domain.castmember;

import com.catalog.domain.category.Category;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class CastMemberValidator extends Validator {
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final CastMember castMember;

    public CastMemberValidator(final CastMember aMember,
                               final ValidationHandler handler) {
        super(handler);
        this.castMember = aMember;
    }

    @Override
    public void validate() {

    }
}
