package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.CategoryRepository;
import com.api.admarket.api.v1.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public SimpleCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category, String parentName) {
        if (getById(category.getName()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        Category saved = categoryRepository.save(category);
        if (saved.getParent() == null) {
            categoryRepository.assignParent(saved.getName(),
                    (parentName != null)
                            ? parentName
                            : "all");
        }
        return saved;
    }

    @Override
    public Category getRoot() {
        return categoryRepository.findRoot();
    }

    @Override
    public Optional<Category> getById(String categoryName) {
        return categoryRepository.findById(categoryName);
    }

    @Override
    public List<Category> getAllByParent(String parentName, boolean subInclude) {
        List<Category> categories = getOrThrow(parentName).getChildren();
        if (!subInclude) {
            categories = categories.stream().peek(x -> x.setChildren(null)).toList();
        }
        return categories;
    }

    @Override
    public Category updateById(String categoryName, Category category) {
        if (categoryName.equalsIgnoreCase("all")) {
            throw new IllegalArgumentException("Change 'all' category");
        }
        Category found = getById(categoryName).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
        found.setName(category.getName());
        if (category.getChildren() != null) {
            found.setChildren(category.getChildren());
        }
        found.setParent(category.getParent());
        return found;
    }

    @Override
    public void deleteById(String categoryName) {
        if (categoryName.equalsIgnoreCase("all")) {
            throw new IllegalArgumentException("Change 'all' category");
        }
        categoryRepository.deleteById(categoryName);
    }

    private Category getOrThrow(String categoryName) {
        return getById(categoryName).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
    }
}
