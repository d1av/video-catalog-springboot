package com.catalog.domain.video;

import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;

    private final Video video;

    protected VideoValidator(final Video aVideo, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = aVideo;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkRatingConstraints();
        checkLauchedAtConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
        } else {
            if (title.trim().isBlank()) {
                this.validationHandler().append(new Error("'title' should not be empty"));
            }
            final int length = title.length();
            if (length > TITLE_MAX_LENGTH) {
                this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
            }
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
        } else {
            if (description.trim().isBlank()) {
                this.validationHandler().append(new Error("'description' should not be empty"));
            }
            final int length = description.length();
            if (length > DESCRIPTION_MAX_LENGTH) {
                this.validationHandler().append(
                        new Error("'description' must be between 1 and %s characters"
                                .formatted(DESCRIPTION_MAX_LENGTH)));
            }
        }
    }

    private void checkLauchedAtConstraints() {
        final var launchedAt = this.video.getLaunchedAt();
        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraints() {
        final var rating = this.video.getRating();
        if (rating == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
