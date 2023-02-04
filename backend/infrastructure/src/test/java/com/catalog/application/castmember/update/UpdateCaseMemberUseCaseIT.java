package com.catalog.application.castmember.update;

import com.catalog.Fixture;
import com.catalog.IntegrationTest;
import com.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCaseMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;
    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() throws InterruptedException {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        Thread.sleep(3000);

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway, times(1)).findById(any());

        final var aUpdatedMember = castMemberRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedId.getValue(), aUpdatedMember.getId());
        Assertions.assertEquals(expectedName, aUpdatedMember.getName());
        Assertions.assertEquals(expectedType, aUpdatedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt());
        Assertions.assertEquals(expectedId.getValue(), aUpdatedMember.getId());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore((aUpdatedMember.getUpdatedAt())));

        verify(castMemberGateway).update(any());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowNotFoundException() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = CastMemberID.from("123");
        final String expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

        verify(castMemberGateway).findById(any());

        verify(castMemberGateway, times(0)).update(any());
    }
}
