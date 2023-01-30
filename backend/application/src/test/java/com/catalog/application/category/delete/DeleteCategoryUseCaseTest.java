package com.catalog.application.category.delete;

import com.catalog.application.UseCaseTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

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

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        Mockito.doNothing().when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing().when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .deleteById(expectedId);
    }

    @Test
    public void givenValidId_whenGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalArgumentException()).when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,
                        Mockito.times(1))
                .deleteById(expectedId);

    }

}
