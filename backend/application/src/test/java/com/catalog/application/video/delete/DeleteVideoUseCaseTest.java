package com.catalog.application.video.delete;

import com.catalog.application.UseCaseTest;
import com.catalog.domain.exceptions.InternalErrorException;
import com.catalog.domain.video.VideoGateway;
import com.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteVideoUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(useCase);
    }

    @Test
    public void givestAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(videoGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(this.useCase.execute(expectedId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedId = VideoID.from("123");

        doNothing().when(videoGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(this.useCase.execute(expectedId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedId);
    }

    @Test
    public void givenAnValidId_whenCallsDeleteVideoAndGatewayThrowException_shouldReceiveException() {
        // given
        final var expectedId = VideoID.from("123");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException())).when(videoGateway).deleteById(any());

        // when
        Assertions.assertThrows(InternalErrorException.class,
                () -> this.useCase.execute(expectedId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedId);
    }
}
