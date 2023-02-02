package com.catalog.application.castmember.delete;

import com.catalog.application.Fixture;
import com.catalog.application.UseCaseTest;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = aMember.getId();

        doNothing().when(castMemberGateway).deleteById(any());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        // then
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var expectedId = CastMemberID.from("123");

        doNothing().when(castMemberGateway).deleteById(any());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        // then
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = aMember.getId();

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        // then
        verify(castMemberGateway).deleteById(eq(expectedId));
    }
}
