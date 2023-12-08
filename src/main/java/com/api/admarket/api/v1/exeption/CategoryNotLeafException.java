package com.api.admarket.api.v1.exeption;

import com.api.admarket.api.v1.entity.ad.Category;

public class CategoryNotLeafException extends RuntimeException {

    private final Category category;

    public CategoryNotLeafException(Category category) {
        super();
        this.category = category;
    }

    public CategoryNotLeafException(String message, Category category) {
        super(message);
        this.category = category;
    }

    public CategoryNotLeafException(Throwable cause, Category category) {
        super(cause);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
