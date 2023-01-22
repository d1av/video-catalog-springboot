package com.catalog.application;

import com.catalog.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN onIn);

}