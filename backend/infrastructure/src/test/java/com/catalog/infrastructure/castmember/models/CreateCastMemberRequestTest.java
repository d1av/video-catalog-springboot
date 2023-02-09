package com.catalog.infrastructure.castmember.models;

import com.catalog.Fixture;
import com.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CreateCastMemberRequestTest {
    @Autowired
    private JacksonTester<CreateCastMemberRequest> json;

    @Test
    public void testUnmarshal() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var json = """
                {
                "name": "%s",
                "type": "%s"
                }
                """.formatted(
                expectedName,
                expectedType
        );

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}