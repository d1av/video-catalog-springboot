package com.catalog.domain;

import com.catalog.domain.castmember.CastMemberType;
import net.datafaker.Faker;


public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static String description(Integer length) {
        length = Math.abs(length);
        if (length > 5000) length = 5000;

        StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            builder.append(FAKER.massEffect().quote());
            builder.append(FAKER.onePiece().quote());
            builder.append(FAKER.fallout().quote());
        }
        return builder.toString();
    }

    public static Integer year() {
        return FAKER.number().numberBetween(1000, 2000);
    }

    public static String title() {
        return FAKER.breakingBad().episode();
    }

    public static String name() {
        return FAKER.lordOfTheRings().character();
    }

    public static final class CastMember {
        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }
}
