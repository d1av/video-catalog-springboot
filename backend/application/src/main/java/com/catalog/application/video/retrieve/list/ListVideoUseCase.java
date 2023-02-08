package com.catalog.application.video.retrieve.list;

import com.catalog.application.UseCase;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.video.VideoSearchQuery;

public abstract class ListVideoUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
