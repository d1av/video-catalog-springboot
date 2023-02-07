package com.catalog.application.video.delete;

import com.catalog.domain.video.VideoGateway;
import com.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(String onIn) {
        this.videoGateway.deleteById(VideoID.from(onIn));
    }
}
