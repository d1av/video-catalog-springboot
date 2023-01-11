package com.catalog.application;

import com.catalog.domain.category.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}