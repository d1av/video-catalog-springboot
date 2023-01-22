package com.catalog.domain.category;

public record CategorySearchQuery(
        int page,
        int petPage,
        String terms,
        String sort,
        String direction
) {

}
