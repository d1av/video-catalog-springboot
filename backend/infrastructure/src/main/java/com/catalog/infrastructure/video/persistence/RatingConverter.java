package com.catalog.infrastructure.video.persistence;

import com.catalog.domain.video.Rating;
import jakarta.persistence.AttributeConverter;

public class RatingConverter implements AttributeConverter<Rating, String> {
    @Override
    public String convertToDatabaseColumn(final Rating attribute) {
        if (attribute == null) return null;
        return attribute.getName();
    }

    @Override
    public Rating convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return Rating.of(dbData).orElse(null);
    }
}
