package com.catalog.application.category.update;

import com.catalog.IntegrationTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;
    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());
        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt().getEpochSecond(),actualCategory.getCreatedAt().getEpochSecond());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }


    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory =
                Category.newCategory(null, null, true);

        final String expectedName = null;
        final var expectedId = aCategory.getId();
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID " + expectedId.getValue() + " was not found";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand
                        .with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var notification = Assertions
                .assertThrows(DomainException.class, () -> useCase.execute(aCommand).getLeft());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> save(aCategory));


        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway,
                Mockito.times(0)).create(any());
    }

    @Test
    public void givenAValidCommandWithInactivateCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

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

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getCreatedAt().getEpochSecond(), actualCategory.getCreatedAt().getEpochSecond());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);


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

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt().getEpochSecond(), actualCategory.getCreatedAt().getEpochSecond());
        Assertions.assertEquals(aCategory.getUpdatedAt().getEpochSecond(), actualCategory.getUpdatedAt().getEpochSecond());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

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

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    private void save(Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
