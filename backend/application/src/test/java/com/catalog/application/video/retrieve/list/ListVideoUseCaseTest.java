package com.catalog.application.video.retrieve.list;

import com.catalog.application.Fixture;
import com.catalog.application.UseCaseTest;
import com.catalog.application.genre.retrieve.list.GenreListOutput;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.domain.video.Video;
import com.catalog.domain.video.VideoGateway;
import com.catalog.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;
    @Mock
    private CategoryGateway categoryGateway;
    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenValidQuery_whenCallsListVideos_shouldReturnVideos() {
        // given
        final var videos = List.of(
                Fixture.systemDesign(),
                Fixture.systemDesign()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();
        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, expectedTotal, videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        when(categoryGateway.findById(any()))
                .thenReturn(Optional.of(Category.newCategory("Test", "desc", true)));

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms,
                        expectedSort, expectedDirection);
        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
//        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
        // then
    }

    @Test
    public void givenAValidQuery_whenCallsListVideoAndResultIsEmpty_shouldReturnVideos() {
        // given
        final var videos = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<VideoListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, expectedTotal, videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms,
                        expectedSort, expectedDirection);
        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));

    }

    @Test
    public void givenAValidQuery_whenCallsListVideoAndGatewayThrowsRandomError_shouldReturnException() {
        // given
        final var videos = List.of(
                Fixture.systemDesign(),
                Fixture.systemDesign()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException("Gateway error"));

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms,
                        expectedSort, expectedDirection);
        // when
        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));

    }
}
