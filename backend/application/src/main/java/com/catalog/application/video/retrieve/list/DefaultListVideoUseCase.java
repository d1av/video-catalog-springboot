package com.catalog.application.video.retrieve.list;

import com.catalog.domain.Identifier;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.video.VideoGateway;
import com.catalog.domain.video.VideoSearchQuery;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultListVideoUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;

    public DefaultListVideoUseCase(final VideoGateway videoGateway,
                                   final CategoryGateway categoryGateway,
                                   final GenreGateway genreGateway,
                                   final CastMemberGateway castMemberGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
        final var search = this.videoGateway.findAll(aQuery);

        final var names = search.items().stream()
                .map(aVideo ->
                        VideoListOutput.newListWithNames(
                                aVideo.getId().getValue(),
                                aVideo.getTitle(),
                                aVideo.getDescription(),
                                aVideo.getCreatedAt(),
                                aVideo.getUpdatedAt(),
                                getNameFromSet(aVideo.getCategories(), "categoryGateway"),
                                getNameFromSet(aVideo.getGenres(), "genreGateway"),
                                getNameFromSet(aVideo.getCastMembers(), "castMemberGateway")

                        )
                ).toList();

        return new Pagination<>(
                search.currentPage(),
                search.perPage(),
                search.total(),
                names
        );
    }


    private Set<String> getNameFromSet(Set<? extends Identifier> list, String mapper) {
        Set<String> names = new HashSet<>();
        if (list.isEmpty()) return names;

        if (mapper.equals("categoryGateway")) {
            names = list.stream()
                    .map(this::getCategoryName)
                    .collect(Collectors.toSet());
        } else if (mapper.equals("genreGateway")) {
            names = list.stream()
                    .map(this::getGenreName)
                    .collect(Collectors.toSet());
        } else {
            names = list.stream()
                    .map(this::getCastMembersName)
                    .collect(Collectors.toSet());
        }
        return names;
    }

    private String getCategoryName(Identifier anId) {
        return categoryGateway.findById((CategoryID) anId).isEmpty() ?
                "" : categoryGateway.findById((CategoryID) anId).get().getName();
    }

    private String getGenreName(Identifier anId) {
        return genreGateway.findById((GenreID) anId).isEmpty() ?
                "" : genreGateway.findById((GenreID) anId).get().getName();

    }
    private String getCastMembersName(Identifier anId) {
        return castMemberGateway.findById((CastMemberID) anId).isEmpty() ?
                "" : castMemberGateway.findById((CastMemberID) anId).get().getName();
    }
}
