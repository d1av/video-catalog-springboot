package com.catalog.domain;

import com.catalog.domain.castmember.CastMemberType;
import net.datafaker.Faker;


public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static String description(Integer length) {
        length = Math.abs(length);
        if (length > 1000) length = 300;

        String description = "";
        while (description.length() < length) {
            String text1 = FAKER.massEffect().quote();
            String text2 = FAKER.onePiece().quote();
            String text3 = FAKER.fallout().quote();
            description = text1.concat(text2.concat(text3));
        }
        return description;
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
