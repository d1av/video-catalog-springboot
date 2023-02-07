package com.catalog.application;

import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.domain.category.Category;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.video.Rating;
import com.catalog.domain.video.Resource;
import com.catalog.domain.video.Video;
import net.datafaker.Faker;

import java.time.Year;
import java.util.Arrays;
import java.util.Set;

import static com.catalog.application.Fixture.Videos.*;
import static io.vavr.API.*;


public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static String firstName() {
        return FAKER.name().firstName();
    }

    public static String description(Integer length) {
        length = Math.abs(length);
        if (length > 5000) length = 5000;

        StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            builder.append(FAKER.massEffect().quote());
            builder.append(FAKER.onePiece().quote());
            builder.append(FAKER.fallout().quote());
        }

        String correctLengthString = builder.toString();
        if (correctLengthString.length() > length) {
            correctLengthString = correctLengthString.substring(0, length);
        }
        return correctLengthString;
    }

    public static Year year() {
        return Year.of(FAKER.number().numberBetween(2020, 2030));
    }

    public static Double duration() {
        return FAKER.number().randomDouble(1, 1, 120);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.breakingBad().episode();
    }

    public static String name() {
        return FAKER.lordOfTheRings().character();
    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.newMember("Wesley Snipes", CastMemberType.ACTOR);

        private static final CastMember MIA_MALKOVA =
                CastMember.newMember("Mia Malkova", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember mia() {
            return CastMember.with(MIA_MALKOVA);
        }


    }

    public static final class Categories {

        private static final Category AULAS =
                Category.newCategory("Aulas", "Some description", true);

        public static Category aulas() {
            return AULAS.clone();
        }
    }

    public static final class Genres {

        private static final Genre TECH = Genre.newGenre("Tecnology", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
    }

    public static final class Videos {

        public static Video systemDesign() {
            return Video.newVideo(
                    Fixture.title(),
                    description(),
                    Year.of(Fixture.year().getValue()),
                    Fixture.duration(),
                    Fixture.bool(),
                    Fixture.bool(),
                    rating(),
                    Set.of(Categories.aulas().getId()),
                    Set.of(Genres.tech().getId()),
                    Set.of(CastMembers.wesley().getId(), CastMembers.mia().getId())
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );
            final byte[] content = "Conteudo".getBytes();
            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static String description() {
            return FAKER.options().option(
                    "Java Logging API was introduced in 1.4 and you can use java logging API to log " +
                            "application messages. In this java logging tutorial, we will learn basic features of " +
                            "Java Logger. We will also look into Java Logger example of different logging levels, " +
                            "Logging Handlers, Formatters, Filters, Log Manager and logging configurations.",

                    "All of the logic would be handled by the AreaCalculator class. This would violate the " +
                            "single-responsibility principle. The AreaCalculator class should only be concerned with " +
                            "the sum of the areas of provided shapes. It should not care whether the user wants " +
                            "JSON or HTML.\n" +
                            "To address this, you can create a separate SumCalculatorOutputter class and use that" +
                            " new class to handle the logic you need to output the data to the user:"
            );
        }
    }
}

