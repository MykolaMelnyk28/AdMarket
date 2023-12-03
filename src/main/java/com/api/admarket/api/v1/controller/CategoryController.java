package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.CategoryRequest;
import com.api.admarket.api.v1.dto.ad.CategoryResponse;
import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<CategoryResponse> getRoot() {
        return ResponseEntity.ok(toDto(categoryService.getRoot()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody CategoryRequest request
    ) {
        Category created = categoryService.create(Category.builder()
                .name(request.getName())
                .build(), request.getParent());

        return new ResponseEntity<>(toDto(created), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> getByName(
            @PathVariable String categoryName
    ) {
        Optional<Category> found = categoryService.getById(categoryName);
        return ResponseEntity.of(found.map(this::toDto));
    }

    @PutMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> updateById(
            @PathVariable String categoryName,
            @RequestBody CategoryRequest request
    ) {
        Category updated = categoryService.updateById(categoryName, Category.builder()
                .name(request.getName())
                .build());

        return new ResponseEntity<>(toDto(updated), HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryName}")
    public HttpStatus deleteById(
            @PathVariable String categoryName
    ) {
        categoryService.deleteById(categoryName);
        return HttpStatus.OK;
    }

    private CategoryResponse toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponse dto = new CategoryResponse();
        dto.setName(category.getName());
        List<CategoryResponse> children = new ArrayList<>();

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            children = toDtoAll(category.getChildren());
        }
        dto.setChildren(children);
        return dto;
    }
    private List<CategoryResponse> toDtoAll(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .toList();
    }

}
