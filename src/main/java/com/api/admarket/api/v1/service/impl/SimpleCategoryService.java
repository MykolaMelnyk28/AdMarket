package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.CategoryRepository;
import com.api.admarket.api.v1.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        category.setLeaf(true);
        Category saved = categoryRepository.save(category);
        return saved;
    }

    @Override
    public Category getRoot() {
        return categoryRepository.findRoot();
    }

    @Override
    public Optional<Category> getByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<Category> getAllByParent(String parentName, boolean subInclude) {
        if (subInclude) {
            return getAllCategoryHierarchy(parentName);
        } else {
            Category category = getOrThrow(parentName);
            return (category.getChildren() == null) ? null : new ArrayList<>();
        }
    }

    public List<Category> getAllCategoryHierarchy(String categoryName) {
        Category category = getOrThrow(categoryName);
        List<Category> list = getCategoryHierarchyRcurs(category, new ArrayList<>());
        return list;
    }

    public List<Category> getCategoryHierarchyRcurs(Category category, List<Category> list) {
        List<Category> subCategory = category.getChildren();
        if (subCategory == null || subCategory.isEmpty()) {
            return new ArrayList<>();
        }
        for(Category c : subCategory) {
            list.add(c);
            getCategoryHierarchyRcurs(c, list);
        }
        return list;
    }

    @Override
    public Category updateByName(String categoryName, Category category) {
        throwIfImmutableCategory(categoryName);
        Category found = getOrThrow(categoryName);
        copy(category, found);
        return categoryRepository.save(found);
    }

    private void copy(Category source, Category destination) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getParent() != null) {
            Category parent = getOrThrow(source.getParent().getName());
            parent.setLeaf(false);
            Category dParent = destination.getParent();
            if (dParent.getChildren() == null || dParent.getChildren().size() < 2) {
                dParent.setLeaf(true);
            }
            destination.setParent(parent);
        }
    }

    @Override
    public void deleteByName(String categoryName) {
        throwIfImmutableCategory(categoryName);
        Category parent = getOrThrow(categoryName).getParent();
        if (parent.getChildren() == null || parent.getChildren().size() < 2) {
            parent.setLeaf(true);
        }
        categoryRepository.deleteByName(categoryName);
    }

    private Category getOrThrow(String categoryName) {
        return getByName(categoryName).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
    }

    private void throwIfCategoryAlreadyExists(String category) {
        if (getByName(category).isPresent()) {
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
