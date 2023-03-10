package com.catalog.application.genre.create;

import com.catalog.IntegrationTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.infrastructure.category.CategoryMySQLGateway;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateGenreUseCaseIT {
    @Autowired
    private DefaultCreateGenreUseCase useCase;
    @Autowired
    private GenreGateway genreGateway;
    @SpyBean
    private CategoryGateway categoryGateway;
    @Autowired
    private GenreRepository genreRepository;
    @SpyBean
    private CategoryMySQLGateway categoryMySQLGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var filmes =
                categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId());

        final var aCommand = CreateGenreCommand
                .with(expectedName, expectIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());


        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());


    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectedName = "Ação";
        final var expectIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand
                .with(expectedName, expectIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectedName = "Ação";
        final var expectIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand
                .with(expectedName, expectIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        //given
        final var expectedName = "  ";
        final var expectIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand
                .with(expectedName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Mockito.verify(categoryGateway, times(0)).existsByIds(expectedCategories);
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        //given
        final String expectedName = null;
        final var expectIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand
                .with(expectedName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


        Mockito.verify(categoryGateway, times(0)).existsByIds(expectedCategories);
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        //given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));
        final var filmes = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var expectName = "  ";
        final var expectIsActive = true;
        final var expectedCategories = List.of(filmes, series.getId(), documentarios);

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);
    }

    private List<String> asString(final List<CategoryID> aCategories) {
        return aCategories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
