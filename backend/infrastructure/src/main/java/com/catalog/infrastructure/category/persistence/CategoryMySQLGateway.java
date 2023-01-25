package com.catalog.infrastructure.category.persistence;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.domain.pagination.Pagination;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    @Override
    public Category create(Category aCategory) {
        return null;
    }

    @Override
    public void deleteById(CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }
}
