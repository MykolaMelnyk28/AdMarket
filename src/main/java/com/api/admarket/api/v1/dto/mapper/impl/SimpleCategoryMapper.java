package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.ad.CategoryDTO;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.entity.ad.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("simple")
public class SimpleCategoryMapper implements CategoryMapper {

    @Override
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setParent(getParentOrNull(dto));
        category.setChildren(getEntityChildrenOrEmptyList(dto));

        return category;
    }

    private Category getParentOrNull(CategoryDTO dto) {
        if (dto.getParent() == null) {
            return null;
        }
        Category parent = new Category();
        parent.setName(dto.getParent());
        return parent;
    }

    private List<Category> getEntityChildrenOrEmptyList(CategoryDTO dto) {
        List<CategoryDTO> dtos = dto.getChildren();
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setName(category.getName());
        dto.setParent(getParentOrNull(category));
        dto.setChildren(getChildrenOrEmptyList(category));

        return dto;
    }

    private String getParentOrNull(Category category) {
        return (category.getParent() == null) ? null : category.getParent().getName();
    }

    private List<CategoryDTO> getChildrenOrEmptyList(Category category) {
        List<Category> categories = category.getChildren();
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }
        return categories.stream()
                .map(this::toDto)
                .toList();
    }

}
