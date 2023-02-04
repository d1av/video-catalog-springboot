package com.catalog.application.castmember.delete;

import com.catalog.Fixture;
import com.catalog.IntegrationTest;
import com.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {
    @Autowired
    private DeleteCastMemberUseCase useCase;
    @SpyBean
    private CastMemberGateway castMemberGateway;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember1 = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var aMember2 = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(aMember1),
                CastMemberJpaEntity.from(aMember2)
        ));

        final var expectedId = aMember1.getId().getValue();

        Assertions.assertEquals(2, castMemberRepository.count());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));
        // then
        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertFalse(castMemberRepository.existsById(expectedId));

        verify(castMemberGateway, times(1)).deleteById(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var actualMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var expectedId = CastMemberID.from("invalid-123");

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        // then
        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertTrue(castMemberRepository.existsById(actualMember.getId()));

        verify(castMemberGateway, times(1)).deleteById(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var actualMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        final var expectedErrorMessage = "Gateway error";

        final var expectedId = CastMemberID.from(actualMember.getId());

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());

        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(castMemberGateway, times(1)).deleteById(any());
    }
}
