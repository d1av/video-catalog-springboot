package com.catalog.application.video.retrieve.get;

import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.domain.video.Video;
import com.catalog.domain.video.VideoGateway;
import com.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {
    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(String anId) {
        final var aVideoId = VideoID.from(anId);

        return this.videoGateway.findById(VideoID.from(anId))
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
