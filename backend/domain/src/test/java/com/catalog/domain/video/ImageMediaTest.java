package com.catalog.domain.video;

import com.catalog.domain.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturn() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = Fixture.firstName() + ".png";
        final var expectedLocation = "/images/ac";

        // when
        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then
        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = Fixture.firstName() + ".png";
        final var expectedLocation = "/images/ac";

        final var img1 = ImageMedia.with(expectedChecksum, Fixture.name(), expectedLocation);
        final var img2 = ImageMedia.with(expectedChecksum, Fixture.name(), expectedLocation);

        // then
        Assertions.assertEquals(img1, img2);
        Assertions.assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with(null, Fixture.name(), "/images")
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", null, "/images")
        );

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", Fixture.name(), null)
        );
    }
}
