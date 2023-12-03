package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.ad.CategoryRequest;
import com.api.admarket.api.v1.dto.ad.CategoryResponse;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.entity.ad.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("simple")
public class SimpleCategoryMapper implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse response = new CategoryResponse();
        response.setName(category.getName());
        response.setChildren(getChildrenOrEmptyList(category));

        return response;
    }

    private List<CategoryResponse> getChildrenOrEmptyList(Category category) {
        List<Category> categories = category.getChildren();
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }
        return category.getChildren().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        category.setName(request.getName());

        category.setParent(getParentCategoryOrNull(request));

        return category;
    }

    private Category getParentCategoryOrNull(CategoryRequest request) {
        if (request.getParent() == null) {
            return null;
        }
        Category parent = new Category();
        parent.setName(request.getParent());
        return parent;
    }

}
