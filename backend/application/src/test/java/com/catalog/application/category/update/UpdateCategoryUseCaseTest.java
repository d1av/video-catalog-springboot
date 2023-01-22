package com.catalog.application.category.update;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

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

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .findById(ArgumentMatchers.eq(expectedId));

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
}
