package com.catalog.infrastructure.video;

import com.catalog.Fixture;
import com.catalog.IntegrationTest;
import com.catalog.domain.Identifier;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.domain.video.*;
import com.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.catalog.infrastructure.video.persistence.VideoRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@IntegrationTest
public class DefaultVideoGatewayTest {
    @Autowired
    private DefaultVideoGateway videoGateway;
    @Autowired
    private CastMemberGateway castMemberGateway;
    @Autowired
    private CategoryGateway categoryGateway;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private VideoRepository videoRepository;

    private CastMember wesley;
    private CastMember mia;
    private Category aulas;
    private Category lives;
    private Genre tech;
    private Genre business;

    @BeforeEach
    public void setUp() {
        wesley = castMemberGateway.create(Fixture.CastMembers.wesley());
        mia = castMemberGateway.create(Fixture.CastMembers.mia());

        aulas = categoryGateway.create(Fixture.Categories.aulas());
        lives = categoryGateway.create(Fixture.Categories.lives());

        tech = genreGateway.create(Fixture.Genres.tech());
        business = genreGateway.create(Fixture.Genres.business());
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedCastMembers = Set.of(mia.getId());

        final AudioVideoMedia expectedVideo =
                AudioVideoMedia.with(Fixture.randomId(), "Video", "/media/video");

        final AudioVideoMedia expectedTrailer =
                AudioVideoMedia.with(Fixture.randomId(), "Trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with(Fixture.randomId(), "Banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with(Fixture.randomId(), "Thumbnail", "/media/Thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with(Fixture.randomId(), "Thumbnail_Half", "/media/thumb_half");

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMembers
                )
                .setVideo(expectedVideo)
                .setBanner(expectedBanner)
                .setTrailer(expectedTrailer)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);


        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());

        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(actualVideo.getCastMembers()));
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById((actualVideo.getId().getValue())).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLauched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(persistedVideo.getCastMembersID()));
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    public void givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final Set<CategoryID> expectedCategories = Set.of();
        final Set<GenreID> expectedGenres = Set.of();
        final Set<CastMemberID> expectedCastMembers = Set.of();

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());

        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(actualVideo.getCastMembers()));
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById((actualVideo.getId().getValue())).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLauched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertNull(persistedVideo.getCategoriesID());
        Assertions.assertNull(persistedVideo.getGenresID());
        Assertions.assertNull(persistedVideo.getCastMembersID());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersistIt() throws InterruptedException {
        final var aVideo = Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        );

        videoGateway.create(aVideo);
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedCastMembers = Set.of(mia.getId());

        final AudioVideoMedia expectedVideo =
                AudioVideoMedia.with(Fixture.randomId(), "Video", "/media/video");

        final AudioVideoMedia expectedTrailer =
                AudioVideoMedia.with(Fixture.randomId(), "Trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with(Fixture.randomId(), "Banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with(Fixture.randomId(), "Thumbnail", "/media/Thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with(Fixture.randomId(), "Thumbnail_Half", "/media/thumb_half");

        final var updatedVideo = Video.with(aVideo).update(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMembers
                )
                .setVideo(expectedVideo)
                .setBanner(expectedBanner)
                .setTrailer(expectedTrailer)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);


        // when
        Thread.sleep(100);
        final var actualVideo = videoGateway.update(updatedVideo);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());

        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(actualVideo.getCastMembers()));
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertTrue(actualVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

        final var persistedVideo = videoRepository.findById((actualVideo.getId().getValue())).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLauched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(persistedVideo.getCastMembersID()));
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    public void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aVideo = Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        );

        videoRepository.saveAndFlush(VideoJpaEntity.from(aVideo));

        Assertions.assertEquals(1, videoRepository.count());

        final var anId = aVideo.getId();

        // when
        videoGateway.deleteById(anId);

        // then
        Assertions.assertEquals(0, videoRepository.count());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aVideo = Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        );

        videoRepository.saveAndFlush(VideoJpaEntity.from(aVideo));

        Assertions.assertEquals(1, videoRepository.count());

        final var anInvalidId = VideoID.unique();

        // when
        videoGateway.deleteById(anInvalidId);

        // then
        Assertions.assertEquals(1, videoRepository.count());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
        // given
        final var mia = castMemberGateway.create(Fixture.CastMembers.mia());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedCastMembers = Set.of(mia.getId());

        final AudioVideoMedia expectedVideo =
                AudioVideoMedia.with(Fixture.randomId(), "Video", "/media/video");

        final AudioVideoMedia expectedTrailer =
                AudioVideoMedia.with(Fixture.randomId(), "Trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with(Fixture.randomId(), "Banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with(Fixture.randomId(), "Thumbnail", "/media/Thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with(Fixture.randomId(), "Thumbnail_Half", "/media/thumb_half");

        // when
        final var aVideo = videoGateway.create(Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMembers
                )
                .setVideo(expectedVideo)
                .setBanner(expectedBanner)
                .setTrailer(expectedTrailer)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf));

        final var actualVideo = videoGateway.findById(aVideo.getId()).get();

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());

        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        Assertions.assertEquals(Math.round(expectedDuration), Math.round(actualVideo.getDuration()));
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(actualVideo.getCastMembers()));
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById((actualVideo.getId().getValue())).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLauched()));
        Assertions.assertEquals(Math.round(expectedDuration), Math.round(persistedVideo.getDuration()));
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(getIdString(expectedCastMembers), getIdString(persistedVideo.getCastMembersID()));
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    public void givenAInvalidVideoId_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        // when
        final var aVideo = videoGateway.create(Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMembers
                )
        );

        final var invalidId = VideoID.unique();

        final var actualVideo = videoGateway.findById(invalidId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    /*
     * PAGINATION TESTS
     */

    @Test
    @Transactional
    public void givenVideos_whenCallFindAll_shouldReturnAllList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenEmptyVideo_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    @Transactional
    public void givenAValidCategory_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                new HashSet<>(Set.of(aulas.getId())),
                Set.of()
        );

        // when
//        final var actualPage = videoGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualPage.currentPage());
//        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
//        Assertions.assertEquals(expectedTotal, actualPage.total());
//        Assertions.assertEquals(expectedTotal, actualPage.items().size());
//
//        Assertions.assertEquals("21.1 Implementa????o dos testes integrados do findAll", actualPage.items().get(0).title());
//        Assertions.assertEquals("Aula de empreendendorismo", actualPage.items().get(1).title());
    }

    @Test
    @Transactional
    public void givenAValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(wesley.getId()),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

        Assertions.assertEquals("Aula de empreendendorismo", actualPage.items().get(0).title());
        Assertions.assertEquals("System Design", actualPage.items().get(1).title());
    }

    @Test
    public void givenAValidGenre_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of(business.getId())
        );

        // when
//        final var actualPage = videoGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualPage.currentPage());
//        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
//        Assertions.assertEquals(expectedTotal, actualPage.total());
//        Assertions.assertEquals(expectedTotal, actualPage.items().size());
//
//        Assertions.assertEquals("Aula de empreendendorismo", actualPage.items().get(0).title());
    }

    @Test
    @Transactional
    public void givenAllParameters_whenCallsFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "empreendendorismo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                new HashSet<>(Set.of(wesley.getId())),
                new HashSet<>(Set.of(aulas.getId())),
                new HashSet<>(Set.of(business.getId()))
        );

        // when
//        final var actualPage = videoGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualPage.currentPage());
//        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
//        Assertions.assertEquals(expectedTotal, actualPage.total());
//        Assertions.assertEquals(expectedTotal, actualPage.items().size());
//
//        Assertions.assertEquals("Aula de empreendendorismo", actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "a??,0,10,1,1,A????o",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Com??dia rom??ntica",
            "cien,0,10,1,1,Fic????o cient??fica",
            "terr,0,10,1,1,Terror",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered() {

    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,21.1 Implementa????o dos testes integrados do findAll",
            "title,desc,0,10,4,4,System Design",
            "createdAt,asc,0,10,4,4,System Design",
            "createdAt,desc,0,10,4,4,Aula de empreendendorismo",
    })
    public void givenAValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
// given
        mockVideos();
        final var expectedTerms = "";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,21.1 Implementa????o dos testes integrados do findAll;Aula de empreendendorismo",
            "1,2,2,4,N??o cometa esses erros ao trabalhar com Microsservi??os;System Design",
    })
    public void givenAValidPaging_whenCallFindAll_shouldReturnPage(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos
    ) {
        // given
        mockVideos();
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedTitle : expectedVideos.split(";")) {
            final var actualTitle = actualPage.items().get(index).title();
            Assertions.assertEquals(expectedTitle, actualTitle);
            index++;
        }
    }

    @ParameterizedTest
    @CsvSource({
            "system,0,10,1,1,System Design",
            "microsservi??os,0,10,1,1,N??o cometa esses erros ao trabalhar com Microsservi??os",
            "empreendendorismo,0,10,1,1,Aula de empreendendorismo",
            "21,0,10,1,1,21.1 Implementa????o dos testes integrados do findAll"
    })
    public void givenAValidTerm_whenCallFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    private void mockVideos() {
        videoGateway.create(Video.newVideo(
                "System Design",
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(lives.getId()),
                Set.of(tech.getId()),
                Set.of(wesley.getId(), mia.getId())
        ));

        videoGateway.create(Video.newVideo(
                "N??o cometa esses erros ao trabalhar com Microsservi??os",
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        videoGateway.create(Video.newVideo(
                "21.1 Implementa????o dos testes integrados do findAll",
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(tech.getId()),
                Set.of(mia.getId())
        ));

        videoGateway.create(Video.newVideo(
                "Aula de empreendendorismo",
                Fixture.Videos.description(),
                Fixture.year(),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(business.getId()),
                Set.of(wesley.getId())
        ));
    }

    @NotNull
    private static Set<String> getIdString(Set<? extends Identifier> list) {
        return list.stream()
                .map(Identifier::getValue)
                .collect(Collectors.toSet());
    }

}
