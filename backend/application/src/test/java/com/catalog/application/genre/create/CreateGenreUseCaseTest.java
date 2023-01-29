package com.catalog.application.genre.create;

import com.catalog.application.UseCaseTest;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.domain.genre.GenreGateway;
import com.catalog.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultCreateGenreUseCase useCase;
    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectName = "Ação";
        final var expectIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre ->
                        Objects.equals(expectName, aGenre.getName())
                                && Objects.equals(expectIsActive, aGenre.isActive())
                                && Objects.equals(expectedCategories, aGenre.getCategories())
                                && Objects.nonNull(aGenre.getCreatedAt())
                                && Objects.nonNull(aGenre.getUpdatedAt())
                                && Objects.isNull(aGenre.getDeletedAt())
                ));


    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectName = "Ação";
        final var expectIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        when(categoryGateway.existsById(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).existsById(expectedCategories);

        Mockito.verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre ->
                        Objects.equals(expectName, aGenre.getName())
                                && Objects.equals(expectIsActive, aGenre.isActive())
                                && Objects.equals(expectedCategories, aGenre.getCategories())
                                && Objects.nonNull(aGenre.getCreatedAt())
                                && Objects.nonNull(aGenre.getUpdatedAt())
                                && Objects.isNull(aGenre.getDeletedAt())
                ));

    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateCategory_shouldReturnDomainException() {
        //given
        final var expectName = "   ";
        final var expectIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

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

        Mockito.verify(categoryGateway, times(0)).existsById(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateCategory_shouldReturnDomainException() {
        //given
        final String expectName = null;
        final var expectIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
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

        Mockito.verify(categoryGateway, times(0)).existsById(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        //given
        final var filmes = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");
        final var expectName = "Ação";
        final var expectIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        when(categoryGateway.existsById(any()))
                .thenReturn(List.of(series));

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


        Mockito.verify(categoryGateway, times(1)).existsById(expectedCategories);
        Mockito.verify(genreGateway, times(0)).create(any());


    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        //given
        final var filmes = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");
        final var expectName = "  ";
        final var expectIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 1;

        when(categoryGateway.existsById(any()))
                .thenReturn(List.of(series));

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        // Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.firstError().message());
        // Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


        Mockito.verify(categoryGateway, times(1)).existsById(expectedCategories);
        Mockito.verify(genreGateway, times(0)).create(any());


    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var filmes = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");
        final var expectName = " ";
        final var expectIsActive = false;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 1;

        when(categoryGateway.existsById(any()))
                .thenReturn(List.of(series));

        final var aCommand = CreateGenreCommand
                .with(expectName, expectIsActive, asString(expectedCategories));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        // Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.firstError().message());
        // Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


        Mockito.verify(categoryGateway, times(1)).existsById(expectedCategories);
        Mockito.verify(genreGateway, times(0)).create(any());


    }

    private List<String> asString(final List<CategoryID> aCategories) {
        return aCategories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
