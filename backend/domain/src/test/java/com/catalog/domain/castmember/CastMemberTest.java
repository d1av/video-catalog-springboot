package com.catalog.domain.castmember;

import com.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {
    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = "     ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = "Gostaria de enfatizar que o consenso sobre a necessidade de qualificação" +
                " auxilia a preparação e a composição das posturas dos órgãos dirigentes com relação às suas " +
                "atribuições. Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido" +
                " de aprovar a manutenção das novas proposições.";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAInvalidType_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = "John Cena";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }
}
