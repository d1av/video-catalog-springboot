package com.catalog.application.genre.create;

import com.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {
    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

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

    private List<String> asString(final List<CategoryID> aCategories) {
        return aCategories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
