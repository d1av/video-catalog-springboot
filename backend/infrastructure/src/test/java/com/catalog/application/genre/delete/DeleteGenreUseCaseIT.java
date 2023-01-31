package com.catalog.application.genre.delete;

import com.catalog.IntegrationTest;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.genre.GenreID;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteGenreUseCaseIT {
    @Autowired
    private DefaultDeleteGenreUseCase useCase;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());
        // when
        Assertions.assertDoesNotThrow(() ->
                useCase.execute(expectedId.getValue()));
        // should
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(1, genreRepository.count());

        // when
        Assertions.assertDoesNotThrow(() ->
                useCase.execute(expectedId.getValue()));
        // should
        Assertions.assertEquals(1, genreRepository.count());
    }

}
