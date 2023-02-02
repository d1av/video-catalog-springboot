package com.catalog.application.castmember.update;

import com.catalog.application.Fixture;
import com.catalog.application.UseCaseTest;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UpdateCaseMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() throws InterruptedException {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        Thread.sleep(3000);

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway, times(1)).findById(any());

        verify(castMemberGateway).update(argThat(aUpdatedMember ->
                Objects.nonNull(aUpdatedMember.getId())
                        && Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore((aUpdatedMember.getUpdatedAt()))
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

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

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        // when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

//        verify(castMemberGateway).findById(expectedId);

        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowNotFoundException() {
        // given
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

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

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
