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
        final String expectedName = "  ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 2;
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
        final String expectedName = "Gostaria de enfatizar que o consenso sobre a necessidade de qualifica????o" +
                " auxilia a prepara????o e a composi????o das posturas dos ??rg??os dirigentes com rela????o ??s suas " +
                "atribui????es. Do mesmo modo, a estrutura atual da organiza????o apresenta tend??ncias no sentido" +
                " de aprovar a manuten????o das novas proposi????es.";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

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

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() throws InterruptedException {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();
        Thread.sleep(100);
        actualMember.update(expectedName, expectedType);

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        final String expectedName = "    ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 2;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
        final String expectedName = "Asa Akira";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newMember("asa", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdatedWithLengthMoreThan255_shouldReceiveANotification() {
        final String expectedName = "Gostaria de enfatizar que o consenso sobre a necessidade de qualifica????o" +
                " auxilia a prepara????o e a composi????o das posturas dos ??rg??os dirigentes com rela????o ??s suas " +
                "atribui????es. Do mesmo modo, a estrutura atual da organiza????o apresenta tend??ncias no sentido" +
                " de aprovar a manuten????o das novas proposi????es.";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newMember("The Rock", CastMemberType.DIRECTOR);

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
    }

}
