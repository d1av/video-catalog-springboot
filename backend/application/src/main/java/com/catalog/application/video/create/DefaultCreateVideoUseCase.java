package com.catalog.application.video.create;

import com.catalog.domain.Identifier;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.validation.Error;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.handler.Notification;
import com.catalog.domain.video.Rating;
import com.catalog.domain.video.Video;
import com.catalog.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;

    public DefaultCreateVideoUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public CreateVideoOutput execute(CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
        final var aLaunchYear = Year.of(aCommand.launchedAt());
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateCastMembers(members));

        final var aVideo = Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members
        );

        aVideo.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not create Aggregate video", notification);
        }

        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
        return this.videoGateway.create(aVideo);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <Z extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<Z> ids,
            final Function<Iterable<Z>, List<Z>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) return notification;

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(
                    new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }
        return notification;
    }

    private <T> Set<T> toIdentifier(Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    private Supplier<DomainException> invalidRating(final String rating) {
        return () -> DomainException.with(new Error("Rating not found %s".formatted(rating)));
    }
}
