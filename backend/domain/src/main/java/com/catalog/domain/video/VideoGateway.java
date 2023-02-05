package com.catalog.domain.video;

import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anId);

    Video update(Video aVideo);

    Pagination<Video> findAll(VideoSearchQuery aQuery);
}
