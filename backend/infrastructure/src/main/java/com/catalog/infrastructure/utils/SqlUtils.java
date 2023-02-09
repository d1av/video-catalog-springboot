package com.catalog.infrastructure.utils;

import java.util.Objects;

public final class SqlUtils {

    private SqlUtils() {
    }

    public static String like(final String term) {
        if (Objects.isNull(term)) return null;
        return "%" + term + "%";
    }

    public static String upper(final String term) {
        if (Objects.isNull(term)) return null;
        return term.toUpperCase();
    }
}
