package com.catalog.domain.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
    //    @JsonProperty("current_page")
        int currentPage,
  //      @JsonProperty("per_page")
        int perPage,
  //      @JsonProperty("total")
        long total,
        List<T> items
) {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> aNewList = this.items.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(currentPage(), perPage(), total(), aNewList);
    }
}
