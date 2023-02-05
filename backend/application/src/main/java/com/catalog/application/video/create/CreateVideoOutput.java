package com.catalog.application.video.create;

import com.catalog.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
