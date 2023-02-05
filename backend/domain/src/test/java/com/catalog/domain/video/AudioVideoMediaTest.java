package com.catalog.domain.video;

import com.catalog.domain.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioVideoMediaTest {
    @Test
    public void givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = Fixture.firstName() + ".png";
        final var expectedLocation = "/images/ac";
        final var expectedEncodedLocation = "/images/ac-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var actualVideo =
                AudioVideoMedia.with(
                        expectedChecksum,
                        expectedName,
                        expectedLocation,
                        expectedEncodedLocation,
                        expectedStatus
                );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedChecksum, actualVideo.checksum());
        Assertions.assertEquals(expectedName, actualVideo.name());
        Assertions.assertEquals(expectedLocation, actualVideo.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
        Assertions.assertEquals(expectedStatus, actualVideo.status());
    }

    @Test
    public void givenTwoVideosWithSameChecksumAndLocation_whenCallsEqualsAudioVideoImage_shouldReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var exectedRawLocation = "images/" + Fixture.firstName() + ".png";

        final var img1 = AudioVideoMedia.with(expectedChecksum, Fixture.name(), "", "", MediaStatus.PENDING);
        final var img2 = AudioVideoMedia.with(expectedChecksum, Fixture.name(), "", "", MediaStatus.PENDING);

        // then
        Assertions.assertEquals(img1, img2);
        Assertions.assertNotSame(img1, img2);
    }

    @Test
    public void givenITwoVideos_whenCallsWith_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with(null, Fixture.name(), "/videos", "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", null, "/videos", "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", Fixture.name(), null, "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", Fixture.name(), "/videos", null, MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", Fixture.name(), "/videos", "/videos", null)
        );
    }
}
