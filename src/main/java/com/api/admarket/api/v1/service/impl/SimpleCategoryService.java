package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.CategoryRepository;
import com.api.admarket.api.v1.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SimpleCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public SimpleCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category, String parentName)
            throws IllegalArgumentException {
        throwIfCategoryAlreadyExists(category.getName());

        if (parentName == null || parentName.equalsIgnoreCase("all")) {
            category.setParent(getOrThrow("all"));
        } else {
            Category parent = getOrThrow(parentName);
            parent.setLeaf(false);
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
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
        throwIfImmutableCategory(categoryName);
        Category found = getOrThrow(categoryName);
        copy(category, found);
        return found;
    }

    private void copy(Category source, Category destination) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getParent() != null) {
            destination.setParent(source.getParent());
        }
    }

    @Override
    public void deleteById(String categoryName) {
        throwIfImmutableCategory(categoryName);
        categoryRepository.deleteByName(categoryName);
    }

    private Category getOrThrow(String categoryName) {
        return getById(categoryName).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
    }

    private void throwIfCategoryAlreadyExists(String category) {
        if (getById(category).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
    }
    private void throwIfImmutableCategory(String name) {
        if ("all".equalsIgnoreCase(name) ||
                "other".equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("The category is immutable");
        }
    }
}
