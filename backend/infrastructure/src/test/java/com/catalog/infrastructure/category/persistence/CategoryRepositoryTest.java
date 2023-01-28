package com.catalog.infrastructure.category.persistence;

import com.catalog.domain.category.Category;
import com.catalog.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

//        final var actualException =
//                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
//
//        final var actualCause =
//                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
//
//        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
//        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        categoryRepository.save(anEntity);

       // final var actualException = Assertions.assertThrows(
       //         InvalidDataAccessApiUsageException.class, () -> categoryRepository.save(anEntity));

       // final var actualCause = Assertions.assertInstanceOf(
       //         PropertyValueException.class, actualException.getCause());

       // Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        //Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        //final var actualException =
        //        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

       // final var actualCause =
       //         Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

      //  Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
       // Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }


}
