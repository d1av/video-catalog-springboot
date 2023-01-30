package com.catalog.application.category.update;

import com.catalog.application.UseCaseTest;
import com.catalog.application.category.create.CreateCategoryCommand;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade invalida (nome)
    // 3. Teste atualizando uma categoria inativa
    // 4. Teste simulando um erro generico de gateway
    // 5. Atualizar categoria passando ID invalido

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .findById(eq(expectedId));

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .update(ArgumentMatchers.argThat(
                        aUpdatedCategory ->
                        {
                            return Objects.equals(expectedName, aUpdatedCategory.getName())
                                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
                        }
                ));

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedId = aCategory.getId();
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

//        final var aCommand =
//                UpdateCategoryCommand
//                        .with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
//        final var notification = useCase.execute(aCommand).getLeft();
//
//
//        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
//                .thenReturn(Optional.of(Category.with(aCategory)));
//
//
//         Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
//          Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
//          Mockito.verify(categoryGateway,
//                Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactivateCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory =
                Category.newCategory(
                        "Film",
                        null,
                        true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();


        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .findById(eq(expectedId));

//        Mockito.verify(categoryGateway,
//                        Mockito.times(0))
//                .update(ArgumentMatchers.argThat(
//                        aUpdatedCategory ->
//                        {
//                            return Objects.equals(expectedName, aUpdatedCategory.getName())
//                                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
//                                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
//                                    && Objects.equals(expectedId, aUpdatedCategory.getId())
//                                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
//                                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
//                                    && Objects.nonNull(aUpdatedCategory.getDeletedAt());
//                        }
//                ));

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory =
                Category.newCategory(
                        "Film",
                        null,
                        true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";


        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .update(ArgumentMatchers.argThat(
                        aUpdatedCategory ->
                        {
                            return Objects.equals(expectedName, aUpdatedCategory.getName())
                                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
                        }
                ));

    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var aCategory =
                Category.newCategory(
                        "Film",
                        null,
                        true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;


        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .findById(eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway,
                        Mockito.times(0))
                .update(Mockito.any());

    }

}
