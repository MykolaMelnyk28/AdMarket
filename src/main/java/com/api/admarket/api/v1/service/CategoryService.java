package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.ad.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category create(Category category, String parentName);
    Category getRoot();
    Optional<Category> getByName(String categoryName);
    List<Category> getAllByParent(String parentName, boolean subInclude);
    Category updateByName(String categoryName, Category category);
    void deleteByName(String categoryName);
}
