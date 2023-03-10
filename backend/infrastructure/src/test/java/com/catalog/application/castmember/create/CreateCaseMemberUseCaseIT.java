package com.catalog.application.castmember.create;

import com.catalog.Fixture;
import com.catalog.IntegrationTest;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.catalog.Fixture.CastMembers.type;
import static com.catalog.Fixture.name;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCaseMemberUseCaseIT {
    @Autowired
    private CreateCastMemberUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;
    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCreateCastMember_shouldReturnIt() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aCommand = CreateCastMemberCommand.with(
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);

        final var savedMember = this.castMemberRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, savedMember.getName());
        Assertions.assertEquals(expectedType, savedMember.getType());
        Assertions.assertNotNull(savedMember.getCreatedAt());
        Assertions.assertNotNull(savedMember.getUpdatedAt());

        verify(castMemberGateway).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateCastMember_shouldThrowNotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidType_whenCallsCreateCastMember_shouldThrowNotificationException() {
        // given
        final String expectedName = name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

        verify(castMemberGateway, times(0)).create(any());
    }
}
